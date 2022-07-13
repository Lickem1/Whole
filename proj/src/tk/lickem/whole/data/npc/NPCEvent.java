package tk.lickem.whole.data.npc;

import org.bukkit.entity.Player;

public interface NPCEvent {

    void event(NPC npc, Player player);
}
