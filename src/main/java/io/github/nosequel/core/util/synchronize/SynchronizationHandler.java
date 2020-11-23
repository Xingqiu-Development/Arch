package io.github.nosequel.core.util.synchronize;

import com.google.gson.JsonObject;

public interface SynchronizationHandler {

    /**
     * Get the channel to receive the synchronization handler in
     *
     * @return the channel
     */
    String getType();

    /**
     * Method for handling incoming messages
     *
     * @param object the message
     */
    void handle(JsonObject object);

}