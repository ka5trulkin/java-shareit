package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByIdAndOwnerId(Long id, Long ownerId);

    List<ItemDTO> findAllByOwnerId(Long ownerId);

    List<ItemDTO> findByNameOrDescriptionContainsIgnoreCaseAndAvailableTrue(String name, String description);

    default List<ItemDTO> findByNameOrDescription(String name, String description) {
        return findByNameOrDescriptionContainsIgnoreCaseAndAvailableTrue(name, description);
    }
}