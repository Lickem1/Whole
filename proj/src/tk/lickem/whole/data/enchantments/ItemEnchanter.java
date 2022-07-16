package tk.lickem.whole.data.enchantments;

import lombok.var;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tk.lickem.whole.data.packet.ClassType;
import tk.lickem.whole.manager.dynamic.annotations.Init;
import tk.lickem.whole.util.RomanNum;

import java.util.ArrayList;
import java.util.List;

@Init(classType = ClassType.CONSTRUCT)
public class ItemEnchanter {

    public void viewEnchants(ItemStack stack, Player p) {
        p.sendMessage("Enchantments:");
        String format = "%s %s";
        for(var e : stack.getEnchantments().entrySet()) {
            if(e.getKey().getMaxLevel() == e.getValue()) p.sendMessage(String.format(format + " - MAXED", e.getKey().getName(), e.getValue()));
            else p.sendMessage(String.format(format, e.getKey().getName(), e.getValue()));
        }
    }

    public boolean hasEnchant(Enchantment enchantment, ItemStack stack) {
        return stack.containsEnchantment(enchantment);
    }

    public int getEnchantLevel(Enchantment enchantment, ItemStack stack) {
        for(var a : stack.getEnchantments().entrySet()) {
            if(a.getKey() == enchantment) return a.getValue();
        }
        return 0;
    }

    public void enchantItem(Enchantment enchantment, int level, ItemStack stack, Player p) {

        if(enchantment.canEnchantItem(stack)) {
            if(enchantment.getMaxLevel() != stack.getEnchantmentLevel(enchantment)) {
                stack.addUnsafeEnchantment(enchantment, level);
                buildLore(stack, p);
                p.updateInventory();
                p.sendMessage("Enchantment " + enchantment.getName() + " "+ level + " has been added to your item");

            } else p.sendMessage("This enchantment is already maxed out!");
        } else p.sendMessage("This enchantment only works on " + enchantment.getItemTarget());

    }

    private void buildLore(ItemStack stack, Player p) {
        List<String> lore = new ArrayList<>();
        ItemMeta meta = stack.getItemMeta();

        for(var e : stack.getEnchantments().entrySet()) {
            if(e.getKey() instanceof AbstractEnchant) {
                String romanNum = RomanNum.convertTo(e.getValue());
                String a = String.format(((AbstractEnchant) e.getKey()).getLore(), romanNum);
                lore.add(a);
            }
        }
        meta.setLore(lore);
        stack.setItemMeta(meta);
    }
}
