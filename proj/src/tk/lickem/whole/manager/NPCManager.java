package tk.lickem.whole.manager;

import lombok.Getter;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.entity.Player;
import tk.lickem.whole.data.npc.NPC;
import tk.lickem.whole.data.packet.PacketEvent;
import tk.lickem.whole.data.packet.PacketState;
import tk.lickem.whole.data.packet.ClassType;
import tk.lickem.whole.manager.dynamic.annotations.Init;

import java.util.LinkedList;
import java.util.List;

@Init(classType = ClassType.PACKET_LISTENER)
public class NPCManager {

    @Getter
    private final List<NPC> npcList = new LinkedList<>();

    public NPC getNPC(EntityPlayer entityPlayer) {
        for(tk.lickem.whole.data.npc.NPC npcs : npcList) {
            if(npcs.getNPC().getId() == entityPlayer.getId()) {
                return npcs;
            }
        }
        return null;
    }

    public NPC getNPC(int id) {
        for(tk.lickem.whole.data.npc.NPC npcs : npcList) {
            if(npcs.getNPC().getId() == id) {
                return npcs;
            }
        }
        return null;
    }


    @PacketEvent(packet = PacketPlayInUseEntity.class)
    public void listen(Player player, PacketState state, PacketPlayInUseEntity packet) {
        NPC npc = getNPC(packet.getEntityId());

        PacketPlayInUseEntity.EnumEntityUseAction action = packet.a();
        EnumHand hand = packet.b();

        if (npc != null && npc.hasEvent()) {
            if ((action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT && hand == EnumHand.MAIN_HAND) // apparently you can interact with entities using both hands ;D
                    || action == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                npc.getNpcEvent().event(npc, player);
            }
        }
    }
}
