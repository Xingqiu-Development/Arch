package io.github.nosequel.core.player.grant.task;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.player.grant.Grant;
import io.github.nosequel.core.player.grant.GrantController;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.player.data.PlayerGrantData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class GrantCheckTask extends BukkitRunnable {

    private final GrantController grantController = CorePlugin.getInstance().getHandler().find(GrantController.class);
    private final CorePlayerController playerController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);

    public GrantCheckTask() {
        this.runTaskTimer(CorePlugin.getInstance(), 0L, 40L);
    }

    @Override
    public void run() {
        playerController.getPlayers().stream()
                .map(player -> player.findData(PlayerGrantData.class))
                .filter(Objects::nonNull)
                .forEach(grantData -> grantData.getGrants().forEach(Grant::isActive));

        grantController.refreshGrants();
    }
}