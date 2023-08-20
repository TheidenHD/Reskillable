package codersafterdark.reskillable.api.toast;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import codersafterdark.reskillable.network.PacketHandler;
import codersafterdark.reskillable.network.SkillToastPacket;
import codersafterdark.reskillable.network.UnlockableToastPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;

public class ToastHelper {
    //This technically could work for any IToast but as a helper method might as well make sure it is an AbstractToast
    //in case we decide to modify this method to do more things
    public static void sendToast(AbstractToast toast) {
        Minecraft.getInstance().getToasts().addToast(toast);
    }


    public static void sendUnlockableToast(Unlockable u) {
        if (u != null) {
            sendToast(new UnlockableToast(u));
        }
    }


    public static void sendSkillToast(Skill skill, int level) {
        if (skill != null) {
            sendToast(new SkillToast(skill, level));
        }
    }

    public static void sendUnlockableToast(ServerPlayer player, Unlockable u) {
        if (u != null) {
            PacketHandler.INSTANCE.sendTo(new UnlockableToastPacket(ReskillableRegistries.UNLOCKABLES.getKey(u)), player);
        }
    }

    public static void sendSkillToast(ServerPlayer player, Skill skill, int level) {
        if (skill != null) {
            PacketHandler.INSTANCE.sendTo(new SkillToastPacket(ReskillableRegistries.SKILLS.getKey(skill), level), player);
        }
    }
}