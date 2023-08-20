package codersafterdark.reskillable.api.toast;

import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.network.chat.Component;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UnlockableToast extends AbstractToast {
    private final Unlockable unlockable;

    public UnlockableToast(Unlockable unlockable) {
        super(unlockable.getName(), Component.translatable("reskillable.toast.unlockable_desc").getString());
        this.unlockable = unlockable;
    }

    @Override
    protected void renderImage(ToastComponent guiToast) {
        bindImage(guiToast, unlockable.getIcon());
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    protected boolean hasImage() {
        return true;
    }
}