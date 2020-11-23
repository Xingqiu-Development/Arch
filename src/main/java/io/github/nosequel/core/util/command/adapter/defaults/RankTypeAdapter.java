package io.github.nosequel.core.util.command.adapter.defaults;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.rank.Rank;
import io.github.nosequel.core.rank.RankController;
import io.github.nosequel.core.util.command.adapter.TypeAdapter;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class RankTypeAdapter implements TypeAdapter<Rank> {

    private final RankController controller = CorePlugin.getInstance().getHandler().find(RankController.class);

    @Override
    public Rank convert(CommandSender sender, String source) {
        return controller.findRank(source);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String source) {
        return this.controller.getRanks().stream()
                .map(rank -> rank.getGeneralRankData().getId())
                .filter(name -> name.toLowerCase().startsWith(source.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Class<Rank> getType() {
        return Rank.class;
    }
}
