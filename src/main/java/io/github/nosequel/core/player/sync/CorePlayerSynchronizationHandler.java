package io.github.nosequel.core.player.sync;

import com.google.gson.JsonObject;
import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.util.synchronize.SynchronizationHandler;

import java.util.UUID;

public class CorePlayerSynchronizationHandler implements SynchronizationHandler {

    private final CorePlayerController playerController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);

    @Override
    public String getType() {
        return "core-player-sync";
    }

    @Override
    public void handle(JsonObject object) {
        final UUID uuid = UUID.fromString(object.get("uuid").getAsString());
        final CorePlayer player = playerController.find(uuid);

        if(player != null) {
            playerController.reload(player);
        }
    }
}