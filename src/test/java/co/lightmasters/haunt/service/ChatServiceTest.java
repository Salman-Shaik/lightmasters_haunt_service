package co.lightmasters.haunt.service;

import co.lightmasters.haunt.model.Chat;
import co.lightmasters.haunt.model.Message;
import co.lightmasters.haunt.model.dto.ChatDto;
import co.lightmasters.haunt.model.dto.MessageDto;
import co.lightmasters.haunt.repository.ChatRepository;
import co.lightmasters.haunt.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChatServiceTest {
    private ChatRepository chatRepository;
    private MessageRepository messageRepository;
    private ChatService chatService;

    private ChatDto chatDto;
    private Chat chat;

    @BeforeEach
    void setUp() {
        chatRepository = mock(ChatRepository.class);
        messageRepository = mock(MessageRepository.class);
        chatService = new ChatService(chatRepository, messageRepository);

        chatDto = ChatDto.builder().username("test").matchedUsername("matched").build();
        chat = Chat.from(chatDto);
    }

    @Test
    void createChat() {
        when(chatRepository.save(chat)).thenReturn(chat);
        Chat chat = chatService.createChat(chatDto);
        assertEquals(chat.getUsername(), this.chat.getUsername());
        assertEquals(chat.getMatchedUsername(), this.chat.getMatchedUsername());
    }
}
