package com.student_service.entity;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "enrollment")
@Getter
@Setter
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "batch_id", nullable = false)
    private Long batchId;

    
    @ManyToOne(optional = false)
    @JoinColumn(
      name = "student_user_id",
      referencedColumnName = "user_id",
      nullable = false
    )
    private Student student;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    /** e.g. ACTIVE, DROPPED, COMPLETED */
    private String status;
}
