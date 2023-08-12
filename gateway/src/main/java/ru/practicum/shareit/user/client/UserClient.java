package ru.practicum.shareit.user.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDTO;

import static ru.practicum.shareit.utils.PatternsApp.SHAREIT_SERVER_URL;
import static ru.practicum.shareit.utils.PatternsApp.USERS_PREFIX;

@Service
public class UserClient extends BaseClient {
    @Autowired
    public UserClient(@Value(SHAREIT_SERVER_URL) String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + USERS_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addUser(UserDTO userDTO) {
        return post("", userDTO);
    }

    public ResponseEntity<Object> updateUser(Long id, UserDTO userDTO) {
        return patch("/" + id, userDTO);
    }

    public ResponseEntity<Object> getUser(Long id) {
        return get("/" + id);
    }

    public ResponseEntity<Object> deleteUser(Long id) {
        return delete("/" + id);
    }

    public ResponseEntity<Object> getAllUsers() {
        return get("");
    }
}