package tk.lickem.whole.data.backpack;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;

@Getter
public class BackPack {

    private BackPackSize size;
    @Setter private Inventory inv;

    public BackPack(BackPackSize size) {
        this.size = size;
    }
}
