package io.github.nosequel.core.listener.chat.impl;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.listener.chat.ChatProcedure;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.player.data.PlayerPunishmentData;
import io.github.nosequel.core.player.punishments.Punishment;
import io.github.nosequel.core.player.punishments.PunishmentType;
import io.github.nosequel.core.util.MessageConstants;
import org.bukkit.entity.Player;

public class MuteChatProcedure implements ChatProcedure {

    private final CorePlayerController playerController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);

    @Override
    public boolean handle(Player player, String message) {
        final CorePlayer profile = playerController.find(player);
        final PlayerPunishmentData punishmentData = profile.findData(PlayerPunishmentData.class);
        final Punishment mute = punishmentData.getActivePunishments(PunishmentType.MUTE).stream().findFirst().orElse(null);

        if(mute != null) {
            player.sendMessage(MessageConstants.PUNISHMENT_MESSAGE_CANCEL);
            return false;
        }

        return true;
    }
}
