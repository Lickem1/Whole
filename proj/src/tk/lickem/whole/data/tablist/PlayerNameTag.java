package tk.lickem.whole.data.tablist;

import lombok.SneakyThrows;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_12_R1.ScoreboardTeamBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import tk.lickem.whole.data.packet.ClassType;
import tk.lickem.whole.data.packet.PacketEvent;
import tk.lickem.whole.data.packet.PacketState;
import tk.lickem.whole.manager.dynamic.annotations.Init;

import java.lang.reflect.Field;
import java.util.Collections;

@Init(classType = ClassType.PACKET_LISTENER)
public class PlayerNameTag {

    @SneakyThrows
    @PacketEvent(packet = PacketPlayOutPlayerInfo.class)
    public void packet(Player player, PacketState state, PacketPlayOutPlayerInfo packet) {
        Field f = packet.getClass().getDeclaredField("a");
        f.setAccessible(true);
        PacketPlayOutPlayerInfo.EnumPlayerInfoAction e = (PacketPlayOutPlayerInfo.EnumPlayerInfoAction) f.get(packet);

        if(e != PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER && e != PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER) return;

        for(PacketPlayOutPlayerInfo.PlayerInfoData profile : packet.getB()) {
            Player p = Bukkit.getServer().getPlayer(profile.a().getId());

            String prefix = "";
            if(p == null) continue;
            if(p.getName().equalsIgnoreCase("Lickem")) prefix = "&c";
            else prefix = "&7[MEMBER] &f";

            setTeam(player, p.getName(), p.getName(), prefix, "");
        }
    }

    private void setTeam(Player player, String team_id, String target, String prefix, String suffix) {
        remove(player, team_id);
        if (prefix.length() > 16) prefix = prefix.substring(0,16);
        if (suffix.length() > 16) suffix = suffix.substring(0,16);

        TeamsWrapper wrapper = new TeamsWrapper();

        wrapper.setName(team_id);
        wrapper.setMode(TeamsWrapper.Mode.CREATE);
        wrapper.setDisplayName(team_id);
        wrapper.setSuffix(ChatColor.translateAlternateColorCodes('&', suffix));
        wrapper.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix));
        wrapper.setNameVisible(TeamsWrapper.NameVisible.ALWAYS);
        wrapper.setCollisionRule(ScoreboardTeamBase.EnumTeamPush.ALWAYS);
        wrapper.setFriendlyFire(true);
        wrapper.setColorCode(7);
        wrapper.setPlayers(Collections.singleton(target));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(wrapper.getTeam());
    }

    private void remove(Player player, String other) {
        TeamsWrapper wrapper = new TeamsWrapper();
        wrapper.setName(other);
        wrapper.setMode(TeamsWrapper.Mode.REMOVE);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(wrapper.getTeam());
    }

}
