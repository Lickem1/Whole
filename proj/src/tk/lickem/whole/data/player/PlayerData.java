package tk.lickem.whole.data.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import tk.lickem.whole.data.backpack.BackPack;
import tk.lickem.whole.manager.DynamicManger;
import tk.lickem.whole.manager.PlayerDataManager;

import java.util.UUID;

@Setter
@Getter
public class PlayerData {

    private final UUID uuid;
    private Player player;
    private BackPack backPack;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public static PlayerData valueOf(Player p) {
        return DynamicManger.get(PlayerDataManager.class).getProfile(p);
    }
}
