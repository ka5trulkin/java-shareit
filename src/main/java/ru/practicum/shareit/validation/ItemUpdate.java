package ru.practicum.shareit.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static ru.practicum.shareit.exception.base.ExceptionMessage.BAD_ITEM_UPDATE;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ItemUpdateValidator.class)
@Documented
public @interface ItemUpdate {
    String message() default BAD_ITEM_UPDATE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}