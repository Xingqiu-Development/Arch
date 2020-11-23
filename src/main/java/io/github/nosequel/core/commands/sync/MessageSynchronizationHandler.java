package io.github.nosequel.core.commands.sync;

import com.google.gson.JsonObject;
import io.github.nosequel.core.util.synchronize.SynchronizationHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class MessageSynchronizationHandler implements SynchronizationHandler {

    @Override
    public String getType() {
        return "message-sync";
    }

    @Override
    public void handle(JsonObject object) {
        final String permission = !object.has("permission") ? "" : object.get("permission").getAsString();
        final String message = object.get("message").getAsString();

        if (!permission.equals("")) {
            Bukkit.getOnlinePlayers().stream()
                    .filter(player -> player.hasPermission(permission))
                    .forEach(player -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
        } else {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }
}