package codersafterdark.reskillable.api.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class ItemInfo extends NBTLockKey {
    private Item item;

    public ItemInfo(Item item) {
        this(item, null);
    }

    public ItemInfo(Item item, CompoundTag tag) {
        super(tag);
        this.item = item;
    }

    public ItemInfo(ItemStack stack) {
        this(stack.getItem(), stack.getTag());
    }

    @Override
    public LockKey getNotFuzzy() {
        return isNotFuzzy() ? this : new ItemInfo(item);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemInfo other)) {
            return false;
        }
        if (getItem() != other.getItem()) {
            return false;
        }
        if (getTag() == null) {
            return other.getTag() == null;
        }
        return getTag().equals(other.getTag());
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, tag);
    }

    public Item getItem() {
        return item;
    }
}
