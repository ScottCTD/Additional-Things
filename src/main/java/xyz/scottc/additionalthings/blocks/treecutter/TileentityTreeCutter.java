package xyz.scottc.additionalthings.blocks.treecutter;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import xyz.scottc.additionalthings.registries.TileentityTypeRegistry;

public class TileentityTreeCutter extends TileEntity implements ITickableTileEntity {

    public static final int WORKING_RADIUS = 2;
    public static final int TICKS_PER_TREE = 20 * 3;
    public static final int TICKS_DETECT_GAP = 1;

    public BlockPos[] workingArea;

    private int gap = 0;
    private int counter = 0;
    private int workingIndex = 0;

    public TileentityTreeCutter() {
        super(TileentityTypeRegistry.TREE_CUTTER.get());
    }

    @Override
    public void tick() {
        if (this.world == null || this.workingArea == null) return;
        if (this.world.isRemote) return;
        this.gap++;
        this.getBlockState().with(BlockTreeCutter.START, false);
        if (this.gap < TICKS_DETECT_GAP) return;
        if (this.workingIndex > this.workingArea.length - 1) this.workingIndex = 0;
        BlockPos targetPos = this.workingArea[this.workingIndex];
        BlockState targetBlockState = this.world.getBlockState(targetPos);
        Block targetBlock = targetBlockState.getBlock();
        if (!this.isLog(targetBlock)) {
            this.gap = 0;
            this.workingIndex++;
            return;
        }
        this.getBlockState().with(BlockTreeCutter.START, true);
        this.counter++;
        if (this.counter >= TICKS_PER_TREE) {
            this.cutTree(targetPos);
            this.gap = 0;
        }
    }

    private void cutTree(BlockPos from) {
        if (this.world == null) return;
        //Block fromBlock = this.world.getBlockState(from).getBlock();
        BlockPos up = from.up();
        Block upBlock = this.world.getBlockState(up).getBlock();
        BlockPos[] temp = {from.north(), from.south(), from.east(), from.west(), from.north().east(), from.north().west(), from.south().east(), from.south().west()
                , up.north(), up.south(), up.east(), up.west(), up.north().east(), up.north().west(), up.south().east(), up.south().west()};
        // 分出一个线程砍周围的（四周的八个加上上面的八个）
        for (BlockPos blockPos : temp) {
            if (this.isLog(this.world.getBlockState(blockPos).getBlock())) {
                this.world.destroyBlock(blockPos, true);
                // Asynch tasks are not safe
                /*Thread thread = new Thread(() -> this.cutTree(blockPos));
                thread.start();*/
                this.cutTree(blockPos);
                try {
                    Thread.sleep(0);
                } catch (InterruptedException ignored) {}
            }
        }
        this.world.destroyBlock(from, true);
        if (this.isLog(upBlock)) {
            this.cutTree(up);
        }
    }

    private boolean isLog(Block target) {
        return target.isIn(BlockTags.LOGS) || target.getTags().contains(BlockTags.LOGS.getName());
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return super.write(compound);
    }
}
