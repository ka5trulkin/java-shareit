package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDTOMapper;
import ru.practicum.shareit.booking.dto.BookingInfoDTO;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
public class ItemDTOMapper {
    private BookingInfoDTO getBookingOrNull(BookingView view) {
        return view != null ? BookingDTOMapper.fromViewToInfo(view) : null;
    }

    public Item toItem(long ownerId, ItemDTO itemDTO) {
        User owner = User.builder().id(ownerId).build();
        return Item.builder()
                .owner(owner)
                .name(itemDTO.getName())
                .description(itemDTO.getDescription())
                .available(itemDTO.getAvailable())
                .build();
    }

    public Item toItemWhenUpdate(Item item, ItemDTO itemDTO) {
        if (itemDTO.getName() != null) {
            item.setName(itemDTO.getName());
        }
        if (itemDTO.getDescription() != null) {
            item.setDescription(itemDTO.getDescription());
        }
        if (itemDTO.getAvailable() != null) {
            item.setAvailable(itemDTO.getAvailable());
        }
        return item;
    }

    public ItemDTO fromItem(Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .ownerId(item.getOwner().getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }

    public Collection<ItemDTO> fromItemCollection(Collection<Item> items) {
        return items.stream()
                .map(ItemDTOMapper::fromItem)
                .collect(Collectors.toList());
    }

    public ItemWithBookingInfoDTO fromBookingView(ItemDTO itemDTO, Collection<CommentDTO> comments) {
        return new ItemWithBookingInfoDTO(
                itemDTO.getId(),
                itemDTO.getOwnerId(),
                itemDTO.getName(),
                itemDTO.getDescription(),
                itemDTO.getAvailable(),
                null,
                null,
                comments
        );
    }

    public ItemWithBookingInfoDTO fromBookingView(ItemDTO itemDTO, BookingView lastView, BookingView nextView) {
        return new ItemWithBookingInfoDTO(
                itemDTO.getId(),
                itemDTO.getOwnerId(),
                itemDTO.getName(),
                itemDTO.getDescription(),
                itemDTO.getAvailable(),
                getBookingOrNull(lastView),
                getBookingOrNull(nextView)
        );
    }

    public ItemWithBookingInfoDTO fromBookingView(ItemDTO itemDTO, BookingView lastView, BookingView nextView,
                                                  Collection<CommentDTO> comments) {
        return new ItemWithBookingInfoDTO(
                itemDTO.getId(),
                itemDTO.getOwnerId(),
                itemDTO.getName(),
                itemDTO.getDescription(),
                itemDTO.getAvailable(),
                getBookingOrNull(lastView),
                getBookingOrNull(nextView),
                comments
        );
    }

    public Collection<ItemWithBookingInfoDTO> fromBookingViewCollection(Collection<ItemDTO> itemDTO,
                                                                        Collection<BookingView> views) {
        final LocalDateTime time = LocalDateTime.now();
        return itemDTO.stream()
                .map(item -> fromBookingView(
                        item,
                        views.stream()
                                .filter(view -> view.getEnd().isBefore(time))
                                .filter(last -> Objects.equals(item.getId(), last.getItem().getId()))
                                .min(Comparator.comparing(view -> view.getEnd() == null ? null : view.getEnd(),
                                        Comparator.nullsLast(Comparator.naturalOrder())))
                                .orElse(null),
                        views.stream()
                                .filter(view -> view.getStart().isAfter(time))
                                .filter(next -> Objects.equals(item.getId(), next.getItem().getId()))
                                .min(Comparator.comparing(view -> view.getStart() == null ? null : view.getStart(),
                                        Comparator.nullsLast(Comparator.naturalOrder())))
                                .orElse(null)
                ))
                .sorted(Comparator.comparing(
                        item -> item.getNextBooking() == null ? null : item.getNextBooking().getStart(),
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }
}