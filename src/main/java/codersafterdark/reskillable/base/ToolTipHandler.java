package codersafterdark.reskillable.base;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.api.event.CacheInvalidatedEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static net.minecraftforge.fml.common.eventhandler.EventPriority.LOW;

public class ToolTipHandler { //TODO: Convert this from being basically all static to being an object (Low priority)
    private static Map<Class<? extends Screen>, Function<ToolTipInfo, List<String>>> tooltipInjectors = new HashMap<>();
    private static RequirementHolder lastLock = LevelLockHandler.EMPTY_LOCK;
    private static Class<? extends Screen> currentGui;
    private static List<String> toolTip = new ArrayList<>();
    private static ItemStack lastItem;
    private static boolean enabled;

    public static void resetLast() {
        lastItem = null;
        lastLock = LevelLockHandler.EMPTY_LOCK;
        toolTip = new ArrayList<>();
    }

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if (!enabled || event.isCanceled()) {
            return;
        }
        ItemStack current = event.getItemStack();
        PlayerData data = PlayerDataHandler.get(Minecraft.getInstance().player);
        if (lastItem != current) {
            lastItem = current;
            lastLock = LevelLockHandler.getSkillLock(current);
            lastLock.addRequirementsIgnoreShift(data, toolTip = new ArrayList<>());
        }
        boolean showDetails = !ConfigHandler.hideRequirements || Screen.hasShiftDown();
        List<String> extraToolTips = new ArrayList<>();

        if (currentGui != null) {
            //TODO: If an addon/set of addons ever want to both inject tooltips for the same class make tooltipInjectors hold a list of functions
            ToolTipInfo info = new ToolTipInfo(showDetails, data, lastItem);
            for (Map.Entry<Class<? extends Screen>, Function<ToolTipInfo, List<String>>> injectorInfo : tooltipInjectors.entrySet()) {
                if (injectorInfo.getKey().isAssignableFrom(currentGui)) {
                    extraToolTips.addAll(injectorInfo.getValue().apply(info));
                }
            }
            //TODO: Try to cache the return somehow. If so the function would need some way to return two lists one with showDetails one without
            //TODO Cont: Another way would be reset it when item changes and gui changes AND also invalidate it if shift state changes and config option not set
        }
        if (!toolTip.isEmpty() || !extraToolTips.isEmpty()) {
            List<String> curTooltip = event.getToolTip();
            if (showDetails) {
                curTooltip.add(ChatFormatting.DARK_PURPLE + Component.translatable("reskillable.misc.requirements").getString());
                curTooltip.addAll(toolTip);
            } else {
                curTooltip.add(ChatFormatting.DARK_PURPLE + Component.translatable("reskillable.misc.requirements_shift").getString());
            }
            curTooltip.addAll(extraToolTips);
        }
    }

    @SubscribeEvent
    public static void onGuiOpen(ScreenEvent.Opening event) {
        if (enabled && !event.isCanceled()) {
            currentGui = event.getNewScreen() == null ? null : event.getNewScreen().getClass();
        }
    }

    @SubscribeEvent
    public static void connect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        enabled = true;
    }

    @SubscribeEvent
    public static void disconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        enabled = false;
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onCacheInvalidated(CacheInvalidatedEvent event) {
        if (event.anyModified()) {
            //If nothing was invalidated the tooltip is still valid, as if the tooltip contained information it would have had data invalidated
            resetLast();
        }
    }

    public static void addTooltipInjector(Class<? extends Screen> gui, Function<ToolTipInfo, List<String>> creator) {
        tooltipInjectors.put(gui, creator);
    }

    public static class ToolTipInfo {
        private boolean showDetails;
        private PlayerData data;
        private ItemStack item;

        public ToolTipInfo(boolean showDetails, PlayerData data, ItemStack item) {
            this.showDetails = showDetails;
            this.data = data;
            this.item = item;
        }

        public boolean showDetails() {
            return showDetails;
        }

        public PlayerData getData() {
            return data;
        }

        public ItemStack getItem() {
            return item;
        }
    }
}