package xyz.scottc.additionalthings.blocks.itemcollector;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import xyz.scottc.additionalthings.registries.TileentityTypeRegistry;
import xyz.scottc.additionalthings.utils.ShapeUtils;

import java.util.List;

public class TileentityItemCollector extends TileEntity implements ITickableTileEntity {

    public static final int RADIUS = 5;

    public TileentityItemCollector() {
        super(TileentityTypeRegistry.ITEM_COLLECTOR.get());
    }

    @Override
    public void tick() {
        if (this.world == null) return;
        if (this.world.isRemote) return;
        BlockPos centerPos = ShapeUtils.getCenterBlockPos(this.getPos(), this.getBlockState(), RADIUS);
        List<ItemEntity> itemEntities = this.world.getEntitiesWithinAABB(
                ItemEntity.class,
                new AxisAlignedBB(centerPos.getX() + RADIUS + 1, centerPos.getY(), centerPos.getZ() + RADIUS + 1, centerPos.getX() - RADIUS, centerPos.getY() + 1, centerPos.getZ() - RADIUS),
                Entity::isAlive);
        itemEntities.forEach(item -> item.moveToBlockPosAndAngles(this.pos.up(), 0, 0));
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return super.write(compound);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return super.getUpdatePacket();
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return super.getUpdateTag();
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {

    }
}
