package co.lightmasters.haunt.controller;

import co.lightmasters.haunt.model.Message;
import co.lightmasters.haunt.model.dto.DeleteMessageDto;
import co.lightmasters.haunt.model.dto.MessageDto;
import co.lightmasters.haunt.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class HauntChatController {

    private final ChatService chatService;

    @GetMapping(path = "/chatHistory")
    public ResponseEntity<List<Message>> fetchChatHistory(@RequestParam @Valid Long chatId) {
        List<Message> messages = chatService.fetchChatHistoryById(chatId);
        return ResponseEntity.ok().body(messages);
    }

    @PostMapping(path = "/message")
    public ResponseEntity<Message> addMessage(@RequestBody @Valid MessageDto messageDto) {
        Message message = chatService.addMessage(messageDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @DeleteMapping(path = "/message")
    public ResponseEntity<String> deleteMessage(@RequestBody @Valid DeleteMessageDto deleteMessageDto) {
        chatService.deleteMessageById(deleteMessageDto);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }
}
