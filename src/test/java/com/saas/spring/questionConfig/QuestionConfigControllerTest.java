package com.saas.spring.questionConfig;

import com.saas.spring.exception.QuestionConfigExceptions;
import com.saas.spring.questionConfig.dto.QuestionConfigInDto;
import com.saas.spring.questionConfig.dto.QuestionConfigOutDto;
import com.saas.spring.questionConfig.dto.QuestionConfigUpdateDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

@WebMvcTest(QuestionConfigController.class)
public class QuestionConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private QuestionConfigService questionConfigService;

    @Test
    void shouldReturnAllQuestionConfigs() throws Exception {
        // Given
        List<QuestionConfigOutDto> expectedMockedConfigs = Arrays.asList(
            new QuestionConfigOutDto(1L, Map.of("key1", "value1")),
            new QuestionConfigOutDto(2L, Map.of("key2", "value2"))
        );

        // When
        when(questionConfigService.getAll()).thenReturn(expectedMockedConfigs);
        String expectedJson = objectMapper.writeValueAsString(expectedMockedConfigs);

        // Then
        mockMvc.perform(get("/question_configs"))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));

        verify(questionConfigService).getAll();
    }

    @Test
    void shouldReturnAQuestionConfig() throws Exception {
        // Given
        Long idToSearch = 1L;
        QuestionConfigOutDto expectedMockedConfig = new QuestionConfigOutDto(
            idToSearch, 
            Map.of("options", Arrays.asList(
                Map.of("id", 1, "text", "Option 1", "is_correct", false),
                Map.of("id", 2, "text", "Option 2", "is_correct", true)
            ))
        );

        // When
        when(questionConfigService.getById(idToSearch)).thenReturn(expectedMockedConfig);
        String expectedJson = objectMapper.writeValueAsString(expectedMockedConfig);

        // Then
        mockMvc.perform(get("/question_configs/{id}", idToSearch))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));

        verify(questionConfigService).getById(idToSearch);
    }

    @Test
    void getQuestionConfigByIdShouldReturn404WhenNotExists() throws Exception {
        // Given
        Long id = 999L;
        when(questionConfigService.getById(id))
            .thenThrow(new QuestionConfigExceptions.QuestionConfigNotFoundException(id));

        // When & Then
        mockMvc.perform(get("/question_configs/{id}", id))
            .andExpect(status().isNotFound());

        verify(questionConfigService, times(1)).getById(id);
    }

    @Test
    void createQuestionConfigShouldReturn201WhenValid() throws Exception {
        // Given
        QuestionConfigInDto inDto = new QuestionConfigInDto(
            1L,
            Map.of(
                "options", Arrays.asList(
                    Map.of("id", 1, "text", "Java", "is_correct", false),
                    Map.of("id", 2, "text", "Python", "is_correct", true)
                ),
                "max_selection", 1
            )
        );
        QuestionConfigOutDto outDto = new QuestionConfigOutDto(1L, inDto.config());

        when(questionConfigService.createQuestionConfig(any(QuestionConfigInDto.class)))
            .thenReturn(outDto);

        String expectedJson = objectMapper.writeValueAsString(outDto);

        // When & Then
        mockMvc.perform(post("/question_configs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inDto)))
            .andExpect(status().isCreated())
            .andExpect(content().json(expectedJson));

        verify(questionConfigService, times(1)).createQuestionConfig(any(QuestionConfigInDto.class));
    }

    @Test
    void createQuestionConfigShouldReturn400WhenInvalidSchema() throws Exception {
        // Given - Config inválido (faltan campos requeridos según schema)
        QuestionConfigInDto inDto = new QuestionConfigInDto(
            1L,
            Map.of(
                "options", Arrays.asList(
                    Map.of("id", 1, "text", "Java") // Falta 'is_correct'
                ),
                "max_selection", "uno" // Debería ser integer, no string
            )
        );

        when(questionConfigService.createQuestionConfig(any(QuestionConfigInDto.class)))
            .thenThrow(new QuestionConfigExceptions.InvalidConfigSchemaException(
                "$.options[0]: required property 'is_correct' not found; " +
                "$.max_selection: expected type 'integer', found 'string'"
            ));

        // When & Then
        mockMvc.perform(post("/question_configs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inDto)))
            .andExpect(status().isBadRequest());

        verify(questionConfigService, times(1)).createQuestionConfig(any(QuestionConfigInDto.class));
    }

    @Test
    void updateQuestionConfigShouldReturn200WhenValid() throws Exception {
        // Given
        Long configId = 1L;
        QuestionConfigUpdateDto updateDto = new QuestionConfigUpdateDto(
            null,
            Map.of(
                "options", Arrays.asList(
                    Map.of("id", 1, "text", "Updated Option 1", "is_correct", false),
                    Map.of("id", 2, "text", "Updated Option 2", "is_correct", true)
                ),
                "max_selection", 1,
                "shuffle_options", true
            )
        );
        QuestionConfigOutDto outDto = new QuestionConfigOutDto(configId, updateDto.config());

        when(questionConfigService.updateQuestionConfig(any(QuestionConfigUpdateDto.class), eq(configId)))
            .thenReturn(outDto);

        String expectedJson = objectMapper.writeValueAsString(outDto);

        // When & Then
        mockMvc.perform(patch("/question_configs/{id}", configId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));

        verify(questionConfigService, times(1))
            .updateQuestionConfig(any(QuestionConfigUpdateDto.class), eq(configId));
    }

    @Test
    void updateQuestionConfigShouldReturn400WhenInvalidSchema() throws Exception {
        // Given
        Long configId = 1L;
        QuestionConfigUpdateDto updateDto = new QuestionConfigUpdateDto(
            null,
            Map.of("invalid_field", "bad data") // No cumple con el schema
        );

        when(questionConfigService.updateQuestionConfig(any(QuestionConfigUpdateDto.class), eq(configId)))
            .thenThrow(new QuestionConfigExceptions.InvalidConfigSchemaException(
                "$: required property 'options' not found"
            ));

        // When & Then
        mockMvc.perform(patch("/question_configs/{id}", configId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
            .andExpect(status().isBadRequest());

        verify(questionConfigService, times(1))
            .updateQuestionConfig(any(QuestionConfigUpdateDto.class), eq(configId));
    }

    @Test
    void deleteQuestionConfigShouldReturn204WhenValid() throws Exception {
        // Given
        Long idToDelete = 1L;

        // When & Then
        mockMvc.perform(delete("/question_configs/{id}", idToDelete))
            .andExpect(status().isNoContent());

        verify(questionConfigService, times(1)).deleteQuestionConfig(idToDelete);
    }

    @Test
    void deleteQuestionConfigShouldReturn404WhenNotExists() throws Exception {
        // Given
        Long idToDelete = 999L;
        doThrow(new QuestionConfigExceptions.QuestionConfigNotFoundException(idToDelete))
            .when(questionConfigService).deleteQuestionConfig(idToDelete);

        // When & Then
        mockMvc.perform(delete("/question_configs/{id}", idToDelete))
            .andExpect(status().isNotFound());

        verify(questionConfigService, times(1)).deleteQuestionConfig(idToDelete);
    }
}
