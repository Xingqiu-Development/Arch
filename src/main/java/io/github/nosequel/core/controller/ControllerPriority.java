package io.github.nosequel.core.controller;

import lombok.Getter;

@Getter
public enum ControllerPriority {

    HIGH(3),
    NORMAL(2),
    LOW(1),
    LOWEST(0);

    private final int priority;

    /**
     * Constructor for creating a new ControllerPriority
     * This provides the ordinal priority
     *
     * @param priority the ordinal priority
     */
    ControllerPriority(int priority) {
        this.priority = priority;
    }
}
