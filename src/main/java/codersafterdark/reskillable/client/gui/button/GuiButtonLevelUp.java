package codersafterdark.reskillable.client.gui.button;

import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.client.gui.GuiSkillInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;

public class GuiButtonLevelUp extends Button {
    int cost;
    float renderTicks;

    public GuiButtonLevelUp(int x, int y, OnPress press, CreateNarration narration) {
        super(x, y, 14, 14, Component.empty(), (btn) -> {}, (supplier) -> null);
        cost = Integer.MAX_VALUE;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float f) {
        active = mc.player.experienceLevel >= cost || mc.player.isCreative();

        if (ConfigHandler.enableLevelUp) {
            if (active) {
                GlStateManager.color(1F, 1F, 1F);
                mc.renderEngine.bindTexture(GuiSkillInfo.SKILL_INFO_RES);

                int x = this.x;
                int y = this.y;
                int u = 176;
                int v = 0;
                int w = width;
                int h = height;

                if (mouseX > this.x && mouseY > this.y && mouseX < this.x + width && mouseY < this.y + height) {
                    v += h;
                } else {
                    float speedModifier = 4;
                    GlStateManager.color(1, 1, 1, (float) (Math.sin(mc.player.ticksExisted / speedModifier) + 1) / 2);
                }
                drawTexturedModalRect(x, y, u, v, w, h);
            }
        }
    }
}