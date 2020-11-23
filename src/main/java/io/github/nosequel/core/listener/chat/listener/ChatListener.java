package io.github.nosequel.core.listener.chat.listener;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.listener.chat.ChatProcedure;
import io.github.nosequel.core.listener.chat.ChatProcedureController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final ChatProcedureController controller = CorePlugin.getInstance().getHandler().find(ChatProcedureController.class);

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        final String message = event.getMessage();
        final Player player = event.getPlayer();

        for (ChatProcedure procedure : controller.getChatProcedures()) {
            if (!procedure.handle(player, message)) {
                return;
            }
        }
    }
}