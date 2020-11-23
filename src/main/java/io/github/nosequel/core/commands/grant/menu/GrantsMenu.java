package io.github.nosequel.core.commands.grant.menu;

import io.github.nosequel.core.commands.grant.menu.buttons.GrantInfoButton;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.data.PlayerGrantData;
import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.menu.paginated.PaginatedMenu;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GrantsMenu extends PaginatedMenu {

    private final CorePlayer target;

    public GrantsMenu(Player player, CorePlayer target) {
        super(player, "Choose a Grant", 9);

        this.target = target;
    }

    @Override
    public List<Button> getButtons() {
        final AtomicInteger index = new AtomicInteger();
        final PlayerGrantData grantData = target.findData(PlayerGrantData.class);

        return grantData.getGrants().stream()
                .map(grant -> new GrantInfoButton(grant, target, index.getAndIncrement()))
                .collect(Collectors.toList());
    }
}