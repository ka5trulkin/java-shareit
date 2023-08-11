package ru.practicum.shareit.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageApp extends PageRequest {
    protected PageApp(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public static PageRequest ofStartingIndex(int from, int size) {
        return PageRequest.of((from / size), size);
    }
}