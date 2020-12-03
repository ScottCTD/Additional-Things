package xyz.scottc.additionalthings.blocks.placer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import xyz.scottc.additionalthings.registries.TileentityTypeRegistry;

public class TileentityPlacer extends TileEntity implements ITickableTileEntity {

    // One Slot Inventory
    private final Inventory inventory= new Inventory(1);

    public TileentityPlacer() {
        super(TileentityTypeRegistry.PLACER.get());
    }

    @Override
    public void tick() {
        if (this.world != null) {
            if (!this.world.isBlockPowered(this.pos)) {
                this.world.setBlockState(this.pos, this.getBlockState().with(BlockPlacer.START, false));
                return;
            }
            this.world.setBlockState(this.pos, this.getBlockState().with(BlockPlacer.START, true));

            if (this.inventory.isEmpty()) return;

            ItemStack slotStack = this.inventory.getStackInSlot(0);
            BlockState targetBlock = Block.getBlockFromItem(slotStack.getItem()).getDefaultState();
            BlockPos targetPos = this.getPos().offset(this.getBlockState().get(BlockStateProperties.HORIZONTAL_FACING));
            if (this.world.getBlockState(targetPos).getBlock().equals(Blocks.AIR)) {
                this.world.setBlockState(targetPos, targetBlock);
                slotStack.shrink(1);
            }
        }
    }

    // Read the nbt to load the item.
    @Override
    public void read(@NotNull BlockState state, CompoundNBT nbt) {
        this.inventory.addItem(ItemStack.read(nbt.getCompound("itemstack")));
        super.read(state, nbt);
    }

    // Write the item in the inventory into nbt.
    @Override
    public @NotNull CompoundNBT write(CompoundNBT compound) {
        ItemStack itemStack = this.inventory.getStackInSlot(0).copy();
        compound.put("itemstack", itemStack.serializeNBT());
        return super.write(compound);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
