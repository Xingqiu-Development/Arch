package io.github.nosequel.core.util;

import lombok.Getter;

import java.util.*;

public class SortableArrayList<T> extends LinkedList<T> {

    @Getter
    private final Comparator<T> comparator;

    /**
     * Constructor for making a new SortableArrayList
     *
     * @param comparator the comparator to sort the list with
     */
    public SortableArrayList(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public boolean add(T object) {
        boolean success = super.add(object);
        super.sort(comparator);

        return success;
    }

    @Override
    public boolean remove(Object o) {
        final boolean success = super.removeIf(o::equals);
        this.sort(comparator);

        return success;
    }
}