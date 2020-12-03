package xyz.scottc.additionalthings.blocks.breaker;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import org.jetbrains.annotations.Nullable;

public class BlockBreaker extends Block {

    public static final BooleanProperty START = BooleanProperty.create("start");
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockBreaker() {
        super(Properties.create(Material.IRON)
                .hardnessAndResistance(5, 6)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(1)
                .setRequiresTool()
                .sound(SoundType.METAL));
    }

    // Set the opposite of the facing of player as the direction.
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(START, false).with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(START);
        super.fillStateContainer(builder);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.get(START) ? 14 : 0;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileentityBreaker();
    }
}
