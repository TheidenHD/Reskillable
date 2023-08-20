package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class TraitRequirement extends Requirement {
    private Unlockable unlockable;

    public TraitRequirement(Unlockable unlockable) {
        this.unlockable = unlockable;
        this.tooltip = ChatFormatting.GRAY + " - " + ChatFormatting.LIGHT_PURPLE + Component.translatable("reskillable.requirements.format.trait", "%s",
                this.unlockable.getName()).getString();
    }

    @Override
    public boolean achievedByPlayer(ServerPlayer entityPlayer) {
        return PlayerDataHandler.get(entityPlayer).getSkillInfo(unlockable.getParentSkill()).isUnlocked(unlockable);
    }

    public Skill getSkill() {
        return unlockable.getParentSkill();
    }

    public Unlockable getUnlockable() {
        return unlockable;
    }

    @Override
    public RequirementComparision matches(Requirement other) {
        return other instanceof TraitRequirement ? unlockable.getKey().equals(((TraitRequirement) other).unlockable.getKey()) ?
                RequirementComparision.EQUAL_TO : RequirementComparision.NOT_EQUAL : RequirementComparision.NOT_EQUAL;
    }

    @Override
    public boolean isEnabled() {
        return unlockable != null && unlockable.isEnabled();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof TraitRequirement && unlockable.equals(((TraitRequirement) o).unlockable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unlockable);
    }
}