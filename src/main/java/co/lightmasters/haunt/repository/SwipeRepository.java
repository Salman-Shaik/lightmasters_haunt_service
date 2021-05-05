package co.lightmasters.haunt.repository;

import co.lightmasters.haunt.model.Swipe;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SwipeRepository extends CrudRepository<Swipe, Integer> {
    List<Swipe> findByUsername(String username);
}
