package io.github.nosequel.core.commands.info;

import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.util.data.Data;
import io.github.nosequel.katakuna.button.Button;

public interface InfoableData extends Data {

    Button getButton(CorePlayer target);

}
