package com.content_service.service;



import java.util.List;

import com.content_service.dto.ContentDTO;

public interface ContentService {
    ContentDTO createContent(ContentDTO dto);
    List<ContentDTO> getContentsByCourseId(Long courseId);
    ContentDTO getContentById(Long id);
    void deleteContent(Long id);
}

