package tk.lickem.whole.data.backpack;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import tk.lickem.whole.data.player.PlayerData;
import tk.lickem.whole.data.packet.ClassType;
import tk.lickem.whole.manager.dynamic.DynamicListener;
import tk.lickem.whole.manager.dynamic.annotations.Init;
import tk.lickem.whole.util.itembuilder.ItemBuilder;

@Init(classType = ClassType.CONSTRUCT)
public class BackPackManager extends DynamicListener {

    public void createBackPack(Player p, BackPack backPack) {
        PlayerData d = PlayerData.valueOf(p);

        ItemBuilder item = new ItemBuilder(Material.CHEST, 1, 0).setName("&3&lBackPack");
        item.setLore(
                "&bOwner:&f " + p.getName(),
                "&bSize:&f " + (backPack.getSize().getNumerator() / 9) + " rows",
                "§a ",
                "&f> &eRight click to access your backpack!"
        );
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("backpack", p.getName());

        item.addTag(tag);

        backPack.setInv(Bukkit.createInventory(p, backPack.getSize().getNumerator(), "§3" + p.getName() + "'s BackPack"));
        d.setBackPack(backPack);
        p.getInventory().addItem(item.build());
        p.sendMessage(" > Enjoy your new backpack!!");
    }

    public boolean isBackPackItem(ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack stack = CraftItemStack.asNMSCopy(item);
        return (stack.hasTag() && !stack.getTag().getString("backpack").isEmpty());
    }

    @EventHandler
    public void drop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (isBackPackItem(e.getItemDrop().getItemStack())) {
            e.setCancelled(true);
            p.sendMessage("You cannot drop backpacks");
        }
    }


    @EventHandler
    public void interaction(PlayerInteractEvent e) {
        Action a = e.getAction();
        if(a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK || e.getItem() == null || e.getItem().getType() == Material.AIR) return;

        Player p = e.getPlayer();
        PlayerData d = PlayerData.valueOf(p);
        ItemStack stack = e.getItem();

        if(isBackPackItem(stack)) {
            e.setCancelled(true);
            if(d.getBackPack() != null) p.openInventory(d.getBackPack().getInv());
            else p.sendMessage("Unable to open or find backpack");
        }

    }

    @EventHandler
    public void place(BlockPlaceEvent e) {
        Player p = e.getPlayer();
    }


    @EventHandler
    public void inv(InventoryClickEvent e) {
        ItemStack clicked = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();

        if(p.getOpenInventory() != null) {
            if(p.getOpenInventory().getTitle().contains("BackPack")) {
                if(isBackPackItem(clicked)) e.setCancelled(true);
            }
        }
    }
}
