package io.github.nosequel.core.commands;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.rank.Rank;
import io.github.nosequel.core.rank.RankController;
import io.github.nosequel.core.util.MessageConstants;
import io.github.nosequel.core.util.command.annotation.Command;
import io.github.nosequel.core.util.command.annotation.Subcommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RankCommand {

    private final RankController rankController = CorePlugin.getInstance().getHandler().find(RankController.class);

    @Command(label = "rank", permission = "core.rank")
    @Subcommand(parentLabel = "rank", label = "help", permission = "core.rank")
    public void help(CommandSender sender) {
        sender.sendMessage(MessageConstants.RANK_HELP_MESSAGE);
    }

    @Subcommand(parentLabel = "rank", label = "create", permission = "core.rank")
    public void create(CommandSender sender, String rankName) {
        if (rankController.findRank(rankName) != null) {
            sender.sendMessage(MessageConstants.RANK_ALREADY_EXISTS);
            return;
        }

        final Rank rank = new Rank(null, rankName);
        rank.sync();

        sender.sendMessage(MessageConstants.RANK_CREATED);
    }

    @Subcommand(parentLabel = "rank", label = "rename", permission = "core.rank")
    public void rename(CommandSender sender, Rank rank, String newName) {
        rank.getGeneralRankData().setId(newName);
        rank.sync();

        sender.sendMessage(MessageConstants.RANK_RENAMED);
    }

    @Subcommand(parentLabel = "rank", label = "delete", permission = "core.rank")
    public void delete(CommandSender sender, Rank rank) {
        rankController.unregister(rank);

        sender.sendMessage(MessageConstants.RANK_DELETED);
    }

    @Subcommand(parentLabel = "rank", label = "prefix", permission = "core.rank")
    public void prefix(CommandSender sender, Rank rank, String prefix) {
        rank.getGeneralRankData().setPrefix(prefix.replace("_", " ").replace("\"\"", ""));
        rank.sync();

        sender.sendMessage(MessageConstants.RANK_PREFIX_SET);
    }

    @Subcommand(parentLabel = "rank", label = "suffix", permission = "core.rank")
    public void suffix(CommandSender sender, Rank rank, String suffix) {
        rank.getGeneralRankData().setSuffix(suffix.replace("_", " ").replace("\"\"", ""));
        rank.sync();

        sender.sendMessage(MessageConstants.RANK_SUFFIX_SET);
    }

    @Subcommand(parentLabel = "rank", label = "weight")
    public void weight(CommandSender sender, Rank rank, Integer weight) {
        rank.getGeneralRankData().setWeight(weight);
        rank.sync();

        sender.sendMessage(MessageConstants.RANK_WEIGHT_SET);
    }

    @Subcommand(parentLabel = "rank", label = "color")
    public void color(CommandSender sender, Rank rank, ChatColor color) {
        rank.getGeneralRankData().setColor(color);
        rank.sync();

        sender.sendMessage(MessageConstants.RANK_COLOR_SET);
    }

    @Subcommand(parentLabel = "rank", label = "displayname")
    public void displayName(CommandSender sender, Rank rank, String displayName) {
        rank.getGeneralRankData().setDisplayName(displayName);
        rank.sync();

        sender.sendMessage(MessageConstants.RANK_DISPLAY_NAME_SET);
    }

    @Subcommand(parentLabel = "rank", label = "permission")
    public void permission(CommandSender sender, Rank rank, String type, String permission) {
        if (type.equalsIgnoreCase("add")) {
            rank.getGeneralRankData().addPermission(permission);
            sender.sendMessage(MessageConstants.RANK_ADDED_PERMISSION);
        } else if (type.equalsIgnoreCase("remove")) {
            rank.getGeneralRankData().removePermission(permission);
            sender.sendMessage(MessageConstants.RANK_REMOVED_PERMISSION);
        }

        rank.sync();
        rankController.refreshPermissions(rank);
    }

    @Subcommand(parentLabel = "rank", label = "hide")
    public void hide(CommandSender sender, Rank rank) {
        rank.getGeneralRankData().setHidden(!rank.getGeneralRankData().isHidden());
        rank.sync();

        sender.sendMessage(MessageConstants.RANK_HIDDEN_SET);
    }

    @Subcommand(parentLabel = "rank", label = "list")
    public void list(CommandSender sender) {
        this.rankController.getRanks().stream()
                .map(rank -> rank.getGeneralRankData().getId() + ChatColor.GRAY + " [" + ChatColor.WHITE + rank.getGeneralRankData().getWeight() + "]")
                .forEach(sender::sendMessage);
    }

    @Subcommand(parentLabel = "rank", label = "info")
    public void info(CommandSender sender, Rank rank) {
        sender.sendMessage(new String[]{
                ChatColor.BLUE + "-------" + ChatColor.GRAY + "[" + rank.getGeneralRankData().getColor() + rank.getGeneralRankData().getId() + ChatColor.GRAY + "]" + ChatColor.BLUE + "-------",
                ChatColor.AQUA + "Rank Information",
                ChatColor.BLUE + "* " + ChatColor.AQUA + "Unique Identifier: " + ChatColor.WHITE + rank.getUuid().toString(),
                ChatColor.BLUE + "* " + ChatColor.AQUA + "Display Name: " + rank.getGeneralRankData().getDisplayName(),
                ChatColor.BLUE + "* " + ChatColor.AQUA + "Color: " + rank.getGeneralRankData().getColor() + rank.getGeneralRankData().getColor().name(),
                ChatColor.BLUE + "* " + ChatColor.AQUA + "Prefix: " + ChatColor.translateAlternateColorCodes('&', rank.getGeneralRankData().getPrefix()),
                ChatColor.BLUE + "* " + ChatColor.AQUA + "Suffix: " + ChatColor.translateAlternateColorCodes('&', rank.getGeneralRankData().getSuffix()),
                ChatColor.BLUE + "* " + ChatColor.AQUA + "Weight: " + ChatColor.WHITE + rank.getGeneralRankData().getWeight(),
                        "",
                ChatColor.BLUE + "* " + ChatColor.AQUA + "Permissions: " + ChatColor.WHITE + String.join(", ", rank.getGeneralRankData().getPermissions()),
                "",
        });
    }
}