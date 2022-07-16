package tk.lickem.whole.data.packet;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import tk.lickem.whole.manager.DynamicManger;

import java.util.List;

@AllArgsConstructor
public class PacketHandler extends ChannelDuplexHandler {

    private final Player player;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
        if(!handle(packet)) super.channelRead(ctx, packet);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
        if(!handle(packet)) super.write(ctx, packet, promise);
    }

    private boolean handle(Object packet) {
        PacketState state = new PacketState();
        if(state.isCancelled()) return false;
        List<PacketEventReference> list = DynamicManger.getPacketReference(packet.getClass());
        if(list == null) return false;
        for(PacketEventReference reference : list) {
            reference.call(player, state, packet);
        }

        return state.isCancelled();
    }
}
