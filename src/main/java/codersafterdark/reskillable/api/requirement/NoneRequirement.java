package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.data.PlayerData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

//This has the same body as a TrueRequirement, except that it should not simplify out
public final class NoneRequirement extends Requirement {
    public NoneRequirement() {
        this.tooltip = ChatFormatting.GREEN + Component.translatable("reskillable.requirements.format.unobtainable").getString();
    }

    @Override
    public boolean achievedByPlayer(ServerPlayer entityPlayerMP) {
        return true;
    }

    @Override
    public String getToolTip(PlayerData data) {
        return tooltip;
    }

    @Override
    public RequirementComparision matches(Requirement other) {
        return other instanceof NoneRequirement ? RequirementComparision.EQUAL_TO : RequirementComparision.NOT_EQUAL;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof NoneRequirement;
    }

    @Override
    public int hashCode() {
        //Does not actually matter but might as well have it be the same for each none requirement
        return 2;
    }
}