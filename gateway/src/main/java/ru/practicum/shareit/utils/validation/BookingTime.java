package ru.practicum.shareit.utils.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static ru.practicum.shareit.exception.base.ExceptionMessage.BAD_BOOKING_TIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = BookingTimeValidator.class)
@Documented
public @interface BookingTime {
    String message() default BAD_BOOKING_TIME;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}