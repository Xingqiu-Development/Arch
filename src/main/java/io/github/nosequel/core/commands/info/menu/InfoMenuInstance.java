package io.github.nosequel.core.commands.info.menu;

import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import io.github.nosequel.katakuna.menu.Menu;
import io.github.nosequel.katakuna.menu.paginated.PaginatedMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class InfoMenuInstance extends PaginatedMenu {

    private final InfoMenu instance;
    private final Menu menu;

    /**
     * Constructor for making a new instance of a InfoMenuInstance class
     *
     * @param menu     the menu to make an InfoMenu instance of
     * @param instance the original instance
     */
    public InfoMenuInstance(Menu menu, InfoMenu instance) {
        super(menu.getPlayer(), menu.getTitle(), menu instanceof PaginatedMenu ? menu.getSize() - 9 : menu.getSize());

        this.menu = menu;
        this.instance = instance;
    }

    @Override
    public List<Button> getButtons() {
        final List<Button> buttons = menu.getButtons();

        buttons.add(new ButtonBuilder(Material.LEVER)
                .setAmount(1)
                .setDisplayName(ChatColor.GRAY + "Go back")
                .setIndex(-5)
                .setAction((type, player) -> new InfoMenu(instance.getPlayer(), instance.getTarget()).updateMenu()));

        return buttons;
    }
}