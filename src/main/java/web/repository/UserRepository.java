package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.entity.User;


public interface UserRepository extends JpaRepository<User, Integer> {
}
