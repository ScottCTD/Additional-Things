package xyz.scottc.additionalthings.blocks.diamondgenerator;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import xyz.scottc.additionalthings.registries.BlockRegistry;
import xyz.scottc.additionalthings.registries.ContainerTypeRegistry;
import xyz.scottc.additionalthings.utils.ATEnergyStorage;

public class ContainerDiamondGenerator extends Container {

    private final TileentityDiamondGenerator tile;

    public ContainerDiamondGenerator(int id, World world, BlockPos pos, PlayerInventory playerInv, PlayerEntity player) {
        super(ContainerTypeRegistry.DIAMOND_GENERATOR.get(), id);
        tile = (TileentityDiamondGenerator) world.getTileEntity(pos);

        if (tile != null) {
            // The slot in this one-slot machine
            this.tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(
                    iItemHandler -> this.addSlot(new SlotItemHandler(iItemHandler, 0, 79, 33)));
        }

        // Player Inventory
        int startX = 8;
        int startY = 84;
        int size = 18;

        int index = 9;
        int y = startY;

        for (int i = 0; i < 3; i++) {
            int x = startX;
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, index, x, y));
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
            this.addSlot(new Slot(playerInv, index, x, startY));
            x += size;
            index++;
        }

        this.syncEnergy();
    }

    // Sync the energy in the server and the client side
    private void syncEnergy() {
        // 这个负责传输后16位int
        this.trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                // 取energy整型的后16位 形成一个short
                return getEnergy() & 0xFFFF;
            }

            @Override
            public void set(int value) {
                tile.getCapability(CapabilityEnergy.ENERGY).ifPresent(iEnergyStorage -> {
                    // 这个方法是将value（int后面16位的short）从客户端传到服务端并且修改服务端数值
                    // 所以这里将原本int的存储的能量的后16位去掉，把value的放进去
                    int energyStored = iEnergyStorage.getEnergyStored() & 0xFFFF0000;
                    ((ATEnergyStorage) iEnergyStorage).setEnergy(energyStored + (value & 0xFFFF));
                });
            }
        });

        // 传输前16位int
        this.trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                // 获取energy的前16位
                return (getEnergy() >> 16) & 0xFFFF;
            }

            @Override
            public void set(int value) {
                tile.getCapability(CapabilityEnergy.ENERGY).ifPresent(iEnergyStorage -> {
                    int energyStored = iEnergyStorage.getEnergyStored() & 0x0000FFFF;
                    ((ATEnergyStorage) iEnergyStorage).setEnergy(energyStored | value << 16);
                });
            }
        });
    }

    int getEnergy() {
        return this.tile.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
    }

    BlockState getBlockState() {
        return this.tile.getBlockState();
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull PlayerEntity playerIn, int index) {
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

    @Override
    public boolean canInteractWith(@NotNull PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(playerIn.getEntityWorld(), this.tile.getPos()), playerIn, BlockRegistry.DIAMOND_GENERATOR.get());
    }
}
