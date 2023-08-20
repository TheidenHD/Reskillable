package codersafterdark.reskillable.api.data;

import codersafterdark.reskillable.api.requirement.RequirementCache;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class PlayerDataHandler {
    private static final String DATA_TAG = "SkillableData";
    private static HashMap<Integer, PlayerData> playerData = new HashMap<>();

    public static PlayerData get(Player player) {
        if (player == null) {
            return null;
        }

        int key = getKey(player);
        if (!playerData.containsKey(key)) {
            playerData.put(key, new PlayerData(player));
        }

        PlayerData data = playerData.get(key);
        if (data.playerWR.get() != player) {
            CompoundTag cmp = new CompoundTag();
            data.saveToNBT(cmp);
            RequirementCache.removeCache(player.getUUID(), player.level().isClientSide());
            playerData.remove(key);
            data = get(player);
            data.loadFromNBT(cmp);
        }

        return data;
    }

    public static void cleanup() {
        List<Integer> removals = new ArrayList<>();
        for (Entry<Integer, PlayerData> item : playerData.entrySet()) {
            PlayerData d = item.getValue();
            if (d != null && d.playerWR.get() == null) {
                removals.add(item.getKey());
            }
        }

        removals.forEach(i -> playerData.remove(i));
    }

    private static int getKey(Player player) {
        return player == null ? 0 : player.hashCode() << 1 + (player.level().isClientSide() ? 1 : 0);
    }

    public static CompoundTag getDataCompoundForPlayer(Player player) {
        CompoundTag forgeData = player.getPersistentData();
        if (!forgeData.contains(Player.PERSISTED_NBT_TAG)) {
            forgeData.put(Player.PERSISTED_NBT_TAG, new CompoundTag());
        }

        CompoundTag persistentData = forgeData.getCompound(Player.PERSISTED_NBT_TAG);
        if (!persistentData.contains(DATA_TAG)) {
            persistentData.put(DATA_TAG, new CompoundTag());
        }

        return persistentData.getCompound(DATA_TAG);
    }

    public static class EventHandler {

        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                PlayerDataHandler.cleanup();
            }
        }

        @SubscribeEvent
        public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
            PlayerData data = PlayerDataHandler.get(event.getEntity());
            if (data != null) {
                data.sync();
                data.getRequirementCache().forceClear();
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                PlayerData data = PlayerDataHandler.get(event.player);
                if (data != null) {
                    data.tickPlayer(event);
                }
            }
        }

        @SubscribeEvent
        public static void onBlockDrops(HarvestDropsEvent event) {
            PlayerData data = PlayerDataHandler.get(event.getHarvester());
            if (data != null) {
                data.blockDrops(event);
            }
        }

        @SubscribeEvent
        public static void onGetBreakSpeed(BreakSpeed event) {
            PlayerData data = PlayerDataHandler.get(event.getEntity());
            if (data != null) {
                data.breakSpeed(event);
            }
        }

        @SubscribeEvent
        public static void onMobDrops(LivingDropsEvent event) {
            if (event.getSource().getEntity() instanceof Player) {
                PlayerData data = PlayerDataHandler.get((Player) event.getSource().getEntity());
                if (data != null) {
                    data.mobDrops(event);
                }
            }
        }

        @SubscribeEvent
        public static void onHurt(LivingHurtEvent event) {
            if (event.getEntity() instanceof Player) {
                PlayerData data = PlayerDataHandler.get((Player) event.getEntity());
                if (data != null) {
                    data.hurt(event);
                }
            }
            if (event.getSource().getEntity() instanceof Player) {
                PlayerData data = PlayerDataHandler.get((Player) event.getSource().getEntity());
                if (data != null) {
                    data.attackMob(event);
                }
            }
        }

        @SubscribeEvent
        public static void onRightClickBlock(RightClickBlock event) {
            PlayerData data = PlayerDataHandler.get(event.getEntity());
            if (data != null) {
                data.rightClickBlock(event);
            }
        }

        @SubscribeEvent
        public static void onEnderTeleport(EnderTeleportEvent event) {
            if (event.getEntity() instanceof Player) {
                PlayerData data = PlayerDataHandler.get((Player) event.getEntity());
                if (data != null) {
                    data.enderTeleport(event);
                }
            }
        }

        @SubscribeEvent
        public static void onMobDeath(LivingDeathEvent event) {
            if (event.getSource().getEntity() instanceof Player) {
                PlayerData data = PlayerDataHandler.get((Player) event.getSource().getEntity());
                if (data != null) {
                    data.killMob(event);
                }
            }
        }
    }
}