package codersafterdark.reskillable.api.data;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.requirement.NoneRequirement;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementComparision;
import codersafterdark.reskillable.api.requirement.logic.TrueRequirement;
import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.lib.LibObfuscation;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.util.ArrayList;
import java.util.List;

public class RequirementHolder {
    private static AdvancementList advList;
    private final List<Requirement> requirements;
    private final boolean forcedEmpty;
    private boolean hasNone;

    public RequirementHolder() {
        this.requirements = Lists.newArrayList();
        this.forcedEmpty = true;
    }

    public RequirementHolder(List<Requirement> requirements) {
        this.requirements = requirements;
        this.forcedEmpty = false;
    }

    public RequirementHolder(RequirementHolder... others) {
        this.requirements = Lists.newArrayList();
        this.forcedEmpty = false;
        for (RequirementHolder other : others) {
            if (other.hasNone) {
                this.requirements.addAll(other.requirements);
                hasNone = true;
                break;
            }
            other.requirements.forEach(otherRequirement -> addRequirement(this.requirements, otherRequirement));
        }
    }

    public static RequirementHolder noneHolder() {
        RequirementHolder requirementHolder = new RequirementHolder(new ArrayList<>());
        requirementHolder.hasNone = true;
        requirementHolder.requirements.add(new NoneRequirement());
        return requirementHolder;
    }

    public static RequirementHolder realEmpty() {
        return new RequirementHolder();
    }

    public static RequirementHolder fromStringList(String[] requirementStringList) {
        //TODO If length is 1 try splitting the string. Instead it is probably better to follow the TODO below for deprecating single string requirement lists
        List<Requirement> requirements = new ArrayList<>();
        for (String s : requirementStringList) {
            Requirement requirement = ReskillableAPI.getInstance().getRequirementRegistry().getRequirement(s);
            if (requirement instanceof NoneRequirement) {
                return noneHolder();
            }
            addRequirement(requirements, requirement);
        }
        return requirements.isEmpty() ? RequirementHolder.realEmpty() : new RequirementHolder(requirements);
    }

    //TODO: Rewrite config system to store things in a format closer to JSON so that requirements are as a list which would remove the need for this
    //TODO Cont: This would make sure that there are no issues storing things like ItemRequirements if they have NBT data with commas in it
    public static RequirementHolder fromString(String s) {
        RequirementHolder requirementHolder;
        if (s.matches("(?i)^(null|nil)$")) {
            requirementHolder = RequirementHolder.realEmpty();
        } else {
            requirementHolder = fromStringList(s.split(","));
        }

        return requirementHolder;
    }

    private static void addRequirement(List<Requirement> requirements, Requirement requirement) {
        if (requirement == null || requirement instanceof TrueRequirement) {
            return;
        }
        for (int i = 0; i < requirements.size(); i++) {
            RequirementComparision match = requirements.get(i).matches(requirement);
            if (match.equals(RequirementComparision.EQUAL_TO) || match.equals(RequirementComparision.GREATER_THAN)) {
                return;
            } else if (match.equals(RequirementComparision.LESS_THAN)) {
                requirements.remove(i);
                break;
            }
        }
        requirements.add(requirement);
    }

    public static AdvancementList getAdvancementList() {
        if (advList == null) {
            advList = ObfuscationReflectionHelper.getPrivateValue(ServerAdvancementManager.class, null, LibObfuscation.ADVANCEMENT_LIST[1]);
        }
        return advList;
    }

    public boolean isRealLock() {
        return getRestrictionLength() > 0 && !forcedEmpty;
    }

    public boolean isForcedEmpty() {
        return forcedEmpty;
    }

    public int getRestrictionLength() {
        return requirements.size();
    }

    public void addRequirementsToTooltip(PlayerData data, List<String> tooltip) {
        if (!isRealLock()) {
            return;
        }
        if (!ConfigHandler.hideRequirements || Screen.hasShiftDown()) {
            tooltip.add(ChatFormatting.DARK_PURPLE + Component.translatable("reskillable.misc.requirements").getString());
            addRequirementsIgnoreShift(data, tooltip);
        } else {
            tooltip.add(ChatFormatting.DARK_PURPLE + Component.translatable("reskillable.misc.requirements_shift").getString());
        }
    }

    public void addRequirementsIgnoreShift(PlayerData data, List<String> tooltip) {
        if (isRealLock()) {
            requirements.stream().map(requirement -> requirement.getToolTip(data)).forEach(tooltip::add);
        }
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public boolean hasNone() {
        return hasNone;
    }
}