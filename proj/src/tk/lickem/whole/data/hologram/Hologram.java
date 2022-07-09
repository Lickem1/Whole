package tk.lickem.whole.data.hologram;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class Hologram {
    // Find a way to add new lines to holograms
    @Getter
    private static final List<Hologram> holograms = new LinkedList<>();

    private final UUID hologramID = UUID.randomUUID();
    private final String name;
    private final Location location;
    private Map<Integer, EntityArmorStand> armorStands;

    private final LinkedList<String> lines = new LinkedList<>();
    private final List<UUID> viewers = new ArrayList<>();
    private final LinkedList<EntityArmorStand> toUpdate = new LinkedList<>();
    @Setter
    private IHologramEvent hologramEvent;

    private double nextSpace = 0.0;

    public Hologram(String name, Location location) {
        this.armorStands = new HashMap<>();
        this.name = name;
        this.location = location;
        holograms.add(this);
    }


    public void addLine(String text) {
        double y = location.getY();
        int standID = armorStands.size();
        EntityArmorStand stand = newStand();
        stand.setLocation(location.getX(), (y-nextSpace), location.getZ(), location.getYaw(), location.getPitch());
        stand.setCustomName(ChatColor.translateAlternateColorCodes('&', text));
        armorStands.put(standID, stand);
        toUpdate.add(stand);
        nextSpace += .3;
    }

    public void addLine(String... text) {
        double y = location.getY();
        int standID = armorStands.size();

        for(String s : text) {
            EntityArmorStand stand = newStand();
            stand.setLocation(location.getX(), (y-nextSpace), location.getZ(), location.getYaw(), location.getPitch());
            stand.setCustomName(ChatColor.translateAlternateColorCodes('&', s));
            armorStands.put(standID, stand);
            toUpdate.add(stand);
            nextSpace += .3;
            standID++;
        }
    }

    public void setLine(int line, String text) {
        EntityArmorStand armorStand = armorStands.get(line - 1);
        armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', text));
        toUpdate.add(armorStand);
    }

    public String getLine(int line) {
        return armorStands.get(line - 1).getCustomName();
    }

    public boolean removeLine(int line) {
        try {
            EntityArmorStand armorStand = armorStands.remove(line-1);
            PacketPlayOutEntityDestroy d = new PacketPlayOutEntityDestroy(armorStand.getId());
            for (UUID uuid : viewers) {
                Player p = Bukkit.getServer().getPlayer(uuid);
                EntityPlayer viewer = ((CraftPlayer) p).getHandle();
                viewer.playerConnection.sendPacket(d);
            }
            sortList();
            tryRefresh();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void showToPlayer(Player player) {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        for (EntityArmorStand stand : armorStands.values()) {
            PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving(stand);
            PacketPlayOutEntityMetadata standMeta = new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true);

            ep.playerConnection.sendPacket(spawn);
            ep.playerConnection.sendPacket(standMeta);
        }
        viewers.add(player.getUniqueId());
    }

    public void make() {

        int standID = armorStands.size();

        if(lines.isEmpty()) {
            lines.addLast("This is the default hologram line!");
        }

        for (String line : lines) {
            EntityArmorStand stand = newStand();
            stand.setLocation(location.getX(), (location.getY()-nextSpace), location.getZ(), location.getYaw(), location.getPitch());
            stand.setCustomName(ChatColor.translateAlternateColorCodes('&', line));
            armorStands.put(standID, stand);
            standID++;
            nextSpace += .3;
        }
    }


    public void tryRefresh() {
        viewers.removeIf(uuid -> Bukkit.getServer().getPlayer(uuid) == null);
        if (!toUpdate.isEmpty()) { // makes it so it only updates what needs to be updated
            Iterator<EntityArmorStand> iterator = toUpdate.iterator();

            while (iterator.hasNext()) {
                EntityArmorStand armorStand = iterator.next();
                PacketPlayOutEntityDestroy d = new PacketPlayOutEntityDestroy(armorStand.getId());
                PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving(armorStand);
                PacketPlayOutEntityMetadata standMeta = new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true);


                for (UUID uuid : viewers) {
                    Player p = Bukkit.getServer().getPlayer(uuid);
                    EntityPlayer viewer = ((CraftPlayer) p).getHandle();
                    viewer.playerConnection.sendPacket(d);
                    viewer.playerConnection.sendPacket(spawn);
                    viewer.playerConnection.sendPacket(standMeta);
                }
                iterator.remove();
            }
        }
    }

    public boolean isVisibleTo(UUID uuid) {
        return (viewers.contains(uuid));
    }

    public boolean isVisibleTo(Player player) {
        return (viewers.contains(player.getUniqueId()));
    }

    private void sortList() {
        Map<Integer, EntityArmorStand> newMap = new HashMap<>();
        int newID = 0;
        double newSpace = 0.0;
        for(EntityArmorStand stand : armorStands.values()) {
            stand.setLocation(location.getX(), (location.getY()-newSpace), location.getZ(), location.getYaw(), location.getPitch());
            newMap.put(newID, stand);
            toUpdate.add(stand);
            newSpace += .3;
            newID++;
        }
        armorStands = newMap;
        nextSpace = newSpace;
    }

    private EntityArmorStand newStand() {
        EntityArmorStand stand = new EntityArmorStand(((CraftWorld) location.getWorld()).getHandle());
        stand.setCustomNameVisible(true);
        stand.setBasePlate(true);
        stand.setInvisible(true);
        stand.setArms(true);
        stand.setSmall(true);
        return stand;

    }

    public boolean isEventable() {
        return (hologramEvent != null);
    }

    public static Hologram getHologram(String id) {
        for (Hologram hologram : holograms) if (hologram.getName().equalsIgnoreCase(id)) return hologram;
        return null;
    }

    public static Hologram isHologramEntity(int id) {
        for (Hologram hologram : holograms) {
            for(EntityArmorStand armorStand : hologram.getArmorStands().values()) {
                if(armorStand.getId() == id) return hologram;
            }
        }
        return null;
    }
}
