package tk.lickem.whole.data.enchantments.data.regenerate;

import lombok.*;
import org.bukkit.Location;
import org.bukkit.Material;

@Getter
@Setter
public class RBlockData {

    private int time;
    private Material originalBlock;
    private Location originalBlockLocation;

    @Setter(AccessLevel.NONE)
    private Material[] blocks = {
            Material.SOUL_SAND,
            Material.STONE,
            Material.COBBLESTONE,
            Material.SMOOTH_BRICK,
            Material.BRICK,
            Material.NETHER_BRICK,
            Material.BEDROCK
    };
}
