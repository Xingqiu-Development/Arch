package io.github.nosequel.core.commands.grant.menu;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.commands.grant.menu.buttons.GrantButton;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.rank.RankController;
import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.menu.paginated.PaginatedMenu;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GrantMenu extends PaginatedMenu {

    private final RankController rankController = CorePlugin.getInstance().getHandler().find(RankController.class);
    private final CorePlayer target;

    public GrantMenu(Player player, CorePlayer target) {
        super(player, "Choose a Rank", 9);

        this.target = target;
    }

    @Override
    public List<Button> getButtons() {
        final AtomicInteger index = new AtomicInteger();

        return rankController.getRanks().stream()
                .map(rank -> new GrantButton(rank, target, index.getAndIncrement()))
                .collect(Collectors.toList());
    }
}