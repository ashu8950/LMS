package com.schedule_service.service;


import java.util.List;

import com.schedule_service.dto.ScheduleDTO;

public interface ScheduleService {
    ScheduleDTO createSchedule(ScheduleDTO dto);
    List<ScheduleDTO> getSchedulesByCourse(Long courseId);
    List<ScheduleDTO> getSchedulesByBatch(Long batchId);
    ScheduleDTO getScheduleById(Long id);
    void deleteSchedule(Long id);
}
