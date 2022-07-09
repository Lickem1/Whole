package tk.lickem.whole.data.armorstand;

import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.entity.Player;

public class CustomArmorStand extends EntityArmorStand {

    private Player player;

    public CustomArmorStand(World world, Player p) {
        super(world);
        this.player = p;
    }

    @Override
    public void n() {
        System.out.println("Test");
    }
}
