package io.github.nosequel.core.commands.user;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.util.UUIDUtil;
import io.github.nosequel.core.util.command.annotation.Command;
import io.github.nosequel.core.util.command.annotation.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class InsertUserCommand {

    private final CorePlayerController playerController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);

    @Command(label = "insertuser", permission = "core.insertuser")
    public void insertUser(CommandSender sender, String name, @Parameter(name = "uuid", value = "none") String uuid) {
        final long start = System.currentTimeMillis();

        playerController.insertUser(
                uuid.equalsIgnoreCase("none") ? UUIDUtil.getUuidOfUsername(name) : UUID.fromString(uuid),
                name
        );

        sender.sendMessage(ChatColor.GREEN + "You have inserted a new user with the name " + ChatColor.WHITE + name + ChatColor.YELLOW + " (" + (System.currentTimeMillis() - start) + "ms)");
    }
}