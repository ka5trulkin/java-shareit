package ru.practicum.shareit.booking.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.StateParam;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

import static ru.practicum.shareit.utils.PatternsApp.BOOKINGS_PREFIX;
import static ru.practicum.shareit.utils.PatternsApp.SHAREIT_SERVER_URL;

@Service
public class BookingClient extends BaseClient {
    @Autowired
    public BookingClient(@Value(SHAREIT_SERVER_URL) String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + BOOKINGS_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addBooking(Long bookerId, BookingDTO bookingDto) {
        return post("", bookerId, bookingDto);
    }

    public ResponseEntity<Object> approvalBooking(Long ownerId, Long bookingId, boolean approved) {
        final Map<String, Object> parameters = Map.of("approved", approved);
        return patch("/" + bookingId + "?approved={approved}", ownerId, parameters);
    }

    public ResponseEntity<Object> getBookingByOwnerOrBooker(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookingsByBooker(Long bookerId, String stateParam, Integer from, Integer size) {
        final StateParam state = StateParam.stateOf(stateParam);
        final Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", bookerId, parameters);
    }

    public ResponseEntity<Object> getBookingsByOwner(Long ownerId, String stateParam, Integer from, Integer size) {
        final StateParam state = StateParam.stateOf(stateParam);
        final Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", ownerId, parameters);
    }
}