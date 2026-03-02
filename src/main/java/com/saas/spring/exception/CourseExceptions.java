package com.saas.spring.exception;

public class CourseExceptions {

    public static class CourseNotFoundException extends RuntimeException {
        public CourseNotFoundException(Long id) {
            super("Curso no encontrado con id: " + id);
        }
    }

    public static class InvalidCourseDataException extends RuntimeException {
        public InvalidCourseDataException(String message) {
            super("Datos inválidos para curso: " + message);
        }
    }

    private CourseExceptions() {
        throw new IllegalStateException("Utility class");
    }
}
