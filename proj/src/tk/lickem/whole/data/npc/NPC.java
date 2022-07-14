package tk.lickem.whole.data.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tk.lickem.whole.Whole;
import tk.lickem.whole.manager.DynamicManger;
import tk.lickem.whole.manager.NPCManager;

import java.util.*;

@Getter
public class NPC {

    private final Location location;
    private final EntityPlayer NPC;
    private final GameProfile npcProfile;
    @Setter
    private NPCEvent npcEvent;
    private final List<UUID> viewers = new ArrayList<>();

    public NPC(Location location) {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) location.getWorld()).getHandle();

        this.location = location;
        this.npcProfile = new GameProfile(UUID.randomUUID(), "$NPC_r" + new Random().nextInt(1000));
        this.NPC = new EntityPlayer(nmsServer, nmsWorld, this.npcProfile, new PlayerInteractManager(nmsWorld));
        DynamicManger.get(NPCManager.class).getNpcs().add(this);
    }

    public void spawn() {
        NPC.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        NPC.getDataWatcher().set(new DataWatcherObject<>(13, DataWatcherRegistry.a), (byte) 127); // fix skin layers
        update();
    }

    public boolean hasEvent() {
        return (npcEvent != null);
    }

    public void equip(EnumItemSlot itemSlot, ItemStack stack) {
        sendPacket(new PacketPlayOutEntityEquipment(NPC.getId(), itemSlot, CraftItemStack.asNMSCopy(stack)));
    }

    public void playAnimation(EnumAnimation animation) {
        sendPacket(new PacketPlayOutAnimation(NPC, animation.getId()));
    }

    public void update() {
        sendPacket(
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, NPC),
                new PacketPlayOutEntityDestroy(NPC.getId()),
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, NPC),
                new PacketPlayOutNamedEntitySpawn(NPC),
                new PacketPlayOutEntityHeadRotation(NPC, (byte) (NPC.yaw * 256 / 360))
        );
        Bukkit.getServer().getScheduler().runTaskLater(Whole.getWhole(), () -> sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, NPC)), 3);
    }

    public void addViewer(Player player) {
        viewers.add(player.getUniqueId());
    }

    public void setNamePlateVisibility(ScoreboardTeamBase.EnumNameTagVisibility enumNameTagVisibility) {
        ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), NPC.getName());

        team.setNameTagVisibility(enumNameTagVisibility);
        List<String> playerToAdd = new ArrayList<>();
        playerToAdd.add(NPC.getName());

        sendPacket(
                new PacketPlayOutScoreboardTeam(team, 1),
                new PacketPlayOutScoreboardTeam(team, 0),
                new PacketPlayOutScoreboardTeam(team, playerToAdd, 3)
        );
    }

    public void changeSkin(String value, String signature) {
        npcProfile.getProperties().put("textures", new Property("textures", value, signature));
        update();
    }

    public void sendPacket(Packet<?> packet) {
        viewers.removeIf(v -> Bukkit.getServer().getPlayer(v) == null);
        for (UUID uuid : viewers) {
            Player p = Bukkit.getServer().getPlayer(uuid);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void sendPacket(Packet<?>... packet) {
        viewers.removeIf(v -> Bukkit.getServer().getPlayer(v) == null);
        for (Packet<?> pack : packet) {
            for (UUID uuid : viewers) {
                Player p = Bukkit.getServer().getPlayer(uuid);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(pack);
            }
        }
    }

    @Getter
    @AllArgsConstructor
    public enum EnumAnimation {
        SWING_HAND(0),
        DAMAGE(1),
        LEAVE_BED(2),
        SWING_OFF_HAND(3),
        CRIT(4),
        MAGIC_CRIT(5);

        private final int id;
    }
}
