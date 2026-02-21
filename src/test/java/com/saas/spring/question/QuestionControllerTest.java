package com.saas.spring.question;

import com.saas.spring.question.dto.QuestionOutDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuestionController.class)
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private QuestionService questionService;

    @Test
    void shouldReturnAllQuestions() throws Exception {
        // Given
        List<QuestionOutDto> expectedMockedQuestions = Arrays.asList(
                new QuestionOutDto(1L, "Pregunta 1"),
                new QuestionOutDto(2L, "Pregunta 2")
        );

        // When
        when(questionService.getAllQuestions()).thenReturn(expectedMockedQuestions);
        String expectedJson = objectMapper.writeValueAsString(expectedMockedQuestions);

        // Then
        mockMvc.perform(get("/questions"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(questionService).getAllQuestions();
    }

    @Test
    void shouldReturnAQuestion() throws Exception{
        // Given
        Long idToSearch = 1L;
        QuestionOutDto expectedMockedQuestion = new QuestionOutDto(idToSearch, "Pregunta1");

        // When
        when(questionService.getById(idToSearch)).thenReturn(expectedMockedQuestion);
        String expectedJson = objectMapper.writeValueAsString(expectedMockedQuestion);

        // Then
        mockMvc.perform(get("/questions/{id}", idToSearch))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(questionService).getById(idToSearch);
    }
}
