package tk.lickem.whole.data.enchantments.data.regenerate;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import tk.lickem.whole.Whole;
import tk.lickem.whole.data.enchantments.IEnchant;
import tk.lickem.whole.manager.dynamic.anno.Init;

import java.util.Iterator;
import java.util.LinkedList;

@Init
public class Regenerate extends IEnchant {

    // The block function doesn't like to be called async :(

    public Regenerate() {
        super(101);
    }

    @Override
    public String getName() {
        return "Regenerate";
    }

    @Override
    public String getLore() {
        return ChatColor.LIGHT_PURPLE + "Regenerate %s";
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
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return (itemStack.getType().toString().contains("PICKAXE"));
    }

    @Override
    public boolean isTickable() {
        return true;
    }

    private LinkedList<RBlockData> blocks = new LinkedList<>();


    @Override
    public void doTick() {

        Iterator<RBlockData> rBlockDataIterator = blocks.iterator();

        while (rBlockDataIterator.hasNext()) {
            RBlockData d = rBlockDataIterator.next();
            World world = d.getOriginalBlockLocation().getWorld();


            if (d.getTime() == 0) {
                Bukkit.getServer().getScheduler().runTask(Whole.getWhole(), () -> world.getBlockAt(d.getOriginalBlockLocation()).setType(d.getOriginalBlock()));
                rBlockDataIterator.remove();
            } else {
                Bukkit.getServer().getScheduler().runTask(Whole.getWhole(), () -> world.getBlockAt(d.getOriginalBlockLocation()).setType(d.getBlocks()[d.getTime()]));
                d.setTime(d.getTime() - 1);
            }


        }

    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getPlayer();
        Block b = e.getBlock();
        ItemStack item = p.getInventory().getItemInMainHand();

        if (canAdd(b.getLocation())) {

            if (item.containsEnchantment(this)) {

                RBlockData data = new RBlockData();
                data.setTime(7);
                data.setOriginalBlock(b.getType());
                data.setOriginalBlockLocation(b.getLocation());

                p.getWorld().getBlockAt(b.getLocation()).setType(data.getBlocks()[data.getTime()-1]);

                blocks.add(data);
            }

        } else {
            p.sendMessage("§cYou cannot break this block");
            e.setCancelled(true);
        }
    }

    private boolean canAdd(Location b) {
        Iterator<RBlockData> bb = blocks.iterator();

        int matches = 0;
        while (bb.hasNext()) {
            RBlockData bt = bb.next();
            if (b.equals(bt.getOriginalBlockLocation())) {
                matches++;
            }
        }
        return (matches == 0);
    }
}
