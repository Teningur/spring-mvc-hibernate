package web.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.entity.User;
import web.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repo;

    @Override
    public List<User> getAll() {
        return repo.findAll();
    }

    @Override
    public User getById(int id) {
        Optional<User> optionalUser = repo.findById(id);
        return optionalUser.orElseThrow();
    }
    @Override
    public User save(User user) {
        return repo.saveAndFlush(user);
    }

    @Override
    public void remove(int id) {
        repo.deleteById(id);
    }
}
