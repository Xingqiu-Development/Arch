package io.github.nosequel.core.player.data;

import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.rank.Rank;
import io.github.nosequel.core.util.data.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerGrantProcedureData implements Data {

    public String setReason;
    public Rank grantingRank;
    public CorePlayer grantingPlayer;

    public boolean settingReason, settingDuration;

    /**
     * Method for setting up the grant process
     *
     * @param grantingRank   the rank to grant
     * @param grantingPlayer the target to grant the rank to
     */
    public void setup(Rank grantingRank, CorePlayer grantingPlayer) {
        this.grantingRank = grantingRank;
        this.grantingPlayer = grantingPlayer;
        this.settingReason = true;
    }

    /**
     * Clear the class' data
     */
    public void cleanup() {
        this.setReason = "";
        this.grantingPlayer = null;
        this.grantingRank = null;
        this.settingReason = false;
        this.settingDuration = false;
    }

}
