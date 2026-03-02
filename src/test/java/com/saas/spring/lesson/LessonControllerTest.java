package com.saas.spring.lesson;

import com.saas.spring.exception.LessonExceptions;
import com.saas.spring.lesson.dto.LessonInDto;
import com.saas.spring.lesson.dto.LessonOutDto;
import com.saas.spring.lesson.dto.LessonUpdateDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LessonController.class)
public class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LessonService lessonService;

    @Test
    void shouldReturnAllLessons() throws Exception {
        // Given
        List<LessonOutDto> expectedLessons = Arrays.asList(
            new LessonOutDto(1L, "Introducción a Java", "Conceptos básicos de Java", null),
            new LessonOutDto(2L, "Estructuras de Datos", "Arrays, Listas y Mapas", null)
        );

        // When
        when(lessonService.getAllLessons()).thenReturn(expectedLessons);
        String expectedJson = objectMapper.writeValueAsString(expectedLessons);

        // Then
        mockMvc.perform(get("/lessons"))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));

        verify(lessonService).getAllLessons();
    }

    @Test
    void shouldReturnLessonById() throws Exception {
        // Given
        Long id = 1L;
        LessonOutDto expectedLesson = new LessonOutDto(
            id,
            "Introducción a Python",
            "Fundamentos del lenguaje Python", 
            null
        );

        // When
        when(lessonService.getById(id)).thenReturn(expectedLesson);
        String expectedJson = objectMapper.writeValueAsString(expectedLesson);

        // Then
        mockMvc.perform(get("/lessons/{id}", id))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));

        verify(lessonService).getById(id);
    }

    @Test
    void getLessonByIdShouldReturn404WhenNotExists() throws Exception {
        // Given
        Long id = 999L;
        when(lessonService.getById(id))
            .thenThrow(new LessonExceptions.LessonNotFoundException(id));

        // When & Then
        mockMvc.perform(get("/lessons/{id}", id))
            .andExpect(status().isNotFound());

        verify(lessonService, times(1)).getById(id);
    }

    @Test
    void shouldCreateLesson() throws Exception {
        // Given
        LessonInDto inputDto = new LessonInDto(
            "Introducción a Spring Boot",
            "Aprende los fundamentos de Spring Boot",
            null
        );

        LessonOutDto outputDto = new LessonOutDto(
            1L,
            "Introducción a Spring Boot",
            "Aprende los fundamentos de Spring Boot",
            null
        );

        // When
        when(lessonService.createLesson(any(LessonInDto.class))).thenReturn(outputDto);
        String expectedJson = objectMapper.writeValueAsString(outputDto);

        // Then
        mockMvc.perform(post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
            .andExpect(status().isCreated())
            .andExpect(content().json(expectedJson));

        verify(lessonService, times(1)).createLesson(any(LessonInDto.class));
    }

    @Test
    void createLessonShouldReturn400WhenTitleIsEmpty() throws Exception {
        // Given
        LessonInDto inputDto = new LessonInDto(
            "",
            "Descripción válida",
            null
        );

        // When & Then
        mockMvc.perform(post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
            .andExpect(status().isBadRequest());

        verify(lessonService, times(0)).createLesson(any(LessonInDto.class));
    }

    @Test
    void createLessonShouldReturn400WhenDescriptionIsTooLong() throws Exception {
        // Given
        String longDescription = "x".repeat(501);
        LessonInDto inputDto = new LessonInDto(
            "Título válido",
            longDescription,
            null
        );

        // When & Then
        mockMvc.perform(post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
            .andExpect(status().isBadRequest());

        verify(lessonService, times(0)).createLesson(any(LessonInDto.class));
    }

    @Test
    void shouldUpdateLesson() throws Exception {
        // Given
        Long id = 1L;
        LessonUpdateDto updateDto = new LessonUpdateDto(
            "Título Actualizado",
            "Descripción actualizada",
            null
        );

        LessonOutDto updatedLesson = new LessonOutDto(
            id,
            "Título Actualizado",
            "Descripción actualizada",
            null
        );

        // When
        when(lessonService.updateLesson(any(LessonUpdateDto.class), eq(id))).thenReturn(updatedLesson);
        String expectedJson = objectMapper.writeValueAsString(updatedLesson);

        // Then
        mockMvc.perform(patch("/lessons/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));

        verify(lessonService, times(1)).updateLesson(any(LessonUpdateDto.class), eq(id));
    }

    @Test
    void updateLessonShouldReturn404WhenNotExists() throws Exception {
        // Given
        Long id = 999L;
        LessonUpdateDto updateDto = new LessonUpdateDto(
            "Título Actualizado",
            null,
            null
        );

        when(lessonService.updateLesson(any(LessonUpdateDto.class), eq(id)))
            .thenThrow(new LessonExceptions.LessonNotFoundException(id));

        // When & Then
        mockMvc.perform(patch("/lessons/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
            .andExpect(status().isNotFound());

        verify(lessonService, times(1)).updateLesson(any(LessonUpdateDto.class), eq(id));
    }

    @Test
    void shouldDeleteLesson() throws Exception {
        // Given
        Long id = 1L;

        // When & Then
        mockMvc.perform(delete("/lessons/{id}", id))
            .andExpect(status().isNoContent());

        verify(lessonService, times(1)).deleteLesson(id);
    }

    @Test
    void deleteLessonShouldReturn404WhenNotExists() throws Exception {
        // Given
        Long id = 999L;
        doThrow(new LessonExceptions.LessonNotFoundException(id))
            .when(lessonService).deleteLesson(id);

        // When & Then
        mockMvc.perform(delete("/lessons/{id}", id))
            .andExpect(status().isNotFound());

        verify(lessonService, times(1)).deleteLesson(id);
    }
}
