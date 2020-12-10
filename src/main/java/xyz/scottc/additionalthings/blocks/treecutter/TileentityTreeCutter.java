package xyz.scottc.additionalthings.blocks.treecutter;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.scottc.additionalthings.registries.TileentityTypeRegistry;

public class TileentityTreeCutter extends TileEntity implements ITickableTileEntity {

    public static final int WORKING_RADIUS = 8;
    public static final int TICKS_PER_TREE = 20 * 3;
    public static final int TICKS_DETECT_GAP = 5;

    public BlockPos[] workingArea;
    public boolean renderRange;

    private int gap = 0;
    private int counter = 0;
    public int workingIndex = 0;

    public TileentityTreeCutter() {
        super(TileentityTypeRegistry.TREE_CUTTER.get());
    }

    @Override
    public void tick() {
        if (this.world == null || this.workingArea == null) return;
        if (this.world.isRemote) return;
        this.renderRange = this.world.isBlockPowered(this.pos);
        this.gap++;
        this.world.setBlockState(this.pos, this.getBlockState().with(BlockTreeCutter.START, false));
        if (this.gap < TICKS_DETECT_GAP) return;
        this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), 2);
        if (this.workingIndex > this.workingArea.length - 1) this.workingIndex = 0;
        BlockPos targetPos = this.workingArea[this.workingIndex];
        BlockState targetBlockState = this.world.getBlockState(targetPos);
        Block targetBlock = targetBlockState.getBlock();
        if (!this.isLog(targetBlock)) {
            this.gap = 0;
            this.workingIndex++;
            return;
        }
        this.world.setBlockState(this.pos, this.getBlockState().with(BlockTreeCutter.START, true));
        this.counter++;
        if (this.counter >= TICKS_PER_TREE) {
            this.cutTree(targetPos);
            this.workingIndex++;
            this.counter = 0;
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
        for (BlockPos blockPos : temp) {
            if (this.isLog(this.world.getBlockState(blockPos).getBlock())) {
                this.world.destroyBlock(blockPos, true);
                // Asynch tasks are not safe
                /*Thread thread = new Thread(() -> this.cutTree(blockPos));
                thread.start();*/
                this.cutTree(blockPos);
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
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    @Override
    public void read(@NotNull BlockState state, @NotNull CompoundNBT nbt) {
        this.gap = nbt.getInt("gap");
        this.counter = nbt.getInt("counter");
        this.workingIndex = nbt.getInt("workingIndex");
        this.renderRange = nbt.getBoolean("renderRange");
        super.read(state, nbt);
    }

    @Override
    public @NotNull CompoundNBT write(@NotNull CompoundNBT compound) {
        compound.putInt("gap", this.gap);
        compound.putInt("counter", this.counter);
        compound.putInt("workingIndex", this.workingIndex);
        compound.putBoolean("renderRange", this.renderRange);
        return super.write(compound);
    }

    // called to generate NBT for a syncing packet when a client loads a chunk that this TE is in
    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    // we can sync a TileEntity from the server to all tracking clients by calling world.notifyBlockUpdate
    // when that happens, this method is called on the server to generate a packet to send to the client
    // if you have lots of data, it's a good idea to keep track of which data has changed since the last time
    // this TE was synced, and then only send the changed data;
    // this reduces the amount of packets sent, which is good
    // we only have one value to sync so we'll just write everything into the NBT again
    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), 0, this.getUpdateTag());
    }

    // this method gets called on the client when it receives the packet that was sent in the previous method
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(this.getBlockState(), pkt.getNbtCompound());
    }
}
