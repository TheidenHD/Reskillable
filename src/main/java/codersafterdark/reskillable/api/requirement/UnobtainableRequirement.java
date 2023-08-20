package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.data.PlayerData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

//Needs to be a separate object from FalseRequirement, so that it doesn't always get trimmed out of logic requirements
public class UnobtainableRequirement extends Requirement {
    public UnobtainableRequirement() {
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
        return other instanceof UnobtainableRequirement ? RequirementComparision.EQUAL_TO : RequirementComparision.NOT_EQUAL;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof UnobtainableRequirement;
    }

    @Override
    public int hashCode() {
        return -1;
    }
}