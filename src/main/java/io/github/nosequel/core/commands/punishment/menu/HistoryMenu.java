package io.github.nosequel.core.commands.punishment.menu;

import io.github.nosequel.core.commands.punishment.menu.button.PunishmentButton;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.data.PlayerPunishmentData;
import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.menu.paginated.PaginatedMenu;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class HistoryMenu extends PaginatedMenu {

    private final CorePlayer target;

    public HistoryMenu(Player player, CorePlayer target) {
        super(player, "History of a Player", 9);
        this.target = target;
    }

    @Override
    public List<Button> getButtons() {
        final AtomicInteger index = new AtomicInteger();
        final PlayerPunishmentData punishmentData = target.findData(PlayerPunishmentData.class);

        return punishmentData.getPunishments().stream()
                .map(punishment -> new PunishmentButton(punishment, target, index.getAndIncrement()))
                .collect(Collectors.toList());
    }
}