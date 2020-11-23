package io.github.nosequel.core.controller;

import io.github.nosequel.core.util.JavaUtils;
import lombok.Getter;

import java.util.*;


@Getter
public class ControllerHandler {

    private final Map<Class<? extends Controller>, Controller> controllers = new HashMap<>();

    private final Comparator<Controller> unloadControllerComparator = Comparator.comparingInt(controller -> controller.getUnloadPriority().getPriority());
    private final Comparator<Controller> loadControllerComparator = Comparator.comparingInt(controller -> controller.getLoadPriority().getPriority());

    /**
     * Find a controller by their class
     *
     * @param clazz the class of the controller
     * @param <T>   the type of the controller
     * @return the controller
     */
    public <T> T find(Class<? extends T> clazz) {
        return clazz.cast(this.controllers.get(clazz));
    }

    /**
     * Register a new controller
     * This method automatically sorts all controllers with the comparator
     *
     * @param controller the controller
     */
    public <T extends Controller> void register(T controller) {
        if (this.controllers.get(controller.getClass()) != null) {
            throw new IllegalArgumentException("Controller with class \"" + controller.getClass().getName() + "\" already exists.");
        }

        this.controllers.put(controller.getClass(), controller);
    }

    /**
     * Unregister an already registered controller
     *
     * @param controller the class of the controller
     */
    public void unregister(Class<? extends Controller> controller) {
        if (this.controllers.get(controller) == null) {
            throw new IllegalArgumentException("Controller with class \"" + controller.getName() + "\" is not registered.");
        }

        this.controllers.remove(controller);
    }

    /**
     * Load all registered controllers
     */
    public final void load() {
        JavaUtils.getSortedValues(this.controllers, loadControllerComparator.reversed()).forEach(Controller::load);
    }

    /**
     * Unload all registered controllers
     */
    public final void unload() {
        JavaUtils.getSortedValues(this.controllers, unloadControllerComparator.reversed()).forEach(Controller::unload);
    }
}