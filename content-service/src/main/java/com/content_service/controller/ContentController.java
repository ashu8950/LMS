package com.content_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.content_service.dto.ContentDTO;
import com.content_service.service.ContentService;

import java.util.List;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @PostMapping
    public ResponseEntity<ContentDTO> createContent(@RequestBody ContentDTO dto) {
        return ResponseEntity.ok(contentService.createContent(dto));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ContentDTO>> getByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(contentService.getContentsByCourseId(courseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(contentService.getContentById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }
}
