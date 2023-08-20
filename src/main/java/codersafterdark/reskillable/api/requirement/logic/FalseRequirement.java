package codersafterdark.reskillable.api.requirement.logic;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementComparision;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class FalseRequirement extends Requirement {
    public FalseRequirement() {
        this.tooltip = ChatFormatting.RED + Component.translatable("reskillable.requirements.format.unobtainable").getString();
    }

    @Override
    public boolean achievedByPlayer(ServerPlayer entityPlayerMP) {
        return false;
    }

    @Override
    public String getToolTip(PlayerData data) {
        return tooltip;
    }

    @Override
    public RequirementComparision matches(Requirement other) {
        return other instanceof FalseRequirement ? RequirementComparision.EQUAL_TO : RequirementComparision.NOT_EQUAL;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof FalseRequirement;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}