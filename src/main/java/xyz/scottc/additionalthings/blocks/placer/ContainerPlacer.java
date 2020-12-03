package xyz.scottc.additionalthings.blocks.placer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import xyz.scottc.additionalthings.registries.BlockRegistry;
import xyz.scottc.additionalthings.registries.ContainerTypeRegistry;

public class ContainerPlacer extends Container {

    private final TileentityPlacer tile;

    public ContainerPlacer(int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(ContainerTypeRegistry.PLACER.get(), id);
        this.tile = (TileentityPlacer) world.getTileEntity(pos);

        if (tile != null) {
            // The slot in this one-slot machine
            this.addSlot(new Slot(tile.getInventory(), 0, 80, 41));

            // Player Inventory
            int startX = 8;
            int startY = 84;
            int size = 18;

            int index = 9;
            int y = startY;

            for (int i = 0; i < 3; i++) {
                int x = startX;
                for (int j = 0; j < 9; j++) {
                    this.addSlot(new Slot(playerInventory, index, x, y));
                    x += size;
                    index++;
                }
                y += size;
            }

            // Hotbar
            startY = 142;

            int x = startX;
            index = 0;

            for (int i = 0; i < 9; i++) {
                this.addSlot(new Slot(playerInventory, index, x, startY));
                x += size;
                index++;
            }
        }

    }

    @Override
    public boolean canInteractWith(@NotNull PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(playerIn.getEntityWorld(), tile.getPos()), playerIn, BlockRegistry.PLACER.get());
    }

    /*
    这里的index代表的是当前inventory的slotid
    类似如果机器的一个slot是0
    玩家的inv从上到下从左到右就是 1 2 3 4
    物品栏的最后一个是index的最大值
    */
    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull PlayerEntity playerIn, int index) {

        // Just one slot in this, so the end is 1.
        int end = 1;
        int playerInvSize = 36;
        int playerInvEnd = end + playerInvSize;

        ItemStack itemstack = ItemStack.EMPTY;
        Slot clickedSlot = this.inventorySlots.get(index);

        if (clickedSlot != null && clickedSlot.getHasStack()) {
            ItemStack clickedStack = clickedSlot.getStack();
            itemstack = clickedStack.copy();

            // If the item is in this container, goes to the first slot in the player inventory
            if (index < end) {
                if (!this.mergeItemStack(clickedStack, end, playerInvEnd, false)) return ItemStack.EMPTY;
            } else {
                if (!this.mergeItemStack(clickedStack, 0, end, false)) return ItemStack.EMPTY;
            }
        }

        return itemstack;
    }

}
