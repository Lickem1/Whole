package tk.lickem.whole.data.enchantments;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import tk.lickem.whole.Whole;
import tk.lickem.whole.manager.DynamicManger;

public abstract class IEnchant extends Enchantment implements Listener {

    @Getter private final DynamicManger DM = Whole.getWhole().getDynamicManger();
    @Getter private final ItemEnchanter itemEnchanter = DynamicManger.get(ItemEnchanter.class);

    {
        getDM().getEnchants().add(this);
        Bukkit.getServer().getPluginManager().registerEvents(this, Whole.getWhole());
    }

    public IEnchant(int id) {
        super(id);
    }

    public abstract String getName();
    public abstract String getLore();
    public abstract int getStartLevel();
    public abstract int getMaxLevel();
    public abstract boolean canEnchantItem(ItemStack stack);
    public abstract boolean isTickable();
    public abstract void doTick();
}
