package web.service;

import web.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();

    User getById(int id);

    User save(User user);

    void remove(int id);
}
