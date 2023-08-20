package codersafterdark.reskillable.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class CacheInvalidatedEvent extends Event {
    private Player player;
    private boolean modified;

    public CacheInvalidatedEvent(Player player) {
        this(player, false);
    }

    public CacheInvalidatedEvent(Player player, boolean modified) {
        this.player = player;
        this.modified = modified;
    }

    /**
     * @return The player who's cache was invalidated.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * @return True if any part of the cache was modified.
     */
    public boolean anyModified() {
        return modified;
    }
}