package com.saas.spring.question;

import com.saas.spring.exception.QuestionExceptions;
import com.saas.spring.question.dto.QuestionInDto;
import com.saas.spring.question.dto.QuestionOutDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                new QuestionOutDto(1L, "Pregunta 1", 1L),
                new QuestionOutDto(2L, "Pregunta 2", 1L)
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
        QuestionOutDto expectedMockedQuestion = new QuestionOutDto(idToSearch, "Pregunta1", 1L);

        // When
        when(questionService.getById(idToSearch)).thenReturn(expectedMockedQuestion);
        String expectedJson = objectMapper.writeValueAsString(expectedMockedQuestion);

        // Then
        mockMvc.perform(get("/questions/{id}", idToSearch))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(questionService).getById(idToSearch);
    }

    @Test
    void getQuestionByIdShouldReturn404WhenNotExists() throws Exception {

        Long id = 999L;
        when(questionService.getById(id)).thenThrow(new QuestionExceptions.QuestionNotFoundException(id));


        mockMvc.perform(get("/questions/{id}", id))
                .andExpect(status().isNotFound());

        verify(questionService, times(1)).getById(id);
    }

    @Test
    void createQuestionShouldReturn201WhenValid() throws Exception {

        QuestionInDto inDto = new QuestionInDto("Nueva pregunta", 1L, null);
        QuestionOutDto outDto = new QuestionOutDto(1L, "Nueva pregunta", 1L);

        when(questionService.createQuestion(inDto))
                .thenReturn(outDto);


        mockMvc.perform(post("/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("Nueva pregunta")))
                .andExpect(jsonPath("$.questionTypeId", is(1)));

        verify(questionService, times(1)).createQuestion(inDto);
    }
}
