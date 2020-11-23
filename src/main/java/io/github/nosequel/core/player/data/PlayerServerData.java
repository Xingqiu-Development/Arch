package io.github.nosequel.core.player.data;

import com.google.gson.JsonObject;
import io.github.nosequel.core.util.data.impl.SaveableData;
import io.github.nosequel.core.util.json.JsonAppender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerServerData implements SaveableData {

    private String server;

    public PlayerServerData() {
        this.server = "Offline";
    }

    public PlayerServerData(JsonObject object) {
        this.server = object.get("server").getAsString();
    }

    @Override
    public String getSavePath() {
        return "server";
    }

    @Override
    public JsonObject toJson() {
        return new JsonAppender()
                .append("server", this.server).get();
    }
}
