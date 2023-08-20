package codersafterdark.reskillable.advancement.trait;

import codersafterdark.reskillable.api.unlockable.Unlockable;
import codersafterdark.reskillable.lib.LibMisc;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;

public class UnlockUnlockableCriterionInstance extends AbstractCriterionTriggerInstance {
    private final Unlockable unlockable;

    public UnlockUnlockableCriterionInstance(Unlockable unlockable) {
        super(new ResourceLocation(LibMisc.MOD_ID, "unlockable"), ContextAwarePredicate.ANY);
        this.unlockable = unlockable;
    }

    public boolean test(Unlockable other) {
        return unlockable == other;
    }
}
