package tk.lickem.whole.manager.dynamic;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.scheduler.BukkitRunnable;
import tk.lickem.whole.Whole;
import tk.lickem.whole.data.enchantments.AbstractEnchant;
import tk.lickem.whole.manager.DynamicManger;
import tk.lickem.whole.manager.dynamic.annotations.PostInit;

import java.lang.reflect.Field;

@PostInit
public class DynamicEnchant extends BukkitRunnable {

    private DynamicManger dm = Whole.getWhole().getDynamicManger();

    public DynamicEnchant() {

        for(Enchantment e : dm.getEnchants()) {
            if(register(e)) System.out.println("[*] Registered enchant " + e.getName());
            else System.out.println("[!] Failed to register enchant " + e.getName());
        }

        System.out.println("[*] Total of " + dm.getEnchants().size() + " custom enchantments loaded!");

        runTaskTimerAsynchronously(Whole.getWhole(), 1, 20L);
    }

    private boolean register(Enchantment enchant) {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);

            Enchantment.registerEnchantment(enchant);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void run() {

        for (AbstractEnchant e : dm.getEnchants()) {
            if(e.isTickable()) e.doTick();
        }
    }
}
