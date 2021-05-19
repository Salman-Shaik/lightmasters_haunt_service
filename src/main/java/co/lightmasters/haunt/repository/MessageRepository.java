package co.lightmasters.haunt.repository;


import co.lightmasters.haunt.model.Message;
import org.springframework.data.repository.CrudRepository;

import javax.validation.Valid;
import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findByChatId(@Valid Long chatId);

    boolean existsByMessageIdAndChatId(Long messageId, Long chatId);
}
