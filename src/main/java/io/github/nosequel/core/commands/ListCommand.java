package io.github.nosequel.core.commands;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.player.data.PlayerGeneralData;
import io.github.nosequel.core.player.data.PlayerGrantData;
import io.github.nosequel.core.rank.RankController;
import io.github.nosequel.core.util.command.annotation.Command;
import io.github.nosequel.core.util.command.annotation.Subcommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.stream.Collectors;

public class ListCommand {

    private final CorePlayerController playerController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);
    private final RankController rankController = CorePlugin.getInstance().getHandler().find(RankController.class);

    @Command(label = "list", aliases = {"who", "ewho", "elist"})
    public void execute(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', rankController.getRanks().stream()
                .filter(rank -> !rank.getGeneralRankData().isHidden())
                .map(rank -> rank.getGeneralRankData().getDisplayName())
                .collect(Collectors.joining(ChatColor.WHITE + ", "))));

        sender.sendMessage(
                ChatColor.WHITE + "(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ") "
                        + ChatColor.translateAlternateColorCodes('&', playerController.getOnlineSortedPlayers().stream().map(player -> player.findData(PlayerGrantData.class).getGrant().getRank().getGeneralRankData().getColor() + player.findData(PlayerGeneralData.class).getName())
                        .collect(Collectors.joining(ChatColor.WHITE + ", "))
                ));

    }

    @Subcommand(label = "all", parentLabel = "list", permission = "core.staff")
    public void listAll(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', rankController.getRanks().stream()
                .map(rank -> (rank.getGeneralRankData().isHidden() ? ChatColor.GRAY + "*" : "") + rank.getGeneralRankData().getDisplayName())
                .collect(Collectors.joining(ChatColor.WHITE + ", "))));

        sender.sendMessage(
                ChatColor.WHITE + "(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ") "
                        + ChatColor.translateAlternateColorCodes('&', playerController.getOnlineSortedPlayers().stream().map(player -> player.findData(PlayerGrantData.class).getGrant().getRank().getGeneralRankData().getColor() + player.findData(PlayerGeneralData.class).getName())
                        .collect(Collectors.joining(ChatColor.WHITE + ", "))
                ));
    }
}