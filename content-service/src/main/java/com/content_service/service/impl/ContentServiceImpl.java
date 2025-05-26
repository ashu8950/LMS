package com.content_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.content_service.dto.ContentDTO;
import com.content_service.entity.Content;
import com.content_service.repository.ContentRepository;
import com.content_service.service.ContentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentRepository repository;

    @Override
    public ContentDTO createContent(ContentDTO dto) {
        Content content = Content.builder()
                .courseId(dto.getCourseId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .videoUrl(dto.getVideoUrl())
                .build();

        return convertToDTO(repository.save(content));
    }

    @Override
    public List<ContentDTO> getContentsByCourseId(Long courseId) {
        return repository.findByCourseId(courseId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ContentDTO getContentById(Long id) {
        Content content = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found with id: " + id));
        return convertToDTO(content);
    }

    @Override
    public void deleteContent(Long id) {
        repository.deleteById(id);
    }

    private ContentDTO convertToDTO(Content content) {
        ContentDTO dto = new ContentDTO();
        BeanUtils.copyProperties(content, dto);
        return dto;
    }
}
