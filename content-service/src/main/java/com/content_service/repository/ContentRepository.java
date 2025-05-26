package com.content_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.content_service.entity.Content;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByCourseId(Long courseId);
}
