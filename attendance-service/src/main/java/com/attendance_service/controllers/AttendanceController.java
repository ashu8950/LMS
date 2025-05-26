package com.attendance_service.controllers;

import com.attendance_service.dto.AttendanceRequest;
import com.attendance_service.dto.AttendanceResponse;
import com.attendance_service.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<AttendanceResponse> markAttendance(@RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.markAttendance(request));
    }

    @GetMapping
    public ResponseEntity<List<AttendanceResponse>> getAttendance(@RequestParam Long studentId,
                                                                  @RequestParam Long courseId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByStudentAndCourse(studentId, courseId));
    }
}
