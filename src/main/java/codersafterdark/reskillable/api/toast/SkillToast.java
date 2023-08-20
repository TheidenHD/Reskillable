package codersafterdark.reskillable.api.toast;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.client.base.RenderHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

public class SkillToast extends AbstractToast {
    private final Skill skill;
    private final int rank;

    public SkillToast(Skill skill, int level) {
        super(skill.getName(), Component.translatable("reskillable.toast.skill_desc", level).getString());
        this.skill = skill;
        this.rank = this.skill.getRank(level);
    }

    @Override
    protected void renderImage(ToastComponent guiToast) {
        if (this.skill.hasCustomSprites()) {
            ResourceLocation sprite = this.skill.getSpriteLocation(rank);
            if (sprite != null) {
                bindImage(guiToast, sprite);
                Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
            }
        } else {
            bindImage(guiToast, skill.getSpriteLocation());
            Pair<Integer, Integer> pair = skill.getSpriteFromRank(rank);
            RenderHelper.drawTexturedModalRect(x, y, 1, pair.getKey(), pair.getValue(), 16, 16, 1f / 64, 1f / 64);
        }
    }

    @Override
    protected boolean hasImage() {
        return !this.skill.hasCustomSprites() || this.skill.getSpriteLocation(rank) != null;
    }
}