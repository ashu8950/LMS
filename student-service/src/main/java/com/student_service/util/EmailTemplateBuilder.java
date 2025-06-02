package com.student_service.util;

import java.time.LocalDate;

public class EmailTemplateBuilder {

    public static String buildRegisterBody(String name, LocalDate date) {
        return "<p>Hi " + name + ",</p>"
             + "<p>Welcome to LMS! You registered on " + date + ".</p>"
             + "<p>We're excited to have you here!</p><p>– LMS Team</p>";
    }

    public static String buildEnrollmentBody(String name, Long courseId, Long batchId) {
        return "<p>Hi " + name + ",</p>"
             + "<p>Your enrollment has been confirmed:</p>"
             + "<ul><li>Course ID: " + courseId + "</li><li>Batch ID: " + batchId + "</li></ul>"
             + "<p>Wishing you success in your learning journey!</p><p>– LMS Team</p>";
    }

    public static String buildEnrollmentBodyWithNames(String firstName, Long courseId, String courseTitle, Long batchId, String batchName) {
        return "<p>Hi " + firstName + ",</p>"
             + "<p>Your enrollment has been confirmed:</p>"
             + "<ul>"
             + "<li>Course: " + courseTitle + " (ID: " + courseId + ")</li>"
             + "<li>Batch: " + batchName + " (ID: " + batchId + ")</li>"
             + "</ul>"
             + "<p>Wishing you success in your learning journey!</p><p>– LMS Team</p>";
    }

    public static String buildFeedbackBody(String name, LocalDate date) {
        return "<p>Hi " + name + ",</p>"
             + "<p>Thank you for your feedback submitted on " + date + ".</p>"
             + "<p>We truly appreciate your insights.</p><p>– LMS Team</p>";
    }

    public static String buildUpdateBody(String firstName, LocalDate updatedDate) {
        return String.format(
            "Dear %s,\n\nYour profile has been successfully updated in our Learning Management System on %s.\n\n"
            + "If you did not make this change or have any concerns, please contact our support team.\n\n"
            + "Best regards,\nLMS Team",
            firstName,
            updatedDate.toString()
        );
    }
}
