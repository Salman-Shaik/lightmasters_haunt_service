package co.lightmasters.haunt.controller;

import co.lightmasters.haunt.model.Date;
import co.lightmasters.haunt.security.WebSecurityConfig;
import co.lightmasters.haunt.service.DateService;
import co.lightmasters.haunt.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = {WebSecurityConfig.class, HauntDateController.class})
class HauntDateControllerTest {

    @MockBean
    private DateService dateService;

    @Autowired
    private MockMvc mockMvc;

    private Date date;

    @BeforeEach
    void setUp() {
        date = Date.builder()
                .city("City")
                .sexuality("Straight")
                .drinking(true)
                .smoking(false)
                .profession("")
                .prompts(Collections.emptyList())
                .age(21)
                .university("")
                .aboutMe("")
                .name("First Last")
                .build();
    }

    @Test
    void shouldGetDateSuggestionForRespectiveUser() throws Exception {
        when(dateService.getDateSuggestions("test")).thenReturn(Collections.singletonList(date));
        this.mockMvc.perform(get("/v1/dateSuggestions").queryParam("username", "test"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void shouldRespondWithNoContentWhenThereAreNoSuggestionForUser() throws Exception {
        when(dateService.getDateSuggestions("test")).thenReturn(Collections.emptyList());
        this.mockMvc.perform(get("/v1/dateSuggestions").queryParam("username", "test"))
                .andExpect(status().isNoContent()).andReturn();
    }
}
