package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    <T> Optional<T> findById(Long id, Class<T> type);

    <T> Optional<T> findByIdAndOwnerId(Long id, Long ownerId, Class<T> type);

    <T> List<T> findAllByOwnerId(Long ownerId, Class<T> type);

    <T> List<T> findByNameOrDescriptionContainsIgnoreCaseAndAvailableTrue(String name, String description, Class<T> type);

    default <T> List<T> findByNameOrDescription(String name, String description, Class<T> type) {
        return findByNameOrDescriptionContainsIgnoreCaseAndAvailableTrue(name, description, type);
    }
}