package com.saas.spring.exception;

public class QuestionTypeExceptions {

    private QuestionTypeExceptions() {

    }

    public static class QuestionTypeNotFoundException extends RuntimeException {
        public QuestionTypeNotFoundException(Long questionTypeId) {
            super("Tipo de pregunta no encontrada con id: " + questionTypeId);
        }
        
    }

    public static class QuestionTypeCreationException extends RuntimeException {
        public QuestionTypeCreationException(String reason) {
            super("No se pudo crear el tipo de pregunta: " + reason);
        }
    }

    public static class QuestionTypeUpdateException extends RuntimeException {
        public QuestionTypeUpdateException(Long questionTypeId, String reason) {
            super("No se pudo actualizar el tipo de pregunta " + questionTypeId + ": " + reason);
        }
    }

    public static class QuestionTypeDeletionException extends RuntimeException {
        public QuestionTypeDeletionException(Long questionTypeId, String reason) {
            super("No se pudo eliminar el tipo de pregunta " + questionTypeId + ": " + reason);
        }
    }
}
