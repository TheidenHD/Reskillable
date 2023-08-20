package codersafterdark.reskillable.client.gui.button;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public interface IToolTipProvider {
    @Nullable
    ToolTip getToolTip(int mouseX, int mouseY);

    boolean isToolTipVisible();

    boolean isMouseOver(int mouseX, int mouseY);
}