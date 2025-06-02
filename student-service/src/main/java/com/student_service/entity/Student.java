package com.student_service.entity;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    /** Optional: your own internal code */
    @Column(name = "student_code", unique = true)
    private String studentCode;

    /** e.g. ACTIVE, SUSPENDED, etc. */
    private String status;

    @Column(name = "joined_date", nullable = false)
    private LocalDate joinedDate;

    /**
     * All the enrollments for this student.
     * Orphan removal ensures that if you remove an Enrollment
     * from this list, it also gets deleted in the DB.
     */
    @OneToMany(
      mappedBy = "student",
      cascade = CascadeType.ALL,
      orphanRemoval = true
    )
    private List<Enrollment> enrollments;
}
