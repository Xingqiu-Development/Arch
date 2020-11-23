package io.github.nosequel.core.commands.punishment;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.commands.sync.MessageSynchronizationHandler;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.data.PlayerGeneralData;
import io.github.nosequel.core.player.data.PlayerPunishmentData;
import io.github.nosequel.core.player.punishments.Punishment;
import io.github.nosequel.core.player.punishments.PunishmentType;
import io.github.nosequel.core.util.DurationUtil;
import io.github.nosequel.core.util.MessageConstants;
import io.github.nosequel.core.util.command.annotation.Command;
import io.github.nosequel.core.util.command.annotation.Parameter;
import io.github.nosequel.core.util.json.JsonAppender;
import io.github.nosequel.core.util.synchronize.SynchronizeController;
import org.bukkit.command.CommandSender;

public class MuteCommand {

    @Command(label = "mute", aliases = "tempmute", permission = "core.mute")
    public void mute(CommandSender sender, CorePlayer target, @Parameter(name = "duration", value = "permanent") String duration, @Parameter(name = "reason", value = "No reason provided") String reason) {
        final PlayerPunishmentData data = target.findData(PlayerPunishmentData.class);
        final PlayerGeneralData playerData = target.findData(PlayerGeneralData.class);

        long durationLong;

        if (duration.equalsIgnoreCase("permanent") || duration.equalsIgnoreCase("perm")) {
            durationLong = -1L;
        } else {
            try {
                durationLong = DurationUtil.parseTime(duration);
            } catch (Exception exception) {
                durationLong = -1L;
            }
        }

        data.getPunishments().add(new Punishment(null, PunishmentType.MUTE, durationLong, reason.replace("_", " "), sender.getName()));
        target.sync();

        CorePlugin.getInstance().getHandler().find(SynchronizeController.class).synchronize(
                MessageSynchronizationHandler.class,
                new JsonAppender()
                        .append("message", MessageConstants.PUNISHMENT_MUTE_BROADCAST
                                .replace("%muted%", playerData.getName())
                                .replace("%issuer%", sender.getName())).get()
        );
    }

    @Command(label = "unmute", permission = "core.unmute")
    public void unmute(CommandSender sender, CorePlayer target) {
        final PlayerPunishmentData data = target.findData(PlayerPunishmentData.class);
        final PlayerGeneralData playerData = target.findData(PlayerGeneralData.class);

        data.getActivePunishments(PunishmentType.MUTE).stream()
                .filter(Punishment::isActive)
                .forEach(punishment -> punishment.setActive(false));

        target.sync();

        CorePlugin.getInstance().getHandler().find(SynchronizeController.class).synchronize(
                MessageSynchronizationHandler.class,
                new JsonAppender()
                        .append("message", MessageConstants.PUNISHMENT_UNMUTE_BROADCAST
                                .replace("%unmuted%", playerData.getName())
                                .replace("%issuer%", sender.getName())).get()
        );
    }
    
}
