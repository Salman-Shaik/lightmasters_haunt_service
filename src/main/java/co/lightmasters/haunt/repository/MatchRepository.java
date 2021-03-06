package co.lightmasters.haunt.repository;

import co.lightmasters.haunt.model.Match;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MatchRepository extends CrudRepository<Match, Long> {
    List<Match> findByUsername(String username);

    void deleteByUsernameAndSwipedUsername(String username, String swipedUsername);
}
