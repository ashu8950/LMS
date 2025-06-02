package com.batch_service.service.impl;

import com.batch_service.client.CourseClient;
import com.batch_service.client.NotificationClient;
import com.batch_service.config.RequestHeaderContext;
import com.batch_service.dto.BatchDTO;
import com.batch_service.dto.NotificationRequest;
import com.batch_service.entity.Batch;
import com.batch_service.mapper.BatchMapper;
import com.batch_service.repository.BatchRepository;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BatchService {

    private final BatchRepository batchRepository;
    private final BatchMapper batchMapper;
    private final CourseClient courseClient;
    private final NotificationClient notificationClient;
    private final RequestHeaderContext requestHeaderContext;

    public BatchService(BatchRepository batchRepository,
                        BatchMapper batchMapper,
                        CourseClient courseClient,
                        NotificationClient notificationClient,
                        RequestHeaderContext requestHeaderContext) {
        this.batchRepository = batchRepository;
        this.batchMapper = batchMapper;
        this.courseClient = courseClient;
        this.notificationClient = notificationClient;
        this.requestHeaderContext = requestHeaderContext;
    }

    public BatchDTO createBatch(BatchDTO batchDTO) {
        if (batchDTO.getCourseId() == null) {
            throw new IllegalArgumentException("Course ID is required to create a batch.");
        }

        if (!isCourseExists(batchDTO.getCourseId())) {
            throw new EntityNotFoundException("Course not found with ID: " + batchDTO.getCourseId());
        }

        boolean exists = batchRepository.existsByNameAndCourseId(batchDTO.getName(), batchDTO.getCourseId());
        if (exists) {
            throw new IllegalStateException("Batch already exists with name '" + batchDTO.getName()
                    + "' for course ID " + batchDTO.getCourseId());
        }

        Batch batch = batchMapper.toEntity(batchDTO);
        Batch savedBatch = batchRepository.save(batch);
        BatchDTO savedDTO = batchMapper.toDTO(savedBatch);

        sendNotification(
            "schedule",
            "Batch Created: " + savedDTO.getName(),
            "<p>Batch <strong>" + savedDTO.getName() + "</strong> has been created under course ID "
            + savedDTO.getCourseId() + ".</p>"
        );

        return savedDTO;
    }

    public List<BatchDTO> getAllBatches() {
        return batchRepository.findAll()
                .stream()
                .map(batchMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BatchDTO getBatchById(Long id) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Batch not found with ID: " + id));
        return batchMapper.toDTO(batch);
    }

    public BatchDTO updateBatch(Long id, BatchDTO updatedDTO) {
        Batch existingBatch = batchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Batch not found with ID: " + id));

        // Only overwrite name if provided
        if (updatedDTO.getName() != null) {
            existingBatch.setName(updatedDTO.getName());
        }

        // Only overwrite description if provided
        if (updatedDTO.getDescription() != null) {
            existingBatch.setDescription(updatedDTO.getDescription());
        }

        // Only change courseId if provided AND different
        if (updatedDTO.getCourseId() != null &&
            !updatedDTO.getCourseId().equals(existingBatch.getCourseId())) {
            if (!isCourseExists(updatedDTO.getCourseId())) {
                throw new EntityNotFoundException("Course not found with ID: " + updatedDTO.getCourseId());
            }
            existingBatch.setCourseId(updatedDTO.getCourseId());
        }

        try {
            Batch savedBatch = batchRepository.save(existingBatch);
            BatchDTO savedDTO = batchMapper.toDTO(savedBatch);

            sendNotification(
                "schedule",
                "Batch Updated: " + savedDTO.getName(),
                "<p>Batch <strong>" + savedDTO.getName() + "</strong> has been updated.</p>"
            );

            return savedDTO;
        } catch (OptimisticLockingFailureException e) {
            log.error("Optimistic locking failed for batch ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Batch was updated by another transaction. Please reload and try again.", e);
        }
    }

    public void deleteBatch(Long id) {
        Batch batchToDelete = batchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Batch not found with ID: " + id));
        batchRepository.delete(batchToDelete);

        sendNotification(
            "schedule",
            "Batch Deleted: " + batchToDelete.getName(),
            "<p>Batch <strong>" + batchToDelete.getName() + "</strong> has been deleted.</p>"
        );
    }

    private boolean isCourseExists(Long courseId) {
        try {
            Boolean exists = courseClient.existById(courseId);
            return Boolean.TRUE.equals(exists);
        } catch (FeignException.NotFound e) {
            return false;
        } catch (FeignException e) {
            log.error("Error calling course-service for course ID {}: {}", courseId, e.getMessage());
            throw new RuntimeException("Could not validate course existence due to service error.", e);
        }
    }

    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs != null ? attrs.getRequest() : null;
    }

    private void sendNotification(String type, String subject, String htmlBody) {
        HttpServletRequest request = getCurrentHttpRequest();
        String userEmail = request != null ? requestHeaderContext.getUserEmail(request) : null;
        if (userEmail == null || userEmail.isEmpty()) {
            userEmail = "admin@lms.com";
        }

        NotificationRequest notification = NotificationRequest.builder()
                .type(type)
                .to(userEmail)
                .subject(subject)
                .body(htmlBody)
                .build();

        try {
            notificationClient.sendNotification(notification);
            log.info("Notification sent to {}: {}", userEmail, subject);
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage(), e);
        }
    }
}
