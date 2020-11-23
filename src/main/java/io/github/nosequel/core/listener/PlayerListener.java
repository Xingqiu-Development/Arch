package io.github.nosequel.core.listener;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.commands.sync.MessageSynchronizationHandler;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.player.data.PlayerGrantData;
import io.github.nosequel.core.player.data.PlayerPunishmentData;
import io.github.nosequel.core.player.data.PlayerServerData;
import io.github.nosequel.core.player.punishments.Punishment;
import io.github.nosequel.core.player.punishments.PunishmentType;
import io.github.nosequel.core.util.MessageConstants;
import io.github.nosequel.core.util.json.JsonAppender;
import io.github.nosequel.core.util.synchronize.SynchronizeController;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final CorePlayerController profileController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);
    private final SynchronizeController synchronizeController = CorePlugin.getInstance().getHandler().find(SynchronizeController.class);

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        final CorePlayer profile = profileController.find(player);
        final PlayerPunishmentData punishmentData = profile.findData(PlayerPunishmentData.class);

        profile.reloadPermissions();
        punishmentData.getActivePunishments(PunishmentType.BAN).stream()
                .findFirst()
                .ifPresent(banPunishment -> event.disallow(PlayerLoginEvent.Result.KICK_BANNED, MessageConstants.PUNISHMENT_BAN_KICKED));

        if (!profile.hasData(PlayerServerData.class)) {
            profile.addData(new PlayerServerData());
        }

        final PlayerServerData serverData = profile.findData(PlayerServerData.class);
        serverData.setServer(MessageConstants.SERVER);

        if (player.hasPermission("core.staff")) {
            synchronizeController.synchronize(
                    MessageSynchronizationHandler.class,
                    new JsonAppender()
                            .append("message", MessageConstants.STAFF_SERVER_JOIN
                                    .replace("%server%", serverData.getServer())
                                    .replace("%player%", profile.getDisplayName()))
                            .append("permission", "core.staff")
                            .get()
            );
        }


        profile.sync();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final CorePlayer profile = profileController.find(player);

        if (!profile.hasData(PlayerServerData.class)) {
            profile.addData(new PlayerServerData());
        }
        final PlayerServerData serverData = profile.findData(PlayerServerData.class);

        if (player.hasPermission("core.staff")) {
            synchronizeController.synchronize(
                    MessageSynchronizationHandler.class,
                    new JsonAppender()
                            .append("message", MessageConstants.STAFF_SERVER_LEFT
                                    .replace("%server%", serverData.getServer())
                                    .replace("%player%", profile.getDisplayName()))
                            .append("permission", "core.staff")
                            .get()
            );
        }

        serverData.setServer("Offline");
        profile.sync();
    }
}