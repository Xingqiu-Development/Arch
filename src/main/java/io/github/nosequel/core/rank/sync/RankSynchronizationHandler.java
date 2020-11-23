package io.github.nosequel.core.rank.sync;

import com.google.gson.JsonObject;
import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.rank.Rank;
import io.github.nosequel.core.rank.RankController;
import io.github.nosequel.core.util.synchronize.SynchronizationHandler;

import java.util.UUID;

public class RankSynchronizationHandler implements SynchronizationHandler {

    private final RankController rankController = CorePlugin.getInstance().getHandler().find(RankController.class);

    @Override
    public String getType() {
        return "rank-sync";
    }

    @Override
    public void handle(JsonObject object) {
        final UUID uuid = UUID.fromString(object.get("uuid").getAsString());
        final Rank rank = rankController.findRank(uuid);

        rankController.reload(rank);
    }
}
