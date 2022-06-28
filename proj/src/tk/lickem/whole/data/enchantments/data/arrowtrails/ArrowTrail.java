package tk.lickem.whole.data.enchantments.data.arrowtrails;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import tk.lickem.whole.Whole;
import tk.lickem.whole.data.enchantments.AbstractEnchant;
import tk.lickem.whole.manager.dynamic.annotations.Init;

@Init
public class ArrowTrail extends AbstractEnchant {

    public ArrowTrail() {
        super(103);
    }

    @Override
    public String getName() {
        return "ArrowTrail";
    }

    @Override
    public String getLore() {
        return ChatColor.GRAY + "ArrowTrail %s";
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
        return (stack.getType() == Material.BOW);
    }

    @Override
    public boolean isTickable() {
        return false;
    }

    @Override
    public void doTick() {
    }

    private BukkitTask trailTask;

    @EventHandler
    public void trail(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player p = (Player) e.getEntity().getShooter();
            EntityType type = e.getEntityType();

            if (p.getInventory().getItemInMainHand().containsEnchantment(this)) {
                if (type == EntityType.ARROW) {
                    e.getEntity().setMetadata("arrowTrail", new FixedMetadataValue(Whole.getWhole(), true));

                    trailTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Whole.getWhole(), () -> {
                        Location loc = e.getEntity().getLocation();
                        int unit = 360 /5;
                        int time = 0;

                        time++;
                        for(int i = 0; i < 5; i++) {
                            int degrees = ( ((i*unit) + (time *32)/5) % 360);

                            float x = (float) loc.getX();
                            float y = (float) (loc.getY()+.7);
                            float z = (float) loc.getZ();
                            double rad = Math.toRadians(degrees);
                            x+=Math.sin(rad)*.7;
                            z+=Math.cos(rad)*.7;

                            Location newloc = new Location(e.getEntity().getWorld(), x, y, z);

                            e.getEntity().getWorld().playEffect(newloc, Effect.HEART, 1);
                        }


                    }, 1, 0);
                }
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            EntityType type = e.getEntityType();

            if (type == EntityType.ARROW) {
                if(!e.getEntity().getMetadata("arrowTrail").isEmpty()) {
                    trailTask.cancel();
                }
            }
        }
    }
}
