package tk.lickem.whole.manager;

import lombok.Getter;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.entity.Player;
import tk.lickem.whole.data.npc.NPC;
import tk.lickem.whole.manager.dynamic.DynamicPacketListener;

import java.util.LinkedList;
import java.util.List;

@tk.lickem.whole.manager.dynamic.annotations.Packet

public class NPCManager extends DynamicPacketListener {

    @Getter
    private List<NPC> npcs = new LinkedList<>();


    public NPC getNPC(EntityPlayer entityPlayer) {
        for(tk.lickem.whole.data.npc.NPC npcs : npcs) {
            if(npcs.getNPC().getId() == entityPlayer.getId()) {
                return npcs;
            }
        }
        return null;
    }

    public NPC getNPC(int id) {
        for(tk.lickem.whole.data.npc.NPC npcs : npcs) {
            if(npcs.getNPC().getId() == id) {
                return npcs;
            }
        }
        return null;
    }

    @Override
    public void handleReceivedPacket(PlayerConnection playerConnection, Packet packet) {
        if(packet instanceof PacketPlayInUseEntity) {
            Player p = playerConnection.getPlayer();
            PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity) packet;
            NPC npc = getNPC(packetPlayInUseEntity.getEntityId());

            PacketPlayInUseEntity.EnumEntityUseAction action = packetPlayInUseEntity.a();
            EnumHand hand = packetPlayInUseEntity.b();

            if(npc != null && npc.hasEvent()) {
                if((action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT && hand == EnumHand.MAIN_HAND) // apparently you can interact with entities using both hands ;D
                        || action == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                    npc.getNpcEvent().event(npc, p);
                }
            }

        }
    }

    @Override
    public void handleSentPacket(PlayerConnection playerConnection, Packet packet) {
    }
}
