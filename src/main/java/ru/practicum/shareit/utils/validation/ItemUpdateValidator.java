package ru.practicum.shareit.utils.validation;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.item.ItemDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
public class ItemUpdateValidator implements ConstraintValidator<ItemUpdate, ItemDTO> {
    @Override
    public boolean isValid(ItemDTO itemDTO, ConstraintValidatorContext constraintValidatorContext) {
        return itemDTO.getName() != null
                || itemDTO.getDescription() != null
                || itemDTO.getAvailable() != null;
    }
}