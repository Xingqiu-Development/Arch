package io.github.nosequel.core.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class JsonUtils {

    @Getter
    private final JsonParser parser = new JsonParser();

    /**
     * Get a {@link JsonObject} from a {@link String}
     *
     * @param string the string to get the json object from
     * @return the parsed JsonObject
     */
    public JsonObject getJsonFromString(String string) {
        if (string.isEmpty()) {
            throw new IllegalArgumentException("String could not be parsed because it's empty");
        }

        return parser.parse(string).getAsJsonObject();
    }

    public JsonObject getFromMap(Map<String, String> map) {
        final JsonObject object = new JsonObject();
        map.forEach(object::addProperty);

        return object;
    }

    /**
     * Get a {@link Map} from a {@link JsonObject}
     *
     * @param object the {@link JsonObject} to get the {@link Map} from
     * @return the {@link Map}
     */
    public Map<String, JsonElement> getMap(JsonObject object) {
        return new HashMap<String, JsonElement>() {{
            object.entrySet().forEach(entry -> put(entry.getKey(), entry.getValue()));
        }};
    }
}