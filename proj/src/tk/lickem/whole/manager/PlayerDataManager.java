package tk.lickem.whole.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import tk.lickem.whole.Whole;
import tk.lickem.whole.data.packet.PacketInjector;
import tk.lickem.whole.data.player.PlayerData;
import tk.lickem.whole.data.packet.ClassType;
import tk.lickem.whole.manager.dynamic.DynamicListener;
import tk.lickem.whole.manager.dynamic.annotations.Init;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

@Init(classType = ClassType.CONSTRUCT)
public class PlayerDataManager extends DynamicListener {

    public PlayerDataManager() {
        Bukkit.getServer().getPluginManager().registerEvents(this, Whole.getWhole());
    }

    private final Map<UUID, PlayerData> playerMaps = new WeakHashMap<>();

    public PlayerData getProfile(UUID uuid) {
        return playerMaps.get(uuid);
    }

    public PlayerData getProfile(Player p) {
        return playerMaps.get(p.getUniqueId());
    }

    public void createData(UUID uuid) {
        playerMaps.remove(uuid);
        playerMaps.put(uuid, new PlayerData(uuid));
    }

    public void createData(Player p) {
        playerMaps.put(p.getUniqueId(), new PlayerData(p.getUniqueId()));
    }

    public void delete(UUID uuid) {
        playerMaps.remove(uuid);
    }

    public void delete(Player p) {
        playerMaps.remove(p);
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        createData(e.getPlayer());
        getProfile(e.getPlayer()).setPlayer(e.getPlayer());

        DynamicManger.get(PacketInjector.class).injectHandler(e.getPlayer()); // packet injector
    }

    @EventHandler
    public void leave(PlayerQuitEvent e) {
        delete(e.getPlayer());

        DynamicManger.get(PacketInjector.class).ejectHandler(e.getPlayer()); // packet injector

    }
}
