package io.github.nosequel.core.rank.data;

import com.google.gson.JsonObject;
import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.rank.RankController;
import io.github.nosequel.core.util.data.impl.SaveableData;
import io.github.nosequel.core.util.json.JsonAppender;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GeneralRankData implements SaveableData {

    private final RankController rankController = CorePlugin.getInstance().getHandler().find(RankController.class);

    private final String savePath = "general";
    private final List<String> permissions = new ArrayList<>();

    private String id = "";
    private String displayName = "";
    private String prefix = "";
    private String suffix = "";
    private ChatColor color = ChatColor.WHITE;

    private boolean hidden = false;
    private boolean def = false;

    private int weight = 0;

    public GeneralRankData() {

    }

    /**
     * Constructor for creating a new GeneralRankData instance
     *
     * @param id the id of the rank
     */
    public GeneralRankData(String id) {
        this.id = id;
    }

    public GeneralRankData(JsonObject object) {
        this.id = object.get("nameId").getAsString();
        this.displayName = object.get("displayName").getAsString();
        this.prefix = object.get("prefix").getAsString();
        this.suffix = object.get("suffix").getAsString();
        this.weight = object.get("weight").getAsInt();
        this.def = object.get("default").getAsBoolean();
        this.hidden = object.get("hidden").getAsBoolean();
        this.color = ChatColor.valueOf(object.get("color").getAsString());
    }


    /**
     * Add a permission to a rank
     *
     * @param permission the permission
     */
    public void addPermission(String permission) {
        this.permissions.add(permission);
        this.rankController.refreshPermissions(this.rankController.findRank(this.id));
    }

    /**
     * Remove a permission from a rank
     *
     * @param permission the permission to remove
     */
    public void removePermission(String permission) {
        this.permissions.remove(permission);
        this.rankController.refreshPermissions(this.rankController.findRank(this.id));
    }

    /**
     * Set the weight of a rank and automatically sort the ranks
     *
     * @param weight the weight of the rank
     */
    public void setWeight(int weight) {
        this.weight = weight;
        this.rankController.sort();
    }

    /**
     * Set the default status of the rank
     *
     * @param def the default status
     */
    public void setDefault(boolean def) {
        this.def = def;

        if(def) {
            this.rankController.getGrantController().registerDefaultGrant(this.rankController.findRank(this.id));
        }
    }


    @Override
    public JsonObject toJson() {
        return new JsonAppender().append("nameId", this.id)
                .append("displayName", this.displayName)
                .append("prefix", this.prefix)
                .append("suffix", this.suffix)
                .append("weight", this.weight)
                .append("default", this.def)
                .append("hidden", this.hidden)
                .append("color", color.name()).get();
    }
}
