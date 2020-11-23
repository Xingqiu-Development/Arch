package io.github.nosequel.core.commands.grant.procedure;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.player.grant.Grant;
import io.github.nosequel.core.listener.chat.ChatProcedure;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.player.data.PlayerGrantProcedureData;
import io.github.nosequel.core.player.data.PlayerGrantData;
import io.github.nosequel.core.util.DurationUtil;
import io.github.nosequel.core.util.MessageConstants;
import org.bukkit.entity.Player;

public class GrantDurationProcedure implements ChatProcedure {

    private final CorePlayerController playerController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);

    @Override
    public boolean handle(Player player, String message) {
        final CorePlayer profile = playerController.find(player);
        final PlayerGrantProcedureData grantProcedureData = profile.findData(PlayerGrantProcedureData.class);

        if(grantProcedureData.isSettingDuration()) {
            if(message.equalsIgnoreCase("cancel")) {
                grantProcedureData.cleanup();
                player.sendMessage(MessageConstants.GRANT_CANCELLED);

                return false;
            }

            long duration;

            if(message.equalsIgnoreCase("perm") || message.equalsIgnoreCase("permanent")) {
                duration = -1L;
            } else {
                try {
                    duration = DurationUtil.parseTime(message);
                } catch (Exception ignored) {
                    duration = -1L;
                }
            }

            final CorePlayer target = grantProcedureData.getGrantingPlayer();

            target.findData(PlayerGrantData.class).getGrants().add(
                    new Grant(
                            null,
                            grantProcedureData.getGrantingRank(),
                            profile.getDisplayName(),
                            grantProcedureData.getSetReason(),
                            duration)
            );

            target.sync();

            grantProcedureData.cleanup();

            player.sendMessage(MessageConstants.GRANT_FINISHED);

            return false;
        }

        return true;
    }
}
