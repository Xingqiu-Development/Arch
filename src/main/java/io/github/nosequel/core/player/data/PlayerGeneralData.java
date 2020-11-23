package io.github.nosequel.core.player.data;

import com.google.gson.JsonObject;
import io.github.nosequel.core.util.data.impl.SaveableData;
import io.github.nosequel.core.util.json.JsonAppender;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PlayerGeneralData implements SaveableData {

    public final String savePath = "general";

    @Setter
    private String name;

    /**
     * Constructor for making a new empty GeneralPlayerData object
     */
    public PlayerGeneralData() { this(""); }

    /**
     * Constructor for creating a new GeneralPlayerData object
     *
     * @param name the name of the player
     */
    public PlayerGeneralData(String name) {
        this.name = name;
    }

    /**
     * Constructor for loading a new GeneralPlayerData object
     *
     * @param object the JsonObject to load it from
     */
    public PlayerGeneralData(JsonObject object) {
        this.name = object.get("name").getAsString();
    }

    @Override
    public JsonObject toJson() {
        return new JsonAppender().append("name", this.name).get();
    }
}
