package ru.practicum.shareit.item.dto.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDTOMapper;
import ru.practicum.shareit.booking.dto.BookingInfoDTO;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTOMapper;
import ru.practicum.shareit.item.dto.comment.CommentView;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
public class ItemDTOMapper {
    private BookingInfoDTO getBookingOrNull(BookingView view) {
        return view != null ? BookingDTOMapper.fromViewToInfo(view) : null;
    }

    public Item toItem(long ownerId, ItemDTO itemDTO) {
        User owner = User.builder().id(ownerId).build();
        ItemRequest request =
                itemDTO.getRequestId() != null
                        ? ItemRequest.builder().id(itemDTO.getRequestId()).build()
                        : null;
        return Item.builder()
                .owner(owner)
                .name(itemDTO.getName())
                .description(itemDTO.getDescription())
                .available(itemDTO.getAvailable())
                .request(request)
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
                .ownerId(item.getIdOwner())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .requestId(item.getRequestsId())
                .build();
    }

    public ItemOut fromBookingView(ItemDTO itemDTO, List<CommentDTO> comments) {
        return new ItemOut(
                itemDTO.getId(),
                itemDTO.getOwnerId(),
                itemDTO.getName(),
                itemDTO.getDescription(),
                itemDTO.getAvailable(),
                itemDTO.getRequestId(),
                null,
                null,
                comments
        );
    }

    public ItemOut fromBookingView(ItemDTO itemDTO, BookingView lastView, BookingView nextView,
                                   List<CommentDTO> comments) {
        return new ItemOut(
                itemDTO.getId(),
                itemDTO.getOwnerId(),
                itemDTO.getName(),
                itemDTO.getDescription(),
                itemDTO.getAvailable(),
                itemDTO.getRequestId(),
                getBookingOrNull(lastView),
                getBookingOrNull(nextView),
                comments
        );
    }

    public List<ItemOut> fromBookingViewCollection(Collection<ItemDTO> itemDTO,
                                                   Collection<BookingView> bookings,
                                                   Collection<CommentView> comments) {
        final LocalDateTime time = LocalDateTime.now();
        return itemDTO.stream()
                .map(item -> fromBookingView(
                        item,
                        bookings.stream()
                                .filter(view -> view.getEnd().isBefore(time))
                                .filter(last -> Objects.equals(item.getId(), last.getItem().getId()))
                                .min(Comparator.comparing(view -> view.getEnd() == null ? null : view.getEnd(),
                                        Comparator.nullsLast(Comparator.naturalOrder())))
                                .orElse(null),
                        bookings.stream()
                                .filter(view -> view.getStart().isAfter(time))
                                .filter(next -> Objects.equals(item.getId(), next.getItem().getId()))
                                .min(Comparator.comparing(view -> view.getStart() == null ? null : view.getStart(),
                                        Comparator.nullsLast(Comparator.naturalOrder())))
                                .orElse(null),
                        CommentDTOMapper.fromViewList(
                                comments.stream()
                                        .filter(view -> Objects.equals(item.getId(), view.getItem().getId()))
                                        .collect(Collectors.toList()))
                ))
                .sorted(Comparator.comparing(
                        item -> item.getNextBooking() == null ? null : item.getNextBooking().getStart(),
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }
}