package tk.lickem.whole.data.hologram;

import lombok.SneakyThrows;
import net.minecraft.server.v1_12_R1.*;
import tk.lickem.whole.manager.DynamicManger;
import tk.lickem.whole.manager.HologramManager;
import tk.lickem.whole.manager.dynamic.DynamicPacketListener;

@tk.lickem.whole.manager.dynamic.annotations.Packet
public class HologramPacketListener extends DynamicPacketListener {

    @Override
    @SneakyThrows
    public void handleReceivedPacket(PlayerConnection playerConnection, Packet packet) {
        if(packet instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity) packet;
            int id = packetPlayInUseEntity.getEntityId();
            HologramManager hm = DynamicManger.get(HologramManager.class);
            Hologram hologram = hm.isHologramEntity(id);

            if(hologram != null) {
                if(hologram.isEventable()) {
                    hologram.getHologramEvent().e(playerConnection.getPlayer().getPlayer());
                }
            }
        }

    }

    @Override
    public void handleSentPacket(PlayerConnection playerConnection, Packet packet) {
    }
}
