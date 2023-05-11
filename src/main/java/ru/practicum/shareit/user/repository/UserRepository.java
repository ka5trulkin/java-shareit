package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {
    void addNewUser(User user);

    void updateUser(User user);

    void deleteUser(long id);

    User getUser(long id);

    Collection<User> getAllUsers();
}