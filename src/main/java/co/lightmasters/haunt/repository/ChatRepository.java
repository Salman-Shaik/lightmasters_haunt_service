package co.lightmasters.haunt.repository;


import co.lightmasters.haunt.model.Chat;
import co.lightmasters.haunt.model.Ignore;
import org.springframework.data.repository.CrudRepository;

public interface ChatRepository extends CrudRepository<Chat, Long> {
    Chat findByUsernameAndMatchedUsername(String username, String matchedUsername);

    void deleteByUsernameAndMatchedUsername(String username, String swipedUsername);

    boolean existsByChatIdAndUsername(Long chatId, String username);
}
