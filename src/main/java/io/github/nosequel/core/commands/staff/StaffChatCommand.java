package io.github.nosequel.core.commands.staff;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.commands.sync.MessageSynchronizationHandler;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.util.MessageConstants;
import io.github.nosequel.core.util.command.annotation.Command;
import io.github.nosequel.core.util.command.executor.CustomCommandSender;
import io.github.nosequel.core.util.json.JsonAppender;
import io.github.nosequel.core.util.synchronize.SynchronizeController;

public class StaffChatCommand {

    private CorePlayerController playerController;
    private SynchronizeController synchronizeController;

    @Command(label = "staffchat", aliases = {"sc"}, permission = "core.staffchat")
    public void staffchat(CustomCommandSender player, String[] message) {
        if(this.synchronizeController == null || this.playerController == null) {
            this.synchronizeController = CorePlugin.getInstance().getHandler().find(SynchronizeController.class);
            this.playerController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);
        }

        if(message.length != 0 && !message[0].equals("")) {
            synchronizeController.synchronize(
                    MessageSynchronizationHandler.class,
                    new JsonAppender()
                            .append("message", MessageConstants.STAFF_CHAT_MESSAGE
                                    .replace("%sender%", player.getDisplayName())
                                    .replace("%server%", MessageConstants.SERVER)
                                    .replace("%message%", String.join(" ", message)))

                            .append("permission", "core.staffchat").get()
            );
        }
    }
}