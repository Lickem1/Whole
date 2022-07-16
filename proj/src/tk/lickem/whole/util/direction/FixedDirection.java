package tk.lickem.whole.util.direction;

import org.bukkit.entity.Player;
import tk.lickem.whole.data.packet.ClassType;
import tk.lickem.whole.manager.dynamic.annotations.Init;

@Init(classType = ClassType.CONSTRUCT)
public class FixedDirection {

    public Direction getFixedDirection(Player player) {
        double rotation = (player.getEyeLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return Direction.WEST;
        } else if (22.5 <= rotation && rotation < 67.5) {
            return Direction.NORTH_WEST;
        } else if (67.5 <= rotation && rotation < 112.5) {
            return Direction.NORTH;
        } else if (112.5 <= rotation && rotation < 157.5) {
            return Direction.NORTH_EAST;
        } else if (157.5 <= rotation && rotation < 202.5) {
            return Direction.EAST;
        } else if (202.5 <= rotation && rotation < 247.5) {
            return Direction.SOUTH_EAST;
        } else if (247.5 <= rotation && rotation < 292.5) {
            return Direction.SOUTH;
        } else if (292.5 <= rotation && rotation < 337.5) {
            return Direction.SOUTH_WEST;
        } else if (337.5 <= rotation && rotation < 360.0) {
            return Direction.WEST;
        } else {
            return null;
        }
    }

    public enum Direction {
        NORTH,NORTH_EAST,NORTH_WEST,
        SOUTH, SOUTH_EAST,SOUTH_WEST,
        EAST,
        WEST
    }
}
