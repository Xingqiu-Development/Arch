package io.github.nosequel.core.util.command.adapter.defaults;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.player.data.PlayerGeneralData;
import io.github.nosequel.core.util.command.adapter.TypeAdapter;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class CorePlayerTypeAdapter implements TypeAdapter<CorePlayer> {

    private final CorePlayerController controller = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);

    @Override
    public CorePlayer convert(CommandSender sender, String source) {
        return controller.find(source);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String source) {
        return controller.getPlayers().stream()
                .map(player -> player.findData(PlayerGeneralData.class).getName())
                .filter(name -> name.toLowerCase().startsWith(source.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Class<CorePlayer> getType() {
        return CorePlayer.class;
    }
}