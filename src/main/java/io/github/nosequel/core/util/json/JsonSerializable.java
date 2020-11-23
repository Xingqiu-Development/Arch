package io.github.nosequel.core.util.json;

import com.google.gson.JsonObject;

public abstract class JsonSerializable {

    /**
     * Constructor for loading a serialized JsonObject to an object
     *
     * @param object the serialized JsonObject
     */
    public JsonSerializable(JsonObject object) { }

    /**
     * Serialize an object to a JsonObject
     *
     * @return the serialized JsonObject
     */
    public abstract JsonObject toJsonObject();


}