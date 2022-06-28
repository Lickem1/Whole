package tk.lickem.whole.data.enchantments.data.lumberjack;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import tk.lickem.whole.data.enchantments.AbstractEnchant;
import tk.lickem.whole.manager.dynamic.annotations.Init;

import java.util.ArrayList;
import java.util.List;

@Init
public class Lumberjack extends AbstractEnchant {

    public Lumberjack() {
        super(102);
    }

    @Override
    public String getName() {
        return "Lumberjack";
    }

    @Override
    public String getLore() {
        return ChatColor.RED + "Lumberjack %s";
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
        return true;
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
    public boolean canEnchantItem(ItemStack stack) {
        return (stack.getType().toString().contains("_AXE"));
    }

    @Override
    public boolean isTickable() {
        return false;
    }

    @Override
    public void doTick() {

    }

    @EventHandler
    public void blockbreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        ItemStack axe = p.getInventory().getItemInMainHand();
        short durCalc = 0;

        if(axe.containsEnchantment(this)) {
            if(canBreak(b)) {
                for(Block blocks : breakableBlocks(b)) {
                    blocks.breakNaturally(axe);
                    durCalc++;
                }
                short newDur = (short) (durCalc + axe.getDurability());
                if(p.getGameMode() == GameMode.SURVIVAL) axe.setDurability(newDur);
                p.sendMessage("Whoosh!");
            }
        }

    }

    private boolean canBreak(Block b) {
        World w = b.getWorld();
        Block bottomloc = w.getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY()-1, b.getLocation().getBlockZ());
        if(targetBlock(b)) { // Checks if broken block is instance of log
            return bottomloc.getType() == Material.DIRT; // Checks if it's the bottom log
        }
        return false;
    }

    private boolean targetBlock(Block block) {
        return (block.getType() == Material.LOG);
    }

    private List<Block> breakableBlocks(Block bottom) {
        List<Block> blocks = new ArrayList<>();
        int x = bottom.getX();
        int z = bottom.getZ();

        for(int y = bottom.getY()+1; y < bottom.getWorld().getMaxHeight(); y++) {
            Block above = bottom.getWorld().getBlockAt(x, y, z);
            if(above.getType() == bottom.getType()) blocks.add(above);
        }
        return blocks;
    }
}
