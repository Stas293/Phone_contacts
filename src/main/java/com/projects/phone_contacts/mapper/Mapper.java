package com.projects.phone_contacts.mapper;

import java.util.List;
import java.util.Set;

public interface Mapper<F, T> {
    T map(F from);

    default T map(F from, T to) {
        return to;
    }

    default List<T> map(List<F> from) {
        return from.parallelStream()
                .map(this::map)
                .toList();
    }

    default List<T> map(Set<F> from) {
        return from.parallelStream()
                .map(this::map)
                .toList();
    }
}
