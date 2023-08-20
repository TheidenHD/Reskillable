package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.RequirementHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class AdvancementRequirement extends Requirement {
    private ResourceLocation advancementName;

    public AdvancementRequirement(ResourceLocation advancementName) {
        this.advancementName = advancementName;

    }

    @Override
    public boolean achievedByPlayer(ServerPlayer entityPlayer) {
        return Optional.ofNullable(this.getAdvancement())
                .map(advancement -> ReskillableAPI.getInstance().getAdvancementProgress(entityPlayer, advancement))
                .map(AdvancementProgress::isDone)
                .orElse(false);
    }

    @Override
    public String getToolTip(PlayerData data) {
        if (tooltip.isEmpty()) {
            Advancement adv = getAdvancement();
            this.tooltip = ChatFormatting.GRAY + " - " + ChatFormatting.GOLD + Component.translatable("reskillable.requirements.format.advancement",
                    "%S", adv == null ? "" : adv.getChatComponent().getString().replaceAll("[\\[\\]]", "")).getString();
        }
        return super.getToolTip(data);
    }

    public Advancement getAdvancement() {
        return RequirementHolder.getAdvancementList().get(advancementName);
    }

    @Override
    public RequirementComparision matches(Requirement other) {
        return other instanceof AdvancementRequirement && advancementName.equals(((AdvancementRequirement) other).advancementName)
                ? RequirementComparision.EQUAL_TO : RequirementComparision.NOT_EQUAL;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof AdvancementRequirement && advancementName.equals(((AdvancementRequirement) o).advancementName);
    }

    @Override
    public int hashCode() {
        return advancementName.hashCode();
    }
}