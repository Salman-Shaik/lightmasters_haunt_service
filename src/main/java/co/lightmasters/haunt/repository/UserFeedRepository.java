package co.lightmasters.haunt.repository;

import co.lightmasters.haunt.model.UserFeed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFeedRepository extends JpaRepository<UserFeed, String> {
}
