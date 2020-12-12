package io.github.nosequel.core.listener;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.commands.sync.MessageSynchronizationHandler;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.player.data.PlayerPunishmentData;
import io.github.nosequel.core.player.data.PlayerServerData;
import io.github.nosequel.core.util.MessageConstants;
import io.github.nosequel.core.util.json.JsonAppender;
import io.github.nosequel.core.util.synchronize.SynchronizeController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final CorePlayerController profileController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);
    private final SynchronizeController synchronizeController = CorePlugin.getInstance().getHandler().find(SynchronizeController.class);

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        final CorePlayer profile = profileController.find(player);

        if (profile.hasData(PlayerPunishmentData.class)) {
            profile.findData(PlayerPunishmentData.class).attemptPlayerBan(player);
            profile.reloadPermissions();

            if (profile.hasData(PlayerServerData.class)) {
                profile.findData(PlayerServerData.class).setServer(MessageConstants.SERVER);

                if (player.hasPermission("core.staff")) {
                    synchronizeController.synchronize(
                            MessageSynchronizationHandler.class,
                            new JsonAppender().append("message", MessageConstants.STAFF_SERVER_JOIN.replace("%server%", MessageConstants.SERVER).replace("%player%", profile.getDisplayName())).append("permission", "core.staff").get()
                    );
                }

                profile.sync();
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final CorePlayer profile = profileController.find(player);

        if (player.hasPermission("core.staff")) {
            synchronizeController.synchronize(
                    MessageSynchronizationHandler.class,
                    new JsonAppender().append("message", MessageConstants.STAFF_SERVER_LEFT.replace("%server%", MessageConstants.SERVER).replace("%player%", profile.getDisplayName())).append("permission", "core.staff").get()
            );
        }

        profile.findData(PlayerServerData.class).setServer("Offline");
        profile.sync();
    }
}