package co.lightmasters.haunt.repository;

import co.lightmasters.haunt.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
