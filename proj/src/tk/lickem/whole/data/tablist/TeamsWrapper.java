package tk.lickem.whole.data.tablist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_12_R1.ScoreboardTeamBase;

import java.lang.reflect.Field;
import java.util.Collection;


public class TeamsWrapper {

    @Getter private final PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam();

    private final Field a = getField(PacketPlayOutScoreboardTeam.class, "a");
    private final Field b = getField(PacketPlayOutScoreboardTeam.class, "b");
    private final Field c = getField(PacketPlayOutScoreboardTeam.class, "c");
    private final Field d = getField(PacketPlayOutScoreboardTeam.class, "d");
    private final Field e = getField(PacketPlayOutScoreboardTeam.class, "e");
    private final Field f = getField(PacketPlayOutScoreboardTeam.class, "f");
    private final Field g = getField(PacketPlayOutScoreboardTeam.class, "g");
    private final Field h = getField(PacketPlayOutScoreboardTeam.class, "h");
    private final Field i = getField(PacketPlayOutScoreboardTeam.class, "i");
    private final Field j = getField(PacketPlayOutScoreboardTeam.class, "j");

    @SneakyThrows
    public void setName(String name) {
        a.set(team, name);
    }

    @SneakyThrows
    public void setDisplayName(String name) {
        b.set(team, name);
    }

    @SneakyThrows
    public void setPrefix(String pref) {
        c.set(team, pref);
    }

    @SneakyThrows
    public void setSuffix(String suff) {
        d.set(team, suff);
    }

    @SneakyThrows
    public void setNameVisible(NameVisible visible) {
        e.set(team, visible.name());
    }

    @SneakyThrows
    public void setCollisionRule(ScoreboardTeamBase.EnumTeamPush collision) {
        f.set(team, collision.e);
    }

    @SneakyThrows
    public void setColorCode(int packdata) {
        g.setInt(team, packdata);
    }

    @SneakyThrows
    public void setPlayers(Collection<String> n) {
        h.set(team, n);
    }

    @SneakyThrows
    public void setMode(Mode mode) {
        i.setInt(team, mode.mode);
    }

    @SneakyThrows
    public void setFriendlyFire(boolean idk) {
        j.setInt(team, idk ? 3 : 0);
    }

    public enum NameVisible {
        ALWAYS, hideForOtherTeams, hideForOwnTeam, never;
    }

    @AllArgsConstructor
    public enum Mode {
        CREATE(0),
        REMOVE(1),
        UPDATE(2),
        ADD_PLAYER(3),
        REMOVE_PLAYER(4);

        private final int mode;
    }


    @SneakyThrows
    private Field getField(Class<?> c, String name) {
        try {
            Field f = c.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            if (c != Object.class) {
                final Class _super = c.getSuperclass();
                return getField(_super, name);
            }
            throw e;
        }

    }
}
