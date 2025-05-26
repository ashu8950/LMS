package com.schedule_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.schedule_service.entity.Schedule;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByCourseId(Long courseId);
    List<Schedule> findByBatchId(Long batchId);
}
