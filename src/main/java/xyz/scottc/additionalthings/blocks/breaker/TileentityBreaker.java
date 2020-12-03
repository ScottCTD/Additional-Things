package xyz.scottc.additionalthings.blocks.breaker;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import xyz.scottc.additionalthings.registries.TileentityTypeRegistry;

public class TileentityBreaker extends TileEntity implements ITickableTileEntity {

    // Execute every 5 ticks instead of every tick
    private final int ticks = 5;
    private int temp = 0;

    public TileentityBreaker() {
        super(TileentityTypeRegistry.BREAKER.get());
    }

    @Override
    public void tick() {
        this.temp++;
        if (this.temp != this.ticks) return;
        this.temp = 0;
        if (this.world != null) {
            if (this.world.isRemote) return;
            if (!this.world.isBlockPowered(this.pos)) {
                // update block state
                this.world.setBlockState(this.pos, this.getBlockState().with(BlockBreaker.START, false));
                return;
            }
            this.world.setBlockState(this.pos, this.getBlockState().with(BlockBreaker.START, true));
            // offset: the pos in front of that facing
            BlockPos target = this.getPos().offset(this.getBlockState().get(BlockStateProperties.FACING));
            BlockState targetState = this.world.getBlockState(target);
            // hardness <= 0 -> Unbreakable
            if (targetState.getBlockHardness(this.world, target) >= 0) {
                this.world.destroyBlock(target, true);
            }
        }
    }
}
