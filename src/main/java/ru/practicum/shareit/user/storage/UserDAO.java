package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserDAO {
    void addUser(User user);

    void updateUser(User user);

    void deleteUser(long id);

    User getUser(long id);

    Collection<User> getAllUsers();
}