package fr.pixteam.pixcms.service;

import fr.pixteam.pixcms.model.User;
import fr.pixteam.pixcms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Iterable<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User create(User user) {
        user.setDateCreated(Date.from(Instant.now()));

        return this.userRepository.save(user);
    }

    @Override
    public void update(User order) {
        this.userRepository.save(order);
    }
}
