package io.github.nosequel.core.util.synchronize.type;

import com.google.gson.JsonObject;

public interface SynchronizationType {

    default void setup() { }

    /**
     * Publish a {@link JsonObject} through the SynchronizationType
     *
     * @param object  the object to synchronize
     */
    void publish(JsonObject object);

    /**
     * Handle an incoming message through the SynchronizationType
     *
     * @param message the message which was sent
     */
    void incoming(String message);

}
