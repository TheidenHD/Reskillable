package codersafterdark.reskillable.api.data;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementCache;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Ability;
import codersafterdark.reskillable.api.unlockable.IAbilityEventHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Consumer;

public class PlayerData {
    private static final String TAG_SKILLS_CMP = "SkillLevels";
    private final boolean client;
    private final RequirementCache requirementCache;
    public WeakReference<Player> playerWR;
    private Map<Skill, PlayerSkillInfo> skillInfo = new HashMap<>();

    public PlayerData(Player player) {
        playerWR = new WeakReference<>(player);
        client = player.level().isClientSide();
        requirementCache = RequirementCache.getCache(player);

        ReskillableRegistries.SKILLS.getValues().forEach(s -> skillInfo.put(s, new PlayerSkillInfo(s)));

        load();
    }

    public PlayerSkillInfo getSkillInfo(Skill s) {
        return skillInfo.get(s);
    }

    public Collection<PlayerSkillInfo> getAllSkillInfo() {
        return skillInfo.values();
    }

    public boolean hasAnyAbilities() {
        return !getAllAbilities().isEmpty();
    }

    public Set<Ability> getAllAbilities() {
        Set<Ability> set = new TreeSet<>();
        skillInfo.values().forEach(info -> info.addAbilities(set));
        return set;
    }

    public boolean matchStats(RequirementHolder holder) {
        return playerWR.get() == null || holder.getRequirements().stream().allMatch(this::requirementAchieved);
    }

    //helper method to access the requirement cache
    public boolean requirementAchieved(Requirement requirement) {
        return getRequirementCache().requirementAchieved(requirement);
    }

    public final RequirementCache getRequirementCache() {
        return requirementCache;
    }

    public void load() {
        if (!client) {
            Player player = playerWR.get();

            if (player != null) {
                CompoundTag cmp = PlayerDataHandler.getDataCompoundForPlayer(player);
                loadFromNBT(cmp);
            }
        }
    }

    public void save() {
        if (!client) {
            Player player = playerWR.get();

            if (player != null) {
                CompoundTag cmp = PlayerDataHandler.getDataCompoundForPlayer(player);
                saveToNBT(cmp);
            }
        }
    }

    public void sync() {
        if (!client) {
            Player player = playerWR.get();
            ReskillableAPI.getInstance().syncPlayerData(player, this);
        }
    }

    public void saveAndSync() {
        save();
        sync();
    }

    public void loadFromNBT(CompoundTag cmp) {
        CompoundTag skillsCmp = cmp.getCompound(TAG_SKILLS_CMP);
        for (PlayerSkillInfo info : skillInfo.values()) {
            String key = info.skill.getKey();
            if (skillsCmp.contains(key)) {
                CompoundTag infoCmp = skillsCmp.getCompound(key);
                info.loadFromNBT(infoCmp);
            }
        }
    }

    public void saveToNBT(CompoundTag cmp) {
        CompoundTag skillsCmp = new CompoundTag();

        for (PlayerSkillInfo info : skillInfo.values()) {
            String key = info.skill.getKey();
            CompoundTag infoCmp = new CompoundTag();
            info.saveToNBT(infoCmp);
            skillsCmp.put(key, infoCmp);
        }

        cmp.put(TAG_SKILLS_CMP, skillsCmp);
    }

    // Event Handlers

    public void tickPlayer(TickEvent.PlayerTickEvent event) {
        forEachEventHandler(h -> h.onPlayerTick(event));
    }

    // TODO: Figure this out
    public void blockDrops(HarvestDropsEvent event) {
        forEachEventHandler(h -> h.onBlockDrops(event));
    }

    public void mobDrops(LivingDropsEvent event) {
        forEachEventHandler(h -> h.onMobDrops(event));
    }

    public void breakSpeed(BreakSpeed event) {
        forEachEventHandler(h -> h.getBreakSpeed(event));
    }

    public void attackMob(LivingHurtEvent event) {
        forEachEventHandler(h -> h.onAttackMob(event));
    }

    public void hurt(LivingHurtEvent event) {
        forEachEventHandler(h -> h.onHurt(event));
    }

    public void rightClickBlock(RightClickBlock event) {
        forEachEventHandler(h -> h.onRightClickBlock(event));
    }

    public void enderTeleport(EnderTeleportEvent event) {
        forEachEventHandler(h -> h.onEnderTeleport(event));
    }

    public void killMob(LivingDeathEvent event) {
        forEachEventHandler(h -> h.onKillMob(event));
    }

    public void forEachEventHandler(Consumer<IAbilityEventHandler> consumer) {
        skillInfo.values().forEach(info -> info.forEachEventHandler(consumer));
    }
}