package tk.lickem.whole.data.hologram;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import tk.lickem.whole.data.packet.PacketEvent;
import tk.lickem.whole.data.packet.PacketState;
import tk.lickem.whole.manager.DynamicManger;
import tk.lickem.whole.manager.HologramManager;
import tk.lickem.whole.data.packet.ClassType;
import tk.lickem.whole.manager.dynamic.annotations.Init;

@Init(classType = ClassType.PACKET_LISTENER)
public class HologramPacketListener {

    @PacketEvent(packet = PacketPlayInUseEntity.class)
    public void listen(Player player, PacketState state, PacketPlayInUseEntity packet) {
        PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;
        int id = packet.getEntityId();
        HologramManager hm = DynamicManger.get(HologramManager.class);
        Hologram hologram = hm.isHologramEntity(id);

        if(hologram != null) {
            if(hologram.isEventable()) {
                hologram.getHologramEvent().e(playerConnection.getPlayer().getPlayer());
            }
        }
    }
}
