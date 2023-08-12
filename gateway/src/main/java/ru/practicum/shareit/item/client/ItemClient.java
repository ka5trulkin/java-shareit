package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;

import java.util.Map;

import static ru.practicum.shareit.utils.PatternsApp.ITEMS_PREFIX;
import static ru.practicum.shareit.utils.PatternsApp.SHAREIT_SERVER_URL;

@Service
public class ItemClient extends BaseClient {
    @Autowired
    public ItemClient(@Value(SHAREIT_SERVER_URL) String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + ITEMS_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(Long ownerId, ItemDTO itemDTO) {
        return post("", ownerId, itemDTO);
    }

    public ResponseEntity<Object> updateItem(Long id, Long ownerId, ItemDTO itemDTO) {
        return patch("/" + id, ownerId, itemDTO);
    }

    public ResponseEntity<Object> getItemById(Long id, Long userId) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> getItemsByOwner(Long ownerId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", ownerId, parameters);
    }

    public ResponseEntity<Object> getItemsBySearch(String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", null, parameters);
    }

    public ResponseEntity<Object> addComment(Long authorId, Long itemId, CommentDTO commentDTO) {
        return post("/" + itemId + "/comment", authorId, commentDTO);
    }
}