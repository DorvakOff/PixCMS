package fr.pixteam.pixcms.repository;

import fr.pixteam.pixcms.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {


    User findUserByUsernameLowerCase(String username);

}
