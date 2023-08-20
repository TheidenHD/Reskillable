package codersafterdark.reskillable.api.event;

import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

public class LockUnlockableEvent extends PlayerEvent {
    private Unlockable unlockable;

    protected LockUnlockableEvent(Player player, Unlockable unlockable) {
        super(player);
        this.unlockable = unlockable;
    }

    public Unlockable getUnlockable() {
        return unlockable;
    }

    @Cancelable
    public static class Pre extends LockUnlockableEvent {
        public Pre(Player player, Unlockable unlockable) {
            super(player, unlockable);
        }
    }

    public static class Post extends LockUnlockableEvent {
        public Post(Player player, Unlockable unlockable) {
            super(player, unlockable);
        }
    }
}