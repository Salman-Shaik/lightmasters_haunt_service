package co.lightmasters.haunt.repository;

import co.lightmasters.haunt.model.UserProfile;
import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository extends CrudRepository<UserProfile, String> {
}
