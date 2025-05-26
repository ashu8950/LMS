package com.attendance_service.service;

import com.attendance_service.dto.AttendanceRequest;
import com.attendance_service.dto.AttendanceResponse;

import java.util.List;

public interface AttendanceService {
    AttendanceResponse markAttendance(AttendanceRequest request);
    List<AttendanceResponse> getAttendanceByStudentAndCourse(Long studentId, Long courseId);
}
