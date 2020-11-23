package io.github.nosequel.core.commands.user;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.player.data.PlayerGeneralData;
import io.github.nosequel.core.util.command.annotation.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ResetUserCommand {

    private final CorePlayerController playerController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);

    @Command(label = "resetuser", permission = "core.resetuser")
    public void resetuser(CommandSender sender, CorePlayer target) {
        final PlayerGeneralData playerData = target.findData(PlayerGeneralData.class);

        playerController.getPlayers().remove(target);
        playerController.insertUser(
                target.getUuid(),
                playerData.getName()
        );

        sender.sendMessage(ChatColor.YELLOW + "You have reset " + ChatColor.WHITE + playerData.getName() + ChatColor.YELLOW + "'s profile");
    }

}
