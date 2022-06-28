package tk.lickem.whole.data.enchantments.data.messager;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import tk.lickem.whole.data.enchantments.AbstractEnchant;
import tk.lickem.whole.manager.dynamic.annotations.Init;

import java.util.Random;

@Init
public class Messager extends AbstractEnchant {

    public Messager() {
        super(100);
    }

    @Override
    public String getName() {
        return "Messager";
    }

    @Override
    public String getLore() {
        return ChatColor.GRAY + "Messager %s";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return (itemStack.getType().toString().contains("PICKAXE"));
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public boolean isTickable() {
        return false;
    }

    @Override
    public void doTick() {

    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        if (item.containsEnchantment(this)) {
            double random = new Random().nextDouble() * 100.0D;
            if(random < chance(item.getEnchantmentLevel(this))) {
                p.sendMessage(Messages.random());
            }
        }
    }

    private double chance(int level) {
        return (5 * (level));
    }
}
