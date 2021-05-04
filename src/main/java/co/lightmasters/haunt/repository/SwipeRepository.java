package co.lightmasters.haunt.repository;

import co.lightmasters.haunt.model.Swipe;
import co.lightmasters.haunt.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SwipeRepository extends CrudRepository<Swipe, String> {
    List<Swipe> findByUsername(String username);
}
