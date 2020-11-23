package io.github.nosequel.core.commands;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.player.data.PlayerGeneralData;
import io.github.nosequel.core.player.data.PlayerGrantData;
import io.github.nosequel.core.rank.RankController;
import io.github.nosequel.core.util.command.annotation.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListCommand {

    private final CorePlayerController playerController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);
    private final RankController rankController = CorePlugin.getInstance().getHandler().find(RankController.class);

    @Command(label = "list", aliases = {"who", "ewho", "elist"})
    public void execute(CommandSender sender) {
        final List<CorePlayer> players = Bukkit.getOnlinePlayers().stream()
                .map(playerController::find)
                .sorted(Comparator.comparingInt(player -> player.findData(PlayerGrantData.class).getGrant().getRank().getGeneralRankData().getWeight()))
                .collect(Collectors.toList());

        Collections.reverse(players);

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', rankController.getRanks().stream()
                .filter(rank -> sender.hasPermission("core.staff") || !rank.getGeneralRankData().isHidden())
                .map(rank -> (rank.getGeneralRankData().isHidden() ? ChatColor.GRAY + "*" : "") + rank.getGeneralRankData().getDisplayName())
                .collect(Collectors.joining(ChatColor.WHITE + ", "))));

        sender.sendMessage(
                ChatColor.WHITE + "(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ") "
                        + ChatColor.translateAlternateColorCodes('&', players.stream().map(player -> player.findData(PlayerGrantData.class).getGrant().getRank().getGeneralRankData().getColor() + player.findData(PlayerGeneralData.class).getName())
                        .collect(Collectors.joining(ChatColor.WHITE + ", "))
                ));

    }
}