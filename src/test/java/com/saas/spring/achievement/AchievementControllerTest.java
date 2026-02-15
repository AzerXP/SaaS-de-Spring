package com.saas.spring.achievement;

import com.saas.spring.achievement.dto.AchievementInDto;
import com.saas.spring.achievement.dto.AchievementOutDto;
import com.saas.spring.achievement.dto.AchievementUpdateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AchievementController.class)
public class AchievementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AchievementService achievementService;

    @Test
    void getAllAchievementsShouldReturnList() throws Exception {

        List<AchievementOutDto> mockList = Arrays.asList(
                new AchievementOutDto(1L, "Logro 1"),
                new AchievementOutDto(2L, "Logro 2")
        );
        when(achievementService.getAllAchievement()).thenReturn(mockList);


        mockMvc.perform(get("/achievements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Logro 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Logro 2")));

        verify(achievementService, times(1)).getAllAchievement();
    }

    @Test
    void getAchievementByIdShouldReturnAchievementWhenExists() throws Exception {

        Long id = 1L;
        AchievementOutDto mockDto = new AchievementOutDto(id, "Logro Test");
        when(achievementService.getById(id)).thenReturn(mockDto);


        mockMvc.perform(get("/achievements/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Logro Test")));

        verify(achievementService, times(1)).getById(id);
    }

    @Test
    void getAchievementByIdShouldReturn404WhenNotExists() throws Exception {

        Long id = 999L;
        when(achievementService.getById(id)).thenThrow(new IllegalArgumentException("No encontrado"));


        mockMvc.perform(get("/achievements/{id}", id))
                .andExpect(status().isNotFound());

        verify(achievementService, times(1)).getById(id);
    }

    @Test
    void createAchievementShouldReturn201WhenValid() throws Exception {

        AchievementInDto inDto = new AchievementInDto("Nuevo Logro");
        AchievementOutDto outDto = new AchievementOutDto(1L, "Nuevo Logro");

        when(achievementService.createAchievement(inDto))
                .thenReturn(outDto);


        mockMvc.perform(post("/achievements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Nuevo Logro")));

        verify(achievementService, times(1)).createAchievement(inDto);
    }

    @Test
    void createAchievementShouldReturn400WhenNameIsTooShort() throws Exception {
        AchievementInDto inDto = new AchievementInDto("ab");

        mockMvc.perform(post("/achievements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isBadRequest());

        verify(achievementService, never()).createAchievement(any());
    }

    @Test
    void createAchievementShouldReturn400WhenNameIsEmpty() throws Exception {

        AchievementInDto inDto = new AchievementInDto("");

        mockMvc.perform(post("/achievements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isBadRequest());

        verify(achievementService, never()).createAchievement(any());
    }

    @Test
    void updateAchievementShouldReturn200WhenValid() throws Exception {

        Long id = 1L;
        AchievementUpdateDto updateDto = new AchievementUpdateDto("Logro Actualizado");
        AchievementOutDto outDto = new AchievementOutDto(id, "Logro Actualizado");

        when(achievementService.updateAchievement(eq(updateDto), eq(id)))
                .thenReturn(outDto);

        mockMvc.perform(patch("/achievements/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Logro Actualizado")));

        verify(achievementService, times(1))
                .updateAchievement(eq(updateDto), eq(id));
    }

    @Test
    void updateAchievementShouldWorkWithNullName() throws Exception {

        Long id = 1L;
        AchievementUpdateDto updateDto = new AchievementUpdateDto(null);
        AchievementOutDto outDto = new AchievementOutDto(id, "Nombre Original");

        when(achievementService.updateAchievement(eq(updateDto), eq(id)))
                .thenReturn(outDto);

        // When & Then
        mockMvc.perform(patch("/achievements/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Nombre Original")));

        verify(achievementService, times(1))
                .updateAchievement(eq(updateDto), eq(id));
    }

    @Test
    void updateAchievementShouldReturn404WhenNotExists() throws Exception {

        Long id = 999L;
        AchievementUpdateDto updateDto = new AchievementUpdateDto("Test");

        when(achievementService.updateAchievement(eq(updateDto), eq(id)))
                .thenThrow(new IllegalArgumentException("No encontrado"));


        mockMvc.perform(patch("/achievements/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());

        verify(achievementService, times(1))
                .updateAchievement(eq(updateDto), eq(id));
    }

    @Test
    void deleteAchievementShouldReturn204WhenExists() throws Exception {

        Long id = 1L;
        doNothing().when(achievementService).deleteAchievement(id);


        mockMvc.perform(delete("/achievements/{id}", id))
                .andExpect(status().isNoContent());

        verify(achievementService, times(1)).deleteAchievement(id);
    }

    @Test
    void deleteAchievementShouldReturn404WhenNotExists() throws Exception {

        Long id = 999L;
        doThrow(new IllegalArgumentException("No encontrado"))
                .when(achievementService).deleteAchievement(id);

        mockMvc.perform(delete("/achievements/{id}", id))
                .andExpect(status().isNotFound());

        verify(achievementService, times(1)).deleteAchievement(id);
    }
}
