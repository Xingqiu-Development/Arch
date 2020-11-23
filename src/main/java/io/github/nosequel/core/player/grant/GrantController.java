package io.github.nosequel.core.player.grant;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.controller.Controller;
import io.github.nosequel.core.controller.ControllerPriority;
import io.github.nosequel.core.player.grant.task.GrantCheckTask;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.player.data.PlayerGrantData;
import io.github.nosequel.core.rank.Rank;
import io.github.nosequel.core.rank.RankController;
import io.github.nosequel.core.util.SortableArrayList;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Comparator;
import java.util.Objects;

@Getter
public class GrantController implements Controller {

    private final SortableArrayList<Grant> defaultGrants = new SortableArrayList<>(Comparator.comparingInt(grant -> grant.getRank().getWeight()));
    private final ControllerPriority loadPriority = ControllerPriority.LOW;

    private RankController rankController;
    private CorePlayerController playerController;

    @Override
    public void load() {
        this.playerController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);
        this.rankController = CorePlugin.getInstance().getHandler().find(RankController.class);

        new GrantCheckTask();
    }

    /**
     * Register a new default grant
     *
     * @param rank the default rank
     */
    public void registerDefaultGrant(Rank rank) {
        if (this.defaultGrants.stream().noneMatch(grant -> grant.getRank().equals(rank)) && rank.getGeneralRankData().isDef()) {
            this.defaultGrants.add(new Grant(null, rank, ChatColor.DARK_RED + "CONSOLE"));
            this.refreshGrants();
        }
    }

    /**
     * Refresh grants of all online players
     */
    public void refreshGrants() {
        this.defaultGrants.stream()
                .filter(grant -> !grant.getRank().getGeneralRankData().isDef())
                .forEach(this.defaultGrants::remove);

        if(this.playerController != null) {
            Bukkit.getOnlinePlayers().stream()
                    .map(playerController::find)
                    .map(player -> player.findData(PlayerGrantData.class))
                    .filter(Objects::nonNull)
                    .forEach(PlayerGrantData::fixGrants);
        }
    }
}