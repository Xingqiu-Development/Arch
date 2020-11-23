package io.github.nosequel.core.listener.chat;

import org.bukkit.entity.Player;

public interface ChatProcedure {

    /**
     * Handle a chat message for the active chat procedure
     *
     * @param player  the player
     * @param message the message
     */
    boolean handle(Player player, String message);

}
