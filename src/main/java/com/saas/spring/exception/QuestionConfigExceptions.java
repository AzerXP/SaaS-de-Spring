package com.saas.spring.exception;

public class QuestionConfigExceptions {

    private QuestionConfigExceptions() {

    }

    public static class QuestionConfigNotFoundException extends RuntimeException {
        public QuestionConfigNotFoundException(Long questionId) {
            super("Configuracion de pregunta no encontrada con id: " + questionId);
        }
        
    }

    public static class QuestionConfigCreationException extends RuntimeException {
        public QuestionConfigCreationException(String reason) {
            super("No se pudo crear la Configuracion de pregunta: " + reason);
        }
    }

    public static class QuestionConfigUpdateException extends RuntimeException {
        public QuestionConfigUpdateException(Long questionId, String reason) {
            super("No se pudo actualizar la Configuracion de pregunta " + questionId + ": " + reason);
        }
    }

    public static class QuestionConfigDeletionException extends RuntimeException {
        public QuestionConfigDeletionException(Long questionId, String reason) {
            super("No se pudo eliminar la Configuracion de pregunta " + questionId + ": " + reason);
        }
    }
}
