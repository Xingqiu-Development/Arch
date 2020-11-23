package io.github.nosequel.core.controller;

public interface Controller {

    /**
     * Method is called whenever the Controller is supposed to be loaded
     */
    default void load() {
    }

    /**
     * Method is called whenever the Controller is supposed to be unloaded
     */
    default void unload() {
    }

    /**
     * Get the priority of the controller
     *
     * @return the priority
     */
    default ControllerPriority getLoadPriority() {
        return ControllerPriority.NORMAL;
    }

    /**
     * Get the priority of the controller
     *
     * @return the priority
     */
    default ControllerPriority getUnloadPriority() {
        return ControllerPriority.NORMAL;
    }
}
