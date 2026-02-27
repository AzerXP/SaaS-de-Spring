package com.saas.spring.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==================== EXCEPCIONES DE QUESTION ====================
    
    @ExceptionHandler(QuestionExceptions.QuestionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleQuestionNotFound(
            QuestionExceptions.QuestionNotFoundException ex,
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "La pregunta solicitada no existe en el sistema",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
        QuestionExceptions.QuestionCreationException.class,
        QuestionExceptions.QuestionUpdateException.class,
        QuestionExceptions.QuestionDeletionException.class
    })
    public ResponseEntity<ErrorResponse> handleQuestionOperationException(
            RuntimeException ex,
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "Error en operación con pregunta",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ==================== EXCEPCIONES DE QUESTION TYPE ====================
    
    @ExceptionHandler(QuestionTypeExceptions.QuestionTypeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleQuestionTypeNotFound(
            QuestionTypeExceptions.QuestionTypeNotFoundException ex,
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "El tipo de pregunta solicitado no existe en el sistema",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
        QuestionTypeExceptions.QuestionTypeCreationException.class,
        QuestionTypeExceptions.QuestionTypeUpdateException.class,
        QuestionTypeExceptions.QuestionTypeDeletionException.class
    })
    public ResponseEntity<ErrorResponse> handleQuestionTypeOperationException(
            RuntimeException ex,
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "Error en operación con tipo de pregunta",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ==================== EXCEPCIONES DE QUESTION CONFIG ====================
    
    @ExceptionHandler(QuestionConfigExceptions.QuestionConfigNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleQuestionConfigNotFound(
            QuestionConfigExceptions.QuestionConfigNotFoundException ex,
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "La configuracion de pregunta solicitado no existe en el sistema",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
        QuestionConfigExceptions.QuestionConfigCreationException.class,
        QuestionConfigExceptions.QuestionConfigUpdateException.class,
        QuestionConfigExceptions.QuestionConfigDeletionException.class
    })
    public ResponseEntity<ErrorResponse> handleQuestionConfigOperationException(
            RuntimeException ex,
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "Error en operación con configuracion de pregunta",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ==================== EXCEPCIONES DE VALIDACIÓN ====================
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        ErrorResponse error = new ErrorResponse(
            "Error de validación en los datos de entrada",
            errors.toString(),
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            "Error de validación",
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // ==================== EXCEPCIONES DE PARÁMETROS ====================
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            "Argumento inválido proporcionado",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        
        String message = String.format("El parámetro '%s' debe ser de tipo %s", 
            ex.getName(), ex.getRequiredType().getSimpleName());
        
        ErrorResponse error = new ErrorResponse(
            message,
            "Tipo de dato incorrecto",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // ==================== EXCEPCIÓN GENÉRICA (FALLBACK) ====================
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        
        // Log del error para debugging (importante)
        // log.error("Error no controlado: ", ex);
        
        ErrorResponse error = new ErrorResponse(
            "Error interno del servidor",
            "Ha ocurrido un error inesperado. Por favor, contacte al administrador.",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}