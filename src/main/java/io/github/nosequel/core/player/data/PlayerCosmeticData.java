package io.github.nosequel.core.player.data;

import com.google.gson.JsonObject;
import io.github.nosequel.core.util.data.impl.SaveableData;
import io.github.nosequel.core.util.json.JsonAppender;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

@Getter
@Setter
public class PlayerCosmeticData implements SaveableData {

    private ChatColor chatColor = ChatColor.WHITE;
    private ChatColor nameColor = ChatColor.WHITE;

    public PlayerCosmeticData() { }

    /**
     * Constructor for loading a PlayerCosmeticData from a {@link JsonObject}
     *
     * @param object the {@link JsonObject}
     */
    public PlayerCosmeticData(JsonObject object) {
        this.chatColor = this.getColor(object, "chatColor");
        this.nameColor = this.getColor(object, "nameColor");
    }

    @Override
    public String getSavePath() {
        return "cosmetic";
    }

    @Override
    public JsonObject toJson() {
        return new JsonAppender()
                .append("chatColor", chatColor == null ? "none" : chatColor.name())
                .append("nameColor", nameColor == null ? "none" : nameColor.name())
                .get();
    }

    private ChatColor getColor(JsonObject object, String field) {
        return object.get(field).getAsString().equals("none") ? null : ChatColor.valueOf(object.get(field).getAsString());
    }

}
