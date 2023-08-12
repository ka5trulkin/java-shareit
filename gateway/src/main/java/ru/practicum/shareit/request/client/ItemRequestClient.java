package ru.practicum.shareit.request.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestCreate;

import java.util.Map;

import static ru.practicum.shareit.utils.PatternsApp.ITEM_REQUEST_PREFIX;
import static ru.practicum.shareit.utils.PatternsApp.SHAREIT_SERVER_URL;

@Service
public class ItemRequestClient extends BaseClient {
    @Autowired
    public ItemRequestClient(@Value(SHAREIT_SERVER_URL) String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + ITEM_REQUEST_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addRequest(Long ownerId, ItemRequestCreate request) {
        return post("", ownerId, request);
    }

    public ResponseEntity<Object> getAllToOwner(Long ownerId) {
        return get("", ownerId);
    }

    public ResponseEntity<Object> getAll(Long userId, Integer from, Integer size) {
        final Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getRequest(Long userId, Long requestId) {
        return get("/" + requestId, userId);
    }
}