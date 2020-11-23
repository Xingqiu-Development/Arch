package io.github.nosequel.core.util;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.experimental.UtilityClass;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@UtilityClass
public class UUIDUtil {

    private final Map<String, UUID> uuidCache = new HashMap<>();

    public UUID getUuidFromHuuid(String string) {
        return UUID.fromString(new StringBuffer(string)
                .insert(8, "-")
                .insert(13, "-")
                .insert(18, "-")
                .insert(23, "-")
                .toString());
    }

    public UUID getUuidOfUsername(String username) {
        if(!uuidCache.containsKey(username)) {
            try {
                final JSONObject object = (JSONObject) (new JSONParser()).parse(Unirest.get("https://api.mojang.com/users/profiles/minecraft/" + username).asString().getBody());
                uuidCache.put(username, getUuidFromHuuid((String) object.get("id")));
            } catch (UnirestException | ParseException e) {
                e.printStackTrace();
            }
        }

        return uuidCache.get(username);
    }

}
