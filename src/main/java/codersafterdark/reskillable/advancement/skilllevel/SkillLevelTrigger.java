package codersafterdark.reskillable.advancement.skilllevel;

import codersafterdark.reskillable.advancement.AbstractCriterionTrigger;
import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.lib.LibMisc;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.realmsclient.util.JsonUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SkillLevelTrigger extends AbstractCriterionTrigger<SkillLevelListeners, SkillLevelCriterionInstance> {
    public SkillLevelTrigger() {
        super(new ResourceLocation(LibMisc.MOD_ID, "skill_level"), SkillLevelListeners::new);
    }

    public void trigger(ServerPlayer player, Skill skill, int level) {
        SkillLevelListeners listeners = this.getListeners(player.getAdvancements());
        if (listeners != null) {
            listeners.trigger(skill, level);
        }
    }

    @Override
    public SkillLevelCriterionInstance createInstance(JsonObject json, DeserializationContext context) {
        int level = JsonUtils.getIntOr("level", json, 0);
        if (json.has("skill")) {
            ResourceLocation skillName = new ResourceLocation(JsonUtils.getRequiredString("skill", json));
            Skill skill = ReskillableRegistries.SKILLS.getValue(skillName);
            if (skill != null) {
                return new SkillLevelCriterionInstance(skill, level);
            }
            throw new JsonParseException("Failed to find Matching Skill for Name: " + skillName);
        }
        return new SkillLevelCriterionInstance(null, level);
    }
}
