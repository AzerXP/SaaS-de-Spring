package com.saas.spring.exception;

public class LessonExceptions {

    public static class LessonNotFoundException extends RuntimeException {
        public LessonNotFoundException(Long id) {
            super("Lección no encontrada con id: " + id);
        }
    }

    public static class InvalidLessonDataException extends RuntimeException {
        public InvalidLessonDataException(String message) {
            super("Datos inválidos para lección: " + message);
        }
    }

    private LessonExceptions() {
        throw new IllegalStateException("Utility class");
    }
}
