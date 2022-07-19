package tk.lickem.whole.data.tablist;

import lombok.SneakyThrows;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import org.bukkit.entity.Player;
import tk.lickem.whole.data.packet.ClassType;
import tk.lickem.whole.data.packet.PacketEvent;
import tk.lickem.whole.data.packet.PacketState;
import tk.lickem.whole.manager.dynamic.annotations.Init;


@Init(classType = ClassType.PACKET_LISTENER)
public class PlayerNameTag {

    @SneakyThrows
    @PacketEvent(packet = PacketPlayOutPlayerInfo.class)
    public void packet(Player player, PacketState state, PacketPlayOutPlayerInfo packet) {

        // todo

    }
}
