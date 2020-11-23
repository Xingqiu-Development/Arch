package io.github.nosequel.core.commands.grant.procedure;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.listener.chat.ChatProcedure;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.player.data.PlayerGrantProcedureData;
import io.github.nosequel.core.util.MessageConstants;
import org.bukkit.entity.Player;


public class GrantReasonProcedure implements ChatProcedure {

    private final CorePlayerController playerController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);

    @Override
    public boolean handle(Player player, String message) {
        final CorePlayer profile = playerController.find(player);
        final PlayerGrantProcedureData grantProcedureData = profile.findData(PlayerGrantProcedureData.class);

        if(grantProcedureData.isSettingReason()) {
            if(message.equalsIgnoreCase("cancel")) {
                grantProcedureData.cleanup();
                player.sendMessage(MessageConstants.GRANT_CANCELLED);

                return false;
            }

            player.sendMessage(MessageConstants.GRANT_SETTING_DURATION);

            grantProcedureData.setSetReason(message);
            grantProcedureData.setSettingReason(false);
            grantProcedureData.setSettingDuration(true);

            return false;
        }

        return true;
    }
}
