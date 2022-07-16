package tk.lickem.whole.manager;

import lombok.Getter;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import tk.lickem.whole.Whole;
import tk.lickem.whole.data.hologram.Hologram;
import tk.lickem.whole.data.packet.ClassType;
import tk.lickem.whole.manager.dynamic.annotations.Init;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Init(classType = {ClassType.CONSTRUCT})
public class HologramManager extends BukkitRunnable {

    @Getter
    private final List<Hologram> holograms = new LinkedList<>();

    {
        runTaskTimerAsynchronously(Whole.getWhole(), 1, 20);
    }


    @Override
    public void run() {
        Iterator<Hologram> iterator = holograms.iterator();

        while (iterator.hasNext()) {
            Hologram hologram = iterator.next();
            hologram.tryRefresh();
        }
    }

    public Hologram getHologram(String id) {
        for (Hologram hologram : holograms) if (hologram.getName().equalsIgnoreCase(id)) return hologram;
        return null;
    }

    public Hologram isHologramEntity(int id) {
        for (Hologram hologram : holograms) {
            for(EntityArmorStand armorStand : hologram.getArmorStands().values()) {
                if(armorStand.getId() == id) return hologram;
            }
        }
        return null;
    }
}
