package io.github.nosequel.core.util;

import io.github.nosequel.core.CorePlugin;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

@UtilityClass
public class PlayerUtil {

    /**
     * Add a permission to a player's PermissionAttachment
     *
     * @param player     the player
     * @param permission the permission
     */
    public void addPermission(Player player, String permission) {
        final PermissionAttachment attachment = player.addAttachment(CorePlugin.getInstance());

        attachment.getPermissions().clear();
        attachment.setPermission(permission, true);
    }

}
