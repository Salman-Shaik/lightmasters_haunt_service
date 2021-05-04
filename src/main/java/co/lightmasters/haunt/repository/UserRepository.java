package co.lightmasters.haunt.repository;

import co.lightmasters.haunt.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
    List<User> findByCity(String userCity);

    List<User> findByCityAndGender(String userCity, String genderChoice);
}
