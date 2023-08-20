package codersafterdark.reskillable.api.requirement.logic;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementCache;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class DoubleRequirement extends Requirement implements OuterRequirement {
    private final Requirement left, right;

    protected DoubleRequirement(Requirement left, Requirement right) {
        this.left = left;
        this.right = right;
    }

    public Requirement getLeft() {
        return this.left;
    }

    public Requirement getRight() {
        return this.right;
    }

    protected abstract String getFormat();

    protected boolean leftAchieved(Player player) {
        return RequirementCache.requirementAchieved(player, getLeft());
    }

    protected boolean rightAchieved(Player player) {
        return RequirementCache.requirementAchieved(player, getRight());
    }

    @Override
    public String getToolTip(PlayerData data) {
        ChatFormatting color = data == null || !data.requirementAchieved(this) ? ChatFormatting.RED : ChatFormatting.GREEN;
        return ChatFormatting.GRAY + " - " + getToolTipPart(data, getLeft()) + ' ' + color + getFormat() + ' ' + getToolTipPart(data, getRight());
    }

    private String getToolTipPart(PlayerData data, Requirement side) {
        String tooltip = side.getToolTip(data);
        if (tooltip != null && tooltip.startsWith(ChatFormatting.GRAY + " - ")) {
            tooltip = tooltip.replaceFirst(ChatFormatting.GRAY + " - ", "");
        }
        if (side instanceof DoubleRequirement) {
            tooltip = ChatFormatting.GOLD + "(" + ChatFormatting.RESET + tooltip + ChatFormatting.GOLD + ')';
        } else {
            //Ensure that no color leaks
            tooltip = ChatFormatting.RESET + tooltip;
        }
        return tooltip;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof DoubleRequirement) {//The order of the logic requirements does not actually matter so might as well put it here
            DoubleRequirement dreq = (DoubleRequirement) o;
            return (getRight().equals(dreq.getRight()) && getLeft().equals(dreq.getLeft())) || (getRight().equals(dreq.getLeft()) && getLeft().equals(dreq.getRight()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        //Ensure there is no out of bounds errors AND that the hashcode is the same even if left and right got reversed
        long leftHash = getLeft().hashCode();
        long rightHash = getRight().hashCode();
        return (int) ((leftHash + rightHash) / 2);
    }

    @Nonnull
    @Override
    public List<Class<? extends Requirement>> getInternalTypes() {
        List<Class<? extends Requirement>> types = new ArrayList<>();
        Requirement lReq = getLeft();
        Requirement rReq = getRight();
        if (lReq instanceof OuterRequirement) {
            types.addAll(((OuterRequirement) lReq).getInternalTypes());
        } else {
            types.add(lReq.getClass());
        }
        if (rReq instanceof OuterRequirement) {
            types.addAll(((OuterRequirement) rReq).getInternalTypes());
        } else {
            types.add(rReq.getClass());
        }
        return types;
    }

    @Override
    public boolean isCacheable() {
        return getLeft().isCacheable() && getRight().isCacheable();
    }
}