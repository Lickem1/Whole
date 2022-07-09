package tk.lickem.whole.manager;

import org.bukkit.scheduler.BukkitRunnable;
import tk.lickem.whole.Whole;
import tk.lickem.whole.data.hologram.Hologram;
import tk.lickem.whole.manager.dynamic.annotations.Init;

import java.util.Iterator;

@Init
public class HologramManager extends BukkitRunnable {

    {
        runTaskTimerAsynchronously(Whole.getWhole(), 1, 20);
    }


    @Override
    public void run() {
        Iterator<Hologram> iterator = Hologram.getHolograms().iterator();

        while (iterator.hasNext()) {
            Hologram hologram = iterator.next();
            hologram.tryRefresh();
        }
    }
}
