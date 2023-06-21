package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.UserNotFoundExistException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.exception.UserEmailAlreadyExistException;

import java.util.*;

@Repository
public class UserRepositoryInMemory implements UserRepository {
    private long id;
    private final Map<Long, User> userStorage = new HashMap<>();
    private final Set<String> emailsList = new HashSet<>();

    private void setUserId(User user) {
        user.setId(++id);
    }

    private void checkUserEmailDuplicate(User user) {
        if (!emailsList.add(user.getEmail())) {
            throw new UserEmailAlreadyExistException(user.getEmail());
        }
    }

    private void checkUserExist(long id) {
        if (!userStorage.containsKey(id)) {
            throw new UserNotFoundExistException(id);
        }
    }

    private void updateEmailsList(User user) {
        String userEmailInStorage = userStorage.get(user.getId()).getEmail();
        if (!userEmailInStorage.equals(user.getEmail())) {
            this.checkUserEmailDuplicate(user);
            emailsList.remove(userEmailInStorage);
        }
    }

    @Override
    public void addUser(User user) {
        this.checkUserEmailDuplicate(user);
        this.setUserId(user);
        userStorage.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        if (userStorage.containsKey(user.getId())) {
            this.updateEmailsList(user);
            userStorage.put(user.getId(), user);
        } else throw new UserNotFoundExistException(user.getId());
    }

    @Override
    public void deleteUser(long id) {
        this.checkUserExist(id);
        String userEmail = userStorage.get(id).getEmail();
        userStorage.remove(id);
        emailsList.remove(userEmail);
    }

    @Override
    public User getUser(long id) {
        this.checkUserExist(id);
        return userStorage.get(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return Collections.unmodifiableCollection(userStorage.values());
    }
}