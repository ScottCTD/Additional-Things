package xyz.scottc.additionalthings.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xyz.scottc.additionalthings.AdditionalThings;

public class InfiniteSword extends Item {

    public InfiniteSword() {
        super(new Properties().group(AdditionalThings.ITEM_TABLE));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity victim = (LivingEntity) entity;
            victim.setHealth(0);
        }
        return false;
    }
}
