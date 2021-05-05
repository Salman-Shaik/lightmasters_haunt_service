package co.lightmasters.haunt.repository;

import co.lightmasters.haunt.model.Match;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MatchRepository extends CrudRepository<Match, Integer> {
    List<Match> findByUsername(String username);
}
