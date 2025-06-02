package com.batch_service.contollers;

import com.batch_service.config.RequestHeaderContext;
import com.batch_service.dto.BatchDTO;
import com.batch_service.service.impl.BatchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batches")
@RequiredArgsConstructor
public class BatchController {

    private final BatchService batchService;
    private final RequestHeaderContext requestHeaderContext;

    @PostMapping
    public ResponseEntity<BatchDTO> createBatch(@RequestBody BatchDTO batchDTO,
                                                HttpServletRequest request) {
        if (!isAdminOrInstructor(request)) {
            return ResponseEntity.status(403).build();
        }
        BatchDTO createdBatch = batchService.createBatch(batchDTO);
        return ResponseEntity.ok(createdBatch);
    }

    @GetMapping
    public ResponseEntity<List<BatchDTO>> getAllBatches(HttpServletRequest request) {
        if (!isAdminOrInstructor(request)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(batchService.getAllBatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchDTO> getBatchById(@PathVariable Long id,
                                                 HttpServletRequest request) {
        if (!isAdminOrInstructor(request)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(batchService.getBatchById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BatchDTO> updateBatch(@PathVariable Long id,
                                                @RequestBody BatchDTO batchDTO,
                                                HttpServletRequest request) {
        if (!isAdminOrInstructor(request)) {
            return ResponseEntity.status(403).build();
        }
        BatchDTO updatedBatch = batchService.updateBatch(id, batchDTO);
        return ResponseEntity.ok(updatedBatch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBatch(@PathVariable Long id, HttpServletRequest request) {
        if (!isAdminOrInstructor(request)) {
            return ResponseEntity.status(403).build();
        }
        batchService.deleteBatch(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isAdminOrInstructor(HttpServletRequest request) {
        return requestHeaderContext.hasRole(request, "ADMIN") ||
               requestHeaderContext.hasRole(request, "INSTRUCTOR");
    }
}
