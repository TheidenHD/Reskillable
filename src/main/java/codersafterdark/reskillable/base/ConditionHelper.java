package codersafterdark.reskillable.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public final class ConditionHelper {
    public static boolean hasRightTool(Player player, BlockState state, String toolClass, int reqLevel) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        return stack.getItem().getHarvestLevel(stack, toolClass, player, state) >= reqLevel;
    }
}