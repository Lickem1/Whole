package tk.lickem.whole.data.enchantments.data.paintball;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

@Getter
@AllArgsConstructor
public class PaintballData {

    @Setter private int time;
    private final World blockWorld;
    private final Material originalBlock;
    private final byte originalBlockByte;
    private final Location originalBlockLocation;
}
