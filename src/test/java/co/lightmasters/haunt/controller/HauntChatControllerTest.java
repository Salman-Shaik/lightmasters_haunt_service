package co.lightmasters.haunt.controller;

import co.lightmasters.haunt.model.Message;
import co.lightmasters.haunt.model.dto.DeleteMessageDto;
import co.lightmasters.haunt.model.dto.MessageDto;
import co.lightmasters.haunt.security.WebSecurityConfig;
import co.lightmasters.haunt.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = {WebSecurityConfig.class, HauntChatController.class})
class HauntChatControllerTest {

    @MockBean
    private ChatService chatService;

    @Autowired
    private MockMvc mockMvc;

    private MessageDto messageDto;
    private DeleteMessageDto deleteMessageDto;

    @BeforeEach
    void setUp() {
        deleteMessageDto = DeleteMessageDto.builder().messageId(1L).chatId(1L).username("test").build();
        messageDto = MessageDto.builder()
                .chatId(1L)
                .message("Message")
                .sentUsername("matched")
                .build();
    }

    @Test
    void addMessage() throws Exception {
        this.mockMvc.perform(post("/v1/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(messageDto.toJson()))
                .andExpect(status().isCreated()).andReturn();
    }

    @Test
    void fetchChatHistory() throws Exception {
        Message message = Message.builder().message("Duh").messageId(1L).senderUsername("hashed").timeOfCreation(new Date()).chatId(1L).build();
        when(chatService.fetchChatHistoryById(1L)).thenReturn(Collections.singletonList(message));
        this.mockMvc.perform(get("/v1/chatHistory").queryParam("chatId", String.valueOf(1L)))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void deleteMessage() throws Exception {
        this.mockMvc.perform(delete("/v1/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(deleteMessageDto.toJson()))
                .andExpect(status().isOk()).andReturn();
    }
}
