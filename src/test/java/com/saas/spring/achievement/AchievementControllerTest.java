package com.saas.spring.achievement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AchievementController.class)
public class AchievementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itWorks() throws Exception {
        mockMvc.perform(get("/achievements"))
                .andExpect(status().isOk());
    }
}
