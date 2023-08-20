package codersafterdark.reskillable.client.gui;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.client.gui.button.GuiButtonLevelUp;
import codersafterdark.reskillable.client.gui.handler.InventoryTabHandler;
import codersafterdark.reskillable.client.gui.handler.KeyBindings;
import codersafterdark.reskillable.lib.LibMisc;
import codersafterdark.reskillable.network.MessageLevelUp;
import codersafterdark.reskillable.network.MessageUnlockUnlockable;
import codersafterdark.reskillable.network.PacketHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static codersafterdark.reskillable.client.base.RenderHelper.renderTooltip;

public class GuiSkillInfo extends Screen {
    public static final ResourceLocation SKILL_INFO_RES = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/skill_info.png");
    public static final ResourceLocation SKILL_INFO_RES2 = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/skill_info2.png");

    private final Skill skill;

    private int guiWidth, guiHeight;
    private ResourceLocation sprite;

    private GuiButtonLevelUp levelUpButton;
    private Unlockable hoveredUnlockable;
    private boolean canPurchase;

    public GuiSkillInfo(Skill skill) {
        this.skill = skill;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            this.minecraft.setScreen(null);

            if (this.minecraft.screen == null) {
                this.minecraft.setIngameFocus();
            }
        } else if (keyCode == KeyBindings.openGUI.getKeyCode() || keyCode == Minecraft.getInstance().gameSettings.keyBindInventory.getKeyCode()) {
            this.minecraft.setScreen(null);
            if (this.minecraft.screen != null) {
                this.minecraft.setIngameFocus();
            }
        }
    }

    @Override
    public void initGui() {
        guiWidth = 176;
        guiHeight = 166;

        int left = width / 2 - guiWidth / 2;
        int top = height / 2 - guiHeight / 2;

        buttonList.clear();
        if (ConfigHandler.enableLevelUp && skill.hasLevelButton()) {
            buttonList.add(levelUpButton = new GuiButtonLevelUp(left + 147, top + 10));
        }
        InventoryTabHandler.addTabs(this, buttonList);
        sprite = skill.getBackground();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        int left = width / 2 - guiWidth / 2;
        int top = height / 2 - guiHeight / 2;

        PlayerData data = PlayerDataHandler.get(minecraft.player);
        PlayerSkillInfo skillInfo = data.getSkillInfo(skill);

        minecraft.renderEngine.bindTexture(sprite);
        GlStateManager.color(0.5F, 0.5F, 0.5F);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 8; j++) {
                drawTexturedRec(left + 16 + i * 16, top + 33 + j * 16, 16, 16);
            }
        }

        GlStateManager.color(1F, 1F, 1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        if (ConfigHandler.enableLevelUp && skill.hasLevelButton()) {
            minecraft.renderEngine.bindTexture(SKILL_INFO_RES);
        } else {
            minecraft.renderEngine.bindTexture(SKILL_INFO_RES2);
        }

        drawTexturedModalRect(left, top, 0, 0, guiWidth, guiHeight);

        GuiSkills.drawSkill(left + 4, top + 9, skill);

        String levelStr = String.format("%d/%d [ %s ]", skillInfo.getLevel(), skill.getCap(), Component.translatable("reskillable.rank." + skillInfo.getRank()).getString());
        minecraft.fontRenderer.drawString(ChatFormatting.BOLD + skill.getName(), left + 22, top + 8, 4210752);
        minecraft.fontRenderer.drawString(levelStr, left + 22, top + 18, 4210752);

        minecraft.fontRenderer.drawString(Component.translatable("reskillable.misc.skill_points", skillInfo.getSkillPoints()).getString(), left + 15, top + 154, 4210752);

        int cost = skillInfo.getLevelUpCost();
        String costStr = Integer.toString(cost);
        if (skillInfo.isCapped()) {
            costStr = Component.translatable("reskillable.misc.capped").getString();
        }

        if (ConfigHandler.enableLevelUp && skill.hasLevelButton()) {
            drawCenteredString(mc.fontRenderer, costStr, left + 138, top + 13, 0xAFFF02);
            levelUpButton.setCost(cost);
        }

        hoveredUnlockable = null;
        skill.getUnlockables().forEach(u -> drawUnlockable(data, skillInfo, u, mouseX, mouseY));
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (hoveredUnlockable != null) {
            makeUnlockableTooltip(data, skillInfo, mouseX, mouseY);
        }
    }

    public void drawTexturedRec(int x, int y, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double) x, (double) (y + height), (double) this.zLevel).tex(0, 1).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + height), (double) this.zLevel).tex(1, 1).endVertex();
        bufferbuilder.pos((double) (x + width), (double) y, (double) this.zLevel).tex(1, 0).endVertex();
        bufferbuilder.pos((double) x, (double) y, (double) this.zLevel).tex(0, 0).endVertex();
        tessellator.draw();
    }

    private void drawUnlockable(PlayerData data, PlayerSkillInfo info, Unlockable unlockable, int mx, int my) {
        int x = width / 2 - guiWidth / 2 + 20 + unlockable.getX() * 28;
        int y = height / 2 - guiHeight / 2 + 37 + unlockable.getY() * 28;
        minecraft.renderEngine.bindTexture(SKILL_INFO_RES);
        boolean unlocked = info.isUnlocked(unlockable);

        int u = 0;
        int v = guiHeight;
        if (unlockable.hasSpikes()) {
            u += 26;
        }
        if (unlocked) {
            v += 26;
        }

        GlStateManager.color(1F, 1F, 1F);
        drawTexturedModalRect(x, y, u, v, 26, 26);

        minecraft.renderEngine.bindTexture(unlockable.getIcon());
        drawModalRectWithCustomSizedTexture(x + 5, y + 5, 0, 0, 16, 16, 16, 16);

        if (mx >= x && my >= y && mx < x + 26 && my < y + 26) {
            canPurchase = !unlocked && info.getSkillPoints() >= unlockable.getCost();
            hoveredUnlockable = unlockable;
        }
    }

    private void makeUnlockableTooltip(PlayerData data, PlayerSkillInfo info, int mouseX, int mouseY) {
        List<String> tooltip = new ArrayList<>();
        ChatFormatting tf = hoveredUnlockable.hasSpikes() ? ChatFormatting.AQUA : ChatFormatting.YELLOW;

        tooltip.add(tf + hoveredUnlockable.getName());

        if (isShiftKeyDown()) {
            addLongStringToTooltip(tooltip, hoveredUnlockable.getDescription(), guiWidth);
        } else {
            tooltip.add(ChatFormatting.GRAY + Component.translatable("reskillable.misc.shift").getString());
            tooltip.add("");
        }

        if (!info.isUnlocked(hoveredUnlockable)) {
            hoveredUnlockable.getRequirements().addRequirementsToTooltip(data, tooltip);
        } else {
            tooltip.add(ChatFormatting.GREEN + Component.translatable("reskillable.misc.unlocked").getString());
        }

        tooltip.add(ChatFormatting.GRAY + Component.translatable("reskillable.misc.skill_points", hoveredUnlockable.getCost()).getString());

        renderTooltip(mouseX, mouseY, tooltip);
    }

    private void addLongStringToTooltip(List<String> tooltip, String longStr, int maxLen) {
        String[] tokens = longStr.split(" ");
        String curr = ChatFormatting.GRAY.toString();
        int i = 0;

        while (i < tokens.length) {
            while (font.width(curr) < maxLen && i < tokens.length) {
                curr = curr + tokens[i] + ' ';
                i++;
            }
            tooltip.add(curr);
            curr = ChatFormatting.GRAY.toString();
        }
        tooltip.add(curr);
    }

    @Override
    protected void actionPerformed(Button button) {
        if (ConfigHandler.enableLevelUp && skill.hasLevelButton() && button == levelUpButton) {
            MessageLevelUp message = new MessageLevelUp(ReskillableRegistries.SKILLS.getKey(skill));
            PacketHandler.INSTANCE.sendToServer(message);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0 && hoveredUnlockable != null && canPurchase) {
            minecraft.getSoundManager().play(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            MessageUnlockUnlockable message = new MessageUnlockUnlockable(ReskillableRegistries.SKILLS.getKey(skill), ReskillableRegistries.UNLOCKABLES.getKey(hoveredUnlockable));
            PacketHandler.INSTANCE.sendToServer(message);
        } else if (mouseButton == 1 || mouseButton == 3) {
            minecraft.displayGuiScreen(new GuiSkills());
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}