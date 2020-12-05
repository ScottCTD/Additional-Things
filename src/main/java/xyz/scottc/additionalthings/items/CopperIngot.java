package xyz.scottc.additionalthings.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import xyz.scottc.additionalthings.AdditionalThings;

import java.util.Set;

public class CopperIngot extends Item {

    public CopperIngot() {
        super(new Properties().group(AdditionalThings.ITEM_TABLE));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        Set<ResourceLocation> set = this.getTags();
        for (ResourceLocation location : set) {
            playerIn.sendStatusMessage(new StringTextComponent(location.toString()), false);
            AdditionalThings.LOGGER.info(location.toString());
        }
        return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItemMainhand());
    }
}
