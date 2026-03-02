package com.saas.spring.course;

import com.saas.spring.course.dto.CourseInDto;
import com.saas.spring.course.dto.CourseOutDto;
import com.saas.spring.course.dto.CourseUpdateDto;
import com.saas.spring.exception.CourseExceptions;

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

@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CourseService courseService;

    @Test
    void shouldReturnAllCourses() throws Exception {
        // Given
        List<CourseOutDto> expectedCourses = Arrays.asList(
            new CourseOutDto(1L, "Curso de Java", "Aprende Java desde cero"),
            new CourseOutDto(2L, "Curso de Spring Boot", "Domina Spring Boot")
        );

        // When
        when(courseService.getAllCourses()).thenReturn(expectedCourses);
        String expectedJson = objectMapper.writeValueAsString(expectedCourses);

        // Then
        mockMvc.perform(get("/courses"))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));

        verify(courseService).getAllCourses();
    }

    @Test
    void shouldReturnCourseById() throws Exception {
        // Given
        Long id = 1L;
        CourseOutDto expectedCourse = new CourseOutDto(
            id,
            "Curso de Python",
            "Fundamentos de Python"
        );

        // When
        when(courseService.getById(id)).thenReturn(expectedCourse);
        String expectedJson = objectMapper.writeValueAsString(expectedCourse);

        // Then
        mockMvc.perform(get("/courses/{id}", id))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));

        verify(courseService).getById(id);
    }

    @Test
    void getCourseByIdShouldReturn404WhenNotExists() throws Exception {
        // Given
        Long id = 999L;
        when(courseService.getById(id))
            .thenThrow(new CourseExceptions.CourseNotFoundException(id));

        // When & Then
        mockMvc.perform(get("/courses/{id}", id))
            .andExpect(status().isNotFound());

        verify(courseService, times(1)).getById(id);
    }

    @Test
    void shouldCreateCourse() throws Exception {
        // Given
        CourseInDto inputDto = new CourseInDto(
            "Curso de Spring Framework",
            "Aprende Spring Framework completo"
        );

        CourseOutDto outputDto = new CourseOutDto(
            1L,
            "Curso de Spring Framework",
            "Aprende Spring Framework completo"
        );

        // When
        when(courseService.createCourse(any(CourseInDto.class))).thenReturn(outputDto);
        String expectedJson = objectMapper.writeValueAsString(outputDto);

        // Then
        mockMvc.perform(post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
            .andExpect(status().isCreated())
            .andExpect(content().json(expectedJson));

        verify(courseService, times(1)).createCourse(any(CourseInDto.class));
    }

    @Test
    void createCourseShouldReturn400WhenTitleIsEmpty() throws Exception {
        // Given
        CourseInDto inputDto = new CourseInDto(
            "",
            "Descripción válida"
        );

        // When & Then
        mockMvc.perform(post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
            .andExpect(status().isBadRequest());

        verify(courseService, times(0)).createCourse(any(CourseInDto.class));
    }

    @Test
    void createCourseShouldReturn400WhenDescriptionIsTooLong() throws Exception {
        // Given
        String longDescription = "x".repeat(1001);
        CourseInDto inputDto = new CourseInDto(
            "Título válido",
            longDescription
        );

        // When & Then
        mockMvc.perform(post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
            .andExpect(status().isBadRequest());

        verify(courseService, times(0)).createCourse(any(CourseInDto.class));
    }

    @Test
    void shouldUpdateCourse() throws Exception {
        // Given
        Long id = 1L;
        CourseUpdateDto updateDto = new CourseUpdateDto(
            "Título Actualizado",
            "Descripción actualizada"
        );

        CourseOutDto updatedCourse = new CourseOutDto(
            id,
            "Título Actualizado",
            "Descripción actualizada"
        );

        // When
        when(courseService.updateCourse(any(CourseUpdateDto.class), eq(id))).thenReturn(updatedCourse);
        String expectedJson = objectMapper.writeValueAsString(updatedCourse);

        // Then
        mockMvc.perform(patch("/courses/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));

        verify(courseService, times(1)).updateCourse(any(CourseUpdateDto.class), eq(id));
    }

    @Test
    void updateCourseShouldReturn404WhenNotExists() throws Exception {
        // Given
        Long id = 999L;
        CourseUpdateDto updateDto = new CourseUpdateDto(
            "Título Actualizado",
            null
        );

        when(courseService.updateCourse(any(CourseUpdateDto.class), eq(id)))
            .thenThrow(new CourseExceptions.CourseNotFoundException(id));

        // When & Then
        mockMvc.perform(patch("/courses/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
            .andExpect(status().isNotFound());

        verify(courseService, times(1)).updateCourse(any(CourseUpdateDto.class), eq(id));
    }

    @Test
    void shouldDeleteCourse() throws Exception {
        // Given
        Long id = 1L;

        // When & Then
        mockMvc.perform(delete("/courses/{id}", id))
            .andExpect(status().isNoContent());

        verify(courseService, times(1)).deleteCourse(id);
    }

    @Test
    void deleteCourseShouldReturn404WhenNotExists() throws Exception {
        // Given
        Long id = 999L;
        doThrow(new CourseExceptions.CourseNotFoundException(id))
            .when(courseService).deleteCourse(id);

        // When & Then
        mockMvc.perform(delete("/courses/{id}", id))
            .andExpect(status().isNotFound());

        verify(courseService, times(1)).deleteCourse(id);
    }
}
