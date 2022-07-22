package tk.lickem.whole.listener;

import lombok.SneakyThrows;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
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
import tk.lickem.whole.data.hologram.Hologram;
import tk.lickem.whole.data.npc.NPC;
import tk.lickem.whole.data.player.PlayerData;
import tk.lickem.whole.data.tablist.Tablist;
import tk.lickem.whole.manager.DynamicManger;
import tk.lickem.whole.data.packet.ClassType;
import tk.lickem.whole.manager.dynamic.DynamicListener;
import tk.lickem.whole.manager.dynamic.annotations.Init;
import tk.lickem.whole.util.itembuilder.ItemBuilder;

@Init(classType = ClassType.CONSTRUCT)
public class ChatEvent extends DynamicListener {

    @EventHandler
    @SneakyThrows
    public void chat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        PlayerData d = PlayerData.valueOf(p);
        ItemStack stack = p.getInventory().getItemInMainHand();

        ItemEnchanter ie = DynamicManger.get(ItemEnchanter.class);
        BackPackManager bpm = DynamicManger.get(BackPackManager.class);

        e.setCancelled(true);
        switch (e.getMessage().toLowerCase()) {

            case "messager":
                Enchantment messager = Enchantment.getByName("Messager");

                if (ie.hasEnchant(messager, p.getInventory().getItemInMainHand())) {
                    int o = ie.getEnchantLevel(messager, stack);
                    ie.enchantItem(messager, o + 1, stack, p);
                } else ie.enchantItem(messager, 1, stack, p);
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
                if (d.getBackPack() != null) {
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

            case "tablist":
                new Tablist(p);
                break;

            case "npc":
                NPC npc = new NPC(p.getLocation());
                Hologram hologram = new Hologram(npc.getNPC().getName(), npc.getLocation().clone().add(0, 2, 0),
                        "&eLine 1!",
                        "&eLine 2!",
                        "&eLine 3!",
                        "&eLine 4!",
                        "&eLine 5!");
                hologram.showToPlayer(p);
                npc.addViewer(p);

                npc.spawn();
                npc.changeSkin(
                        "ewogICJ0aW1lc3RhbXAiIDogMTY1NzY2ODI1MDkyMywKICAicHJvZmlsZUlkIiA6ICJiMmIxNzk1M2VjZTA0ZjFjYjUxMjVjYjEwODVmZGY5ZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJiY2kiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjk2OGVhYmFjMmMyZTFmMDRiODQzNDQxZTRmZjhjOGUzNTQzMDFiM2JhMDVkYzlmOGMyZmJlN2E1ZTgzNWVkMCIKICAgIH0KICB9Cn0=",
                        "SVmoBWgaujDBxsd0uYfBQ9piSgDy11c8QYZvNiBJwwIpwUHyrQ61hY3mvv1etibvIqBnSONJdAdxV1d8oua64mTZ3Ya8xk/7EXuLTunGodf8JNEQALsZpkERPWf8GzROS/K9tKVAGa9tN706wGD2/dj5Pd3fv38BbnU0SUBPvDI6wNsz+4FseGJDhW3DCM7/1o7JIPjGAoEnG+AJeqOR7tEEJPsKr9yto6sHQwqXJ/QAzb8ZWJ9iVZm/CS2KKatmC40FKML4VHVm5wRfbi3agvPWqS4PHEnkHyACGo0DksOUyZ/TSKBbMWJ13/NG6lD2GTXUfH4uwS9KDvcmLQAU93IJEArKNjpypoPJ6de4iecmdCGhxC1L7TBRw9M8uxwNAJc0g7oC4LdTTbSm/9mNGi1tuREZJuHIF03AB70rTb5dLuecJ3k49lJNvJFv7+5tHR6RcM3XowVbuvgiAfLwI3Tx9gHiWX+C41xhYS5ICj+zPMEFNNdyqEeVrn3ykdYaXV0B/9fnEL3S9BwQG942FgxBGJ7HLvP079dK+FvlEPh7FXELS2IM+Zqzgf4gIeqkx0iGgETpKaqdLvjPhsX1xdFHlToqFZm4rromNw7lPO604gsGAEZBsbT992pCQZpVqiBQbZllTWu5sGL8UqYULkf2mmOXW7SQSjOdr8Z3beo="
                );
                npc.equip(EnumItemSlot.MAINHAND, new ItemBuilder(Material.EMERALD_BLOCK, 1, 0).build());
                npc.setNamePlateVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
                npc.setNpcEvent((n, p1) -> {
                    p1.sendMessage("Back off nerd >:(");
                    n.playAnimation(NPC.EnumAnimation.DAMAGE);

                    // this is so cool wtf
                    EntityPlayer entityPlayer = ((CraftPlayer) p1).getHandle();
                    PacketPlayOutGameStateChange status = new PacketPlayOutGameStateChange(4, 1);
                    entityPlayer.playerConnection.sendPacket(status);
                });
                break;

            default:
                e.setCancelled(false);
        }
    }
}
