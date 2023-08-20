package codersafterdark.reskillable.client.base;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

import java.util.ArrayDeque;
import java.util.Queue;

public class ClientTickHandler {
    public static volatile Queue<Runnable> scheduledActions = new ArrayDeque<>();

    public static int ticksInGame;
    public static float partialTicks;
    public static float delta;
    public static float total;

    private static void calcDelta() {
        float oldTotal = total;
        total = ticksInGame + partialTicks;
        delta = total - oldTotal;
    }

    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            partialTicks = event.renderTickTime;
        }
    }

    @SubscribeEvent
    public static void clientTickEnd(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) {
                PlayerDataHandler.cleanup();
            } else if (mc.player != null) {
                while (!scheduledActions.isEmpty()) {
                    scheduledActions.poll().run();
                }
            }

            Screen gui = mc.screen;
            if (gui == null || !gui.isPauseScreen()) {
                ticksInGame++;
                partialTicks = 0;
                HUDHandler.tick();
            }

            calcDelta();
        }
    }
}