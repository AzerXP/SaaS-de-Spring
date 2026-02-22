package com.saas.spring.exception;

public class QuestionExceptions {

    private QuestionExceptions() {

    }

    public static class QuestionNotFoundException extends RuntimeException {
        public QuestionNotFoundException(Long questionId) {
            super("Pregunta no encontrada con id: " + questionId);
        }
        
    }

    public static class QuestionCreationException extends RuntimeException {
        public QuestionCreationException(String reason) {
            super("No se pudo crear la pregunta: " + reason);
        }
    }

    public static class QuestionUpdateException extends RuntimeException {
        public QuestionUpdateException(Long questionId, String reason) {
            super("No se pudo actualizar la pregunta " + questionId + ": " + reason);
        }
    }

    public static class QuestionDeletionException extends RuntimeException {
        public QuestionDeletionException(Long questionId, String reason) {
            super("No se pudo eliminar la pregunta " + questionId + ": " + reason);
        }
    }
}
