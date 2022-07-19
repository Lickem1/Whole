package tk.lickem.whole.data.tablist;

import com.mojang.authlib.GameProfile;
import com.sun.org.apache.bcel.internal.Const;
import lombok.SneakyThrows;
import net.minecraft.server.v1_12_R1.EnumGamemode;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_12_R1.ScoreboardTeamBase;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.*;

public class Tablist {

    // Apparently PlayerInfoData is a bad class file in 1.12?? :|
    // No need to worry! We have reflection!

    @SneakyThrows
    public Tablist(Player player) {

        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        Class<?> clazz = Class.forName("net.minecraft.server." + version + ".PacketPlayOutPlayerInfo$PlayerInfoData");
        Constructor<?> infoDataConstructor = clazz.getDeclaredConstructor(PacketPlayOutPlayerInfo.class, GameProfile.class, int.class, EnumGamemode.class, IChatBaseComponent.class);


        for (int i = 0; i < 80; i++) {
            GameProfile profile = new GameProfile(UUID.randomUUID(), "§" + ((char) i));
            PacketPlayOutPlayerInfo packetPlayOutPlayerInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
            List<Object> list = Collections.singletonList(
                    infoDataConstructor.newInstance(packetPlayOutPlayerInfo, profile, 0, EnumGamemode.CREATIVE, IChatBaseComponent.ChatSerializer.a(" "))
            );
            FieldUtils.writeField(packetPlayOutPlayerInfo, "b", list, true);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutPlayerInfo);
            setTeam(TeamsWrapper.Mode.CREATE, player, "!FTBL" + ((char) i), "§" + ((char) i), ("§" + ((char) i)) + "", "");

        }

        // just fix tablist slot formatting

        for(int i = 0; i<80;i++) {
            setSlot(player, i, "§bSlot #" + i, "");
        }
    }

    private void setTeam(TeamsWrapper.Mode mode, Player player, String team_id, String target, String prefix, String suffix) {

        if (prefix.length() > 16) prefix = prefix.substring(0, 16);
        if (suffix.length() > 16) suffix = suffix.substring(0, 16);

        TeamsWrapper wrapper = new TeamsWrapper();

        wrapper.setName(team_id);
        wrapper.setMode(mode);
        wrapper.setDisplayName(team_id);
        wrapper.setSuffix(suffix);
        wrapper.setPrefix(prefix);
        wrapper.setNameVisible(TeamsWrapper.NameVisible.ALWAYS);
        wrapper.setCollisionRule(ScoreboardTeamBase.EnumTeamPush.ALWAYS);
        wrapper.setFriendlyFire(true);
        wrapper.setColorCode(7);
        wrapper.setPlayers(Collections.singleton(target));

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(wrapper.getTeam());
    }

    public void setSlot(Player p, int slot, String prefix, String suffix) {
        setTeam(TeamsWrapper.Mode.UPDATE, p, "!FTBL" + ((char) slot), "§" + ((char) slot), ("§" + ((char) slot))  + prefix, suffix);

    }
}
