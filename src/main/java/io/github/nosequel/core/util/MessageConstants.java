package io.github.nosequel.core.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.*;

@UtilityClass
public class MessageConstants {

    public final String[] RANK_HELP_MESSAGE = {
            "",
            YELLOW + "-------" + GRAY + " [" + WHITE + "Rank Help" + GRAY + "] " + YELLOW + "-------",
            GOLD + "/rank create <name> ",
            GOLD + "/rank delete <name>",
            GOLD + "/rank rename <name> <newName> ",
            GOLD + "/rank prefix <name> <prefix> ",
            GOLD + "/rank suffix <name> <suffix> ",
            GOLD + "/rank weight <name> <weight> ",
            GOLD + "/rank color <name> <color> ",
            GOLD + "/rank displayname <name> <displayname>",
            GOLD + "/rank list",
            GOLD + "/rank hide <rank>",
            GOLD + "/rank info <name>",
            "",
    };

    public final String GRANT_INFO_BUTTON_NAME = GOLD + "Grant Id #%id% " + GRAY + "(%active%)";

    public final String[] GRANT_INFO_BUTTON_EXPIRED = new String[] {
            "",
            GOLD + "Rank: " + WHITE + "%rank%",
            GOLD + "Executor: " + WHITE + "%executor%",
            GOLD + "Reason: " + WHITE + "%reason%",
            "",
            GOLD + "Expired at: " + WHITE + "%expire_date%",
            "",
            GRAY + "Click to un-expire this grant"
    };

    public final String[] GRANT_INFO_BUTTON_ACTIVE = new String[] {
            "",
            GOLD + "Rank: " + WHITE + "%rank%",
            GOLD + "Issued by: " + WHITE + "%executor%",
            GOLD + "Reason: " + WHITE + "%reason%",
            "",
            GOLD + "Expires in: " + WHITE + "%expire_date%",
            "",
            GRAY + "Click to expire this grant",
    };

    public final String PUNISHMENT_BUTTON_NAME = GOLD + "Punishment Id #%id% " + GRAY + " (%active%)";

    public final String[] PUNISHMENT_INFO_BUTTON_ACTIVE = new String[] {
            "",
            GOLD + "Issued: " + WHITE + "%issue_date%",
            GOLD + "Issued by: " + WHITE + "%executor%",
            GOLD + "Reason: " + WHITE + "%reason%",
            "",
            GOLD + "Expires in: " + WHITE + "%expire_date%",
            "",
            GRAY + "Click to expire this punishment",
    };

    public final String[] PUNISHMENT_INFO_BUTTON_EXPIRED = new String[] {
            "",
            GOLD + "Issued: " + WHITE + "%issue_date%",
            GOLD + "Issued by: " + WHITE + "%executor%",
            GOLD + "Reason: " + WHITE + "%reason%",
            "",
            GOLD + "Expired at: " + WHITE + "%expire_date%",
            "",
            GRAY + "Click to re-activate this punishment",
    };

    public final String RANK_CREATED = YELLOW + "You have created a new rank.";
    public final String RANK_RENAMED = YELLOW + "You have renamed a rank";
    public final String RANK_DELETED = YELLOW + "You have deleted a rank.";
    public final String RANK_PREFIX_SET = YELLOW + "You have set the prefix of a rank.";
    public final String RANK_SUFFIX_SET = YELLOW + "You have set the suffix of a rank.";
    public final String RANK_WEIGHT_SET = YELLOW + "You have set the weight of a rank.";
    public final String RANK_COLOR_SET = YELLOW + "You have set the color of a rank.";
    public final String RANK_DISPLAY_NAME_SET = YELLOW + "You have set the display name of a rank.";
    public final String RANK_ADDED_PERMISSION = YELLOW + "You have added a permission to a rank";
    public final String RANK_REMOVED_PERMISSION = YELLOW + "You have removed a permission from a rank";
    public final String RANK_HIDDEN_SET = YELLOW + "You have set the hidden state of a rank";
    public final String RANK_ALREADY_EXISTS = RED + "That rank already exists.";

    public final String GRANT_SETTING_REASON = YELLOW + "Please type a reason for this grant to be added, or type " + RED + "cancel " + YELLOW + "to cancel.";
    public final String GRANT_SETTING_DURATION = YELLOW + "Please type a duration for this grant, \"perm\" for permanent or type " + RED + "cancel " + YELLOW + "to cancel.";
    public final String GRANT_CANCELLED = YELLOW + "You have cancelled granting";
    public final String GRANT_FINISHED = GREEN + "You have finished granting.";

    public final String PUNISHMENT_BAN_BROADCAST = WHITE + "%banned%" + GREEN + " has been banned by " + WHITE + "%issuer%";
    public final String PUNISHMENT_UNBAN_BROADCAST = WHITE + "%unbanned%" + GREEN + " has been unbanned by " + WHITE + "%issuer%";
    public final String PUNISHMENT_BAN_KICKED = String.join("\n",
            RED + "You have been banned from this server,",
            RED + "and are not allowed to join",
            "",
            RED + "You can appeal for an unban at our discord, discord.xingqiu.eu"
    );

    public final String PUNISHMENT_MESSAGE_CANCEL = RED + "You can't talk because you're muted";
    public final String PUNISHMENT_MUTE_BROADCAST = WHITE + "%muted%" + GREEN + " has been muted by " + WHITE + "%issuer%";
    public final String PUNISHMENT_UNMUTE_BROADCAST = WHITE + "%unmuted%" + GREEN + " has been unmuted by " + WHITE + "%issuer%";

    public final String STAFF_CHAT_MESSAGE = GRAY + "[%server%] " + DARK_PURPLE + "%sender%" + DARK_PURPLE + ": " + LIGHT_PURPLE + "%message%";
    public final String STAFF_SERVER_JOIN = BLUE + "[Staff] " + AQUA + "%player%" + AQUA + " has joined the network (%server%)";
    public final String STAFF_SERVER_LEFT = BLUE + "[Staff] " + AQUA + "%player%" + AQUA + " has left the network (%server%)";

    public final String CHAT_FORMAT = "%rank_prefix%%name_color%%player_name%%rank_suffix%&7: &f%message%";

    public final String SERVER = Bukkit.getServerId();
}