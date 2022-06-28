package tk.lickem.whole.data.gems;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import tk.lickem.whole.Whole;
import tk.lickem.whole.data.player.PlayerData;
import tk.lickem.whole.manager.dynamic.DynamicListener;
import tk.lickem.whole.manager.dynamic.annotations.Init;
import tk.lickem.whole.util.itembuilder.ItemBuilder;

@Init
public class GemManager extends DynamicListener {

    public boolean buildGem(Player p, Gem gem) {
        ItemBuilder item = new ItemBuilder(Material.PRISMARINE_CRYSTALS, 1 ,0).setName("&f" + gem.getAmount() + " &bGems");
        item.setLore("&f» &7Right click to redeem gems!");
        item.addEnchant(Enchantment.MENDING, 1);

        NBTTagCompound data = new NBTTagCompound();
        data.setString("gem_data", Whole.getWhole().getGson().toJson(gem));
        item.addTag(data);
        item.addFlag(ItemFlag.HIDE_ENCHANTS);

        if(p.getInventory().firstEmpty() != -1) {
            p.getInventory().addItem(item.build());
            return true;
        } else return false;
    }

    public boolean isGem(ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack stack = CraftItemStack.asNMSCopy(item);
        return (stack.hasTag() && !stack.getTag().getString("gem_data").isEmpty());
    }


    public Gem gemData(ItemStack item) {
        if (isGem(item)) {
            net.minecraft.server.v1_12_R1.ItemStack stack = CraftItemStack.asNMSCopy(item);
            Gem gem = Whole.getWhole().getGson().fromJson(stack.getTag().getString("gem_data"), Gem.class);

            return gem;
        } else return null;
    }

    @EventHandler
    public void interaction(PlayerInteractEvent e) {
        Action a = e.getAction();
        if(a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK || e.getItem() == null || e.getItem().getType() == Material.AIR) return;

        Player p = e.getPlayer();
        PlayerData d = PlayerData.valueOf(p);
        ItemStack stack = e.getItem();

        if(isGem(stack)) {
            e.setCancelled(true);

            Gem gemdata = gemData(stack);
            p.getInventory().setItem(p.getInventory().getHeldItemSlot(), null);
            p.sendMessage("Successfully redeemed " + gemdata.getAmount() + " Gems!");
        }

    }
}
