package codersafterdark.reskillable.client.gui.button;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class GuiBestButton extends Button implements IToolTipProvider {
    public GuiBestButton(int x, int y, int width, int height, String buttonText, OnPress press, CreateNarration narration) {
        super(x, y, width, height, Component.translatable(buttonText), press, narration);
    }

    @Override
    public ToolTip getToolTip(int mouseX, int mouseY) {
        return null;
    }

    @Override
    public boolean isToolTipVisible() {
        return visible;
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return isMouseOver(mouseX, mouseY);
    }
}