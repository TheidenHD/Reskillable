package codersafterdark.reskillable.api.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ModLockKey extends NBTLockKey {
    private final String modName;

    public ModLockKey(String modName) {
        this(modName, null);
    }

    public ModLockKey(String modName, CompoundTag tag) {
        super(tag);
        this.modName = modName == null ? "" : modName.toLowerCase();
    }

    public ModLockKey(ItemStack stack) {
        super(stack.getTag());
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(stack.getItem());
        this.modName = registryName == null ? "" : registryName.getNamespace();
    }

    @Override
    public LockKey getNotFuzzy() {
        return isNotFuzzy() ? this : new ModLockKey(modName);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ModLockKey && getModName().equals(((ModLockKey) o).getModName())) {
            return getTag() == null ? ((ModLockKey) o).getTag() == null : getTag().equals(((ModLockKey) o).getTag());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(modName, tag);
    }

    public String getModName() {
        return modName;
    }
}
