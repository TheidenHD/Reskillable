package codersafterdark.reskillable.api;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.skill.SkillConfig;
import codersafterdark.reskillable.api.unlockable.UnlockableConfig;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.Level;

public interface IModAccess {
    SkillConfig getSkillConfig(ResourceLocation name);

    UnlockableConfig getUnlockableConfig(ResourceLocation name, int x, int y, int cost, String[] defaultRequirements);

    void syncPlayerData(Player entityPlayer, PlayerData playerData);

    AdvancementProgress getAdvancementProgress(Player entityPlayer, Advancement advancement);

    void log(Level warn, String s);
}