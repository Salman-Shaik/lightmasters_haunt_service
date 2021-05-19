package co.lightmasters.haunt.service;

import co.lightmasters.haunt.model.Chat;
import co.lightmasters.haunt.model.dto.ChatDto;
import co.lightmasters.haunt.model.Message;
import co.lightmasters.haunt.model.dto.DeleteMessageDto;
import co.lightmasters.haunt.model.dto.MessageDto;
import co.lightmasters.haunt.repository.ChatRepository;
import co.lightmasters.haunt.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;

@Component
@AllArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    public Chat createChat(ChatDto chatDto) {
        Chat chat = Chat.from(chatDto);
        Chat existingChat = chatRepository.findByUsernameAndMatchedUsername(chat.getUsername(),chat.getMatchedUsername());
        if (existingChat != null) {
            return chat;
        }
        return chatRepository.save(chat);
    }

    public Message addMessage(MessageDto messageDto) {
        Message message = Message.from(messageDto);
        return messageRepository.save(message);
    }

    public List<Message> fetchChatHistoryById(@Valid Long chatId) {
        return messageRepository.findByChatId(chatId);
    }

    public void deleteMessageById(@Valid DeleteMessageDto deleteMessageDto) {
        Long chatId = deleteMessageDto.getChatId();
        Long messageId = deleteMessageDto.getMessageId();
        boolean chatExists = chatRepository.existsByChatIdAndUsername(chatId,deleteMessageDto.getUsername());
        boolean messageExists = messageRepository.existsByMessageIdAndChatId(messageId, chatId);
        if (chatExists && messageExists) {
            messageRepository.deleteById(messageId);
        }
    }

    public void deleteChat(String username, String swipedUsername) {
        chatRepository.deleteByUsernameAndMatchedUsername(username, swipedUsername);
    }
}
