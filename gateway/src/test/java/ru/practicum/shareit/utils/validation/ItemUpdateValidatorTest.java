package ru.practicum.shareit.utils.validation;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.item.dto.ItemDTO;

import static org.junit.jupiter.api.Assertions.*;

class ItemUpdateValidatorTest extends AbstractTest {
    private final ItemUpdateValidator validator = new ItemUpdateValidator();

    @Test
    void isValid_whenNameIsNotNull_thenReturnTrue() {
        final ItemDTO itemDTO = getItemDto();
        itemDTO.setDescription(null);
        itemDTO.setAvailable(null);

        assertTrue(validator.isValid(itemDTO, null));
    }

    @Test
    void isValid_whenDescriptionIsNotNull_thenReturnTrue() {
        final ItemDTO itemDTO = getItemDto();
        itemDTO.setName(null);
        itemDTO.setAvailable(null);

        assertTrue(validator.isValid(itemDTO, null));
    }

    @Test
    void isValid_whenAvailableIsNotNull_thenReturnTrue() {
        final ItemDTO itemDTO = getItemDto();
        itemDTO.setName(null);
        itemDTO.setDescription(null);

        assertTrue(validator.isValid(itemDTO, null));
    }

    @Test
    void isValid_whenNameAndDescriptionAndAvailableIsNull_thenReturnFalse() {
        final ItemDTO itemDTO = getItemDto();
        itemDTO.setName(null);
        itemDTO.setDescription(null);
        itemDTO.setAvailable(null);

        assertFalse(validator.isValid(itemDTO, null));
    }
}