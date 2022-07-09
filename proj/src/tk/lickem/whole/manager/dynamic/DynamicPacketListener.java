package tk.lickem.whole.manager.dynamic;

import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import tk.lickem.CodecSpigot;
import tk.lickem.handler.PacketHandler;

public abstract class DynamicPacketListener implements PacketHandler {

    public DynamicPacketListener() {
        CodecSpigot.addPacketHandler(this);
    }

    @Override
    public abstract void handleReceivedPacket(PlayerConnection playerConnection, Packet packet);

    @Override
    public abstract void handleSentPacket(PlayerConnection playerConnection, Packet packet);
}
