package xyz.scottc.additionalthings.blocks.treecutter;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static xyz.scottc.additionalthings.blocks.treecutter.TileentityTreeCutter.WORKING_RADIUS;

public class BlockTreeCutter extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty START = BooleanProperty.create("start");

    public BlockTreeCutter() {
        super(Block.Properties.create(Material.IRON)
        .hardnessAndResistance(5, 6)
        .harvestTool(ToolType.PICKAXE)
        .harvestLevel(1)
        .sound(SoundType.METAL));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        if (worldIn.isRemote) return;
        if (!(placer instanceof PlayerEntity)) return;
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileentityTreeCutter) {
            TileentityTreeCutter treeCutter = (TileentityTreeCutter) tileEntity;
            treeCutter.workingArea = this.initWorkingRange(pos, state);
        }
    }

    private BlockPos[] initWorkingRange(BlockPos selfPos, BlockState state) {
        BlockPos[] result = new BlockPos[(WORKING_RADIUS * 2 + 1) * (WORKING_RADIUS * 2 + 1)];
        int minX = selfPos.getX() - WORKING_RADIUS;
        int maxX = selfPos.getX() + WORKING_RADIUS;
        int minZ = selfPos.getZ() - WORKING_RADIUS;
        int maxZ = selfPos.getZ() + WORKING_RADIUS;
        switch (state.get(FACING)) {
            case NORTH:
                maxZ = selfPos.getZ() - 1;
                minZ = selfPos.getZ() - (WORKING_RADIUS * 2 + 1);
                break;
            case SOUTH:
                minZ = selfPos.getZ() + 1;
                maxZ = selfPos.getZ() + (WORKING_RADIUS * 2 + 1);
                break;
            case EAST:
                minX = selfPos.getX() + 1;
                maxX = selfPos.getX() + (WORKING_RADIUS * 2 + 1);
                break;
            case WEST:
                maxX = selfPos.getX() - 1;
                minX = selfPos.getX() - (WORKING_RADIUS * 2 + 1);
                break;
        }
        int y = selfPos.getY();
        int index = 0;
        for (int i = minX; i <= maxX; i++) {
            for (int j = minZ; j <= maxZ; j++) {
                result[index] = new BlockPos(i, y, j);
                index++;
            }
        }
        return result;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(START);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite()).with(START, false);
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
        return new TileentityTreeCutter();
    }
}
