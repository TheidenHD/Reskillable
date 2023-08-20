package codersafterdark.reskillable.api.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class GenericNBTLockKey extends NBTLockKey {
    public GenericNBTLockKey(CompoundTag tag) {
        super(tag);
    }

    public GenericNBTLockKey(ItemStack stack) {
        this(stack.getTag());
    }

    @Override
    public LockKey getNotFuzzy() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof GenericNBTLockKey && (getTag() == null ? ((GenericNBTLockKey) o).getTag() == null : getTag().equals(((GenericNBTLockKey) o).getTag()));
    }

    @Override
    public int hashCode() {
        return tag == null ? super.hashCode() : tag;
    }
}