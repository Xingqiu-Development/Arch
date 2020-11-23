package io.github.nosequel.core.util.data;

import java.util.List;

public interface DataController<L extends Loadable<?>, D extends Data> {

    /**
     * Get a list of registered data
     *
     * @return the list
     */
    List<? extends D> getRegisteredData();

    /**
     * Load a loadable
     *
     * @param loadable the loadable
     */
    default void load(L loadable) {}

    /**
     * Load all lodables
     */
    default void loadAll() {}

}