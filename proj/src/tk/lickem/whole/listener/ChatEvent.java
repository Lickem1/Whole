package tk.lickem.whole.listener;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import tk.lickem.whole.data.backpack.BackPack;
import tk.lickem.whole.data.backpack.BackPackManager;
import tk.lickem.whole.data.backpack.BackPackSize;
import tk.lickem.whole.data.enchantments.ItemEnchanter;
import tk.lickem.whole.data.gems.GemManager;
import tk.lickem.whole.data.gems.Gem;
import tk.lickem.whole.data.player.PlayerData;
import tk.lickem.whole.manager.DynamicManger;
import tk.lickem.whole.manager.dynamic.DynamicListener;
import tk.lickem.whole.manager.dynamic.annotations.Init;

@Init
public class ChatEvent extends DynamicListener {

    @EventHandler
    public void chat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        PlayerData d = PlayerData.valueOf(p);
        ItemStack stack = p.getInventory().getItemInMainHand();

        ItemEnchanter ie = DynamicManger.get(ItemEnchanter.class);
        BackPackManager bpm = DynamicManger.get(BackPackManager.class);

        e.setCancelled(true);
        switch (e.getMessage().toLowerCase()) {

            case "messager":
                e.setCancelled(true);
                Enchantment messager = Enchantment.getByName("Messager");

                if(ie.hasEnchant(messager, p.getInventory().getItemInMainHand())) {
                    int o = ie.getEnchantLevel(messager, stack);
                    ie.enchantItem(messager, o+1, stack, p);
                }
                else ie.enchantItem(messager, 1, stack, p);
                break;

            case "regenerate":
                Enchantment regenerate = Enchantment.getByName("Regenerate");
                ie.enchantItem(regenerate, 1, stack, p);
                break;

            case "lumberjack":
                Enchantment lumberjack = Enchantment.getByName("Lumberjack");
                ie.enchantItem(lumberjack, 1, stack, p);
                break;

            case "arrowtrail":
                Enchantment arrowtrails = Enchantment.getByName("ArrowTrail");
                ie.enchantItem(arrowtrails, 1, stack, p);
                break;

            case "eview":
                ie.viewEnchants(p.getInventory().getItemInMainHand(), p);
                break;

            case "paintball":
                Enchantment paintball = Enchantment.getByName("Paintball");
                ie.enchantItem(paintball, 1, stack, p);
                break;

            case "backpack":
                if(d.getBackPack() != null) {
                    p.openInventory(d.getBackPack().getInv());
                    return;
                }
                BackPack backPack = new BackPack(BackPackSize.THIRTY_SIX);
                bpm.createBackPack(p, backPack);
                break;

            case "test":
                GemManager gemManager = DynamicManger.get(GemManager.class);
                gemManager.buildGem(p, new Gem(p.getName(), 150, System.currentTimeMillis()));
                break;

            //case "world":
            //    Bukkit.getServer().getScheduler().runTask(w, () -> {
            //        World world = Bukkit.createWorld(new WorldCreator("test").type(WorldType.FLAT).generator(new TestGenerator()));
            //        p.teleport(world.getSpawnLocation());
            //    });
            //    break;

            default: e.setCancelled(false);
        }
    }
}
