package tk.lickem.whole.data.enchantments.data.paintball;

import net.minecraft.server.v1_12_R1.EnumDirection;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import tk.lickem.whole.Whole;
import tk.lickem.whole.data.enchantments.AbstractEnchant;
import tk.lickem.whole.data.player.PlayerData;
import tk.lickem.whole.manager.dynamic.annotations.Init;
import tk.lickem.whole.util.direction.FixedDirection;

import java.util.*;

@Init
public class Paintball extends AbstractEnchant {

    public Paintball() {
        super(104);
    }

    @Override
    public String getName() {
        return "Paintball";
    }

    @Override
    public String getLore() {
        return "§aP§ba§ei§dn§ct§3b§5a§6l§7l §a%s";
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
    public boolean canEnchantItem(ItemStack stack) {
        return (stack.getType() == Material.STICK);
    }

    @Override
    public boolean isTickable() {
        return true;
    }

    private final LinkedList<PaintballData> paintballData = new LinkedList<>();

    @Override
    public void doTick() {

        Iterator<PaintballData> iterator = paintballData.iterator();

        while (iterator.hasNext()) {
            PaintballData data = iterator.next();

            if(data.getTime() == 0) {
                Block block = data.getBlockWorld().getBlockAt(data.getOriginalBlockLocation());
                Bukkit.getServer().getScheduler().runTask(Whole.getWhole(), () -> {
                    block.setType(data.getOriginalBlock());
                    block.setData(data.getOriginalBlockByte());
                });
                iterator.remove();
            } else data.setTime(data.getTime()-1);
        }

    }

    @EventHandler
    public void interaction(PlayerInteractEvent e) {
        Action a = e.getAction();
        if(a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK || e.getItem() == null || e.getItem().getType() == Material.AIR) return;

        Player p = e.getPlayer();
        PlayerData d = PlayerData.valueOf(p);
        ItemStack stack = e.getItem();

        if(stack.containsEnchantment(this)) {
            e.setCancelled(true);

            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BELL, 1f, 1f);

            Location newLocation = p.getEyeLocation();

            p.playEffect(newLocation, Effect.HAPPY_VILLAGER, 1);

            List<Snowball> balls = new ArrayList<>();
            Vector[] locations = {
                    new Vector(0, 0, 1), // Middle
                    new Vector(0, 0, 0), // Middle
                    new Vector(0, 0, -1), // Middle
                    new Vector(0, 1, 1),  // Above
                    new Vector(0, 1, 0),  // Above
                    new Vector(0, 1, -1),  // Above
                    new Vector(0, -1, 1),  // Below
                    new Vector(0, -1, 0),  // Below
                    new Vector(0, -1, -1)  // Below
            };

            for(Vector v : locations) {
                balls.add(p.getWorld().spawn(p.getEyeLocation().add(v), Snowball.class));
            }

            for(Snowball ball : balls) {
                ball.setMetadata("paintball", new FixedMetadataValue(Whole.getWhole(), p.getName()));
                ball.setShooter(p);
                ball.setVelocity(p.getLocation().getDirection().multiply(1.5));
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player p = (Player) e.getEntity().getShooter();
            EntityType type = e.getEntityType();

            if (type == EntityType.SNOWBALL) {
                if(!e.getEntity().getMetadata("paintball").isEmpty()) {
                    if(e.getHitBlock() != null) {
                        if(canAdd(e.getHitBlock().getLocation())) {
                            PaintballData data = new PaintballData(4, p.getWorld(), e.getHitBlock().getType(), e.getHitBlock().getData(), e.getHitBlock().getLocation());

                            paintballData.add(data);

                            Block block = p.getWorld().getBlockAt(e.getHitBlock().getLocation());
                            block.setType(Material.WOOL);
                            block.setData(DyeColor.values()[new Random().nextInt(DyeColor.values().length)].getWoolData());

                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onbreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();

        if(!canAdd(b.getLocation())) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "This block is in transition");
        }
    }

    private boolean canAdd(Location b) {
        Iterator<PaintballData> bb = paintballData.iterator();

        int matches = 0;
        while (bb.hasNext()) {
            PaintballData bt = bb.next();
            if (b.equals(bt.getOriginalBlockLocation())) {
                matches++;
            }
        }
        return (matches == 0);
    }
}
