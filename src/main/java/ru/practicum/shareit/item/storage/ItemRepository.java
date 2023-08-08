package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.dto.item.ItemDTO;
import ru.practicum.shareit.item.dto.item.ItemViewWithRequest;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByIdAndOwnerId(Long id, Long ownerId);

    List<ItemDTO> findByOwnerId(Long id, Pageable pageable);

    List<ItemDTO> findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndAvailableTrue(String name,
                                                                                              String description,
                                                                                              Pageable pageable);

    default List<ItemDTO> findByNameOrDescription(String text, Pageable pageable) {
        return findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndAvailableTrue(text, text, pageable);
    }

    @Query("select i from Item i join fetch i.owner join fetch i.request r join fetch r.owner where i.request.owner.id = :id")
    List<ItemViewWithRequest> findWithRequestsByOwnerId(@Param("id") Long id);

    @Query("select i from Item i join fetch i.owner join fetch i.request r join fetch r.owner where i.request.id in :ids")
    List<ItemViewWithRequest> findByRequestIdIn(@Param("ids") Collection<Long> ids);

    @Query("select i from Item i join fetch i.owner join fetch i.request r join fetch r.owner where i.request.id = :id")
    List<ItemViewWithRequest> findByRequestId(@Param("id") Long id);
}