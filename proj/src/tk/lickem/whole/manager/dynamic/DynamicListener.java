package tk.lickem.whole.manager.dynamic;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import tk.lickem.whole.Whole;

public class DynamicListener implements Listener {

    public Whole w = Whole.getWhole();

    public DynamicListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, Whole.getWhole());
    }
}
