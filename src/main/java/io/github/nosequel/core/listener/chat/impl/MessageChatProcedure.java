package io.github.nosequel.core.listener.chat.impl;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.listener.chat.ChatProcedure;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.player.data.PlayerCosmeticData;
import io.github.nosequel.core.player.data.PlayerGrantData;
import io.github.nosequel.core.rank.Rank;
import io.github.nosequel.core.util.MessageConstants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageChatProcedure implements ChatProcedure {

    private final CorePlayerController playerController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);

    @Override
    public boolean handle(Player player, String message) {
        final CorePlayer profile = playerController.find(player);
        final PlayerGrantData grantData = profile.findData(PlayerGrantData.class);
        final PlayerCosmeticData cosmeticData = profile.findData(PlayerCosmeticData.class);
        final Rank rank = grantData.getGrant().getRank();

        if (rank == null) {
            return false;
        }

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', MessageConstants.CHAT_FORMAT
                .replace("%rank_prefix%", rank.getGeneralRankData().getPrefix())
                .replace("%rank_suffix%", rank.getGeneralRankData().getSuffix())
                .replace("%player_name%", player.getName())
                .replace("%chat_color%", (cosmeticData.getChatColor() == null ? ChatColor.WHITE.toString() : cosmeticData.getChatColor().toString()))
                .replace("%name_color%", (cosmeticData.getNameColor() == null ? "" : cosmeticData.getNameColor().toString())))
                .replace("%message%", message));

        return true;
    }
}