package xyz.scottc.additionalthings.utils;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;

public class ShapeUtils {

    public static BlockPos getCenterBlockPos(BlockPos self, BlockState selfState, int radius) {
        if (selfState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            return self.offset(selfState.get(BlockStateProperties.HORIZONTAL_FACING), radius + 1);
        } else if (selfState.hasProperty(BlockStateProperties.FACING)) {
            return self.offset(selfState.get(BlockStateProperties.FACING), radius + 1);
        }
        return null;
    }

    public static BlockPos[] getSquareInFrontOf(BlockPos self, BlockState selfState, int radius) {
        BlockPos[] result = new BlockPos[(radius * 2 + 1) * (radius * 2 + 1)];
        int minX = self.getX() - radius;
        int maxX = self.getX() + radius;
        int minZ = self.getZ() - radius;
        int maxZ = self.getZ() + radius;
        switch (selfState.get(BlockStateProperties.HORIZONTAL_FACING)) {
            case NORTH:
                maxZ = self.getZ() - 1;
                minZ = self.getZ() - (radius * 2 + 1);
                break;
            case SOUTH:
                minZ = self.getZ() + 1;
                maxZ = self.getZ() + (radius * 2 + 1);
                break;
            case EAST:
                minX = self.getX() + 1;
                maxX = self.getX() + (radius * 2 + 1);
                break;
            case WEST:
                maxX = self.getX() - 1;
                minX = self.getX() - (radius * 2 + 1);
                break;
        }
        int y = self.getY();
        int index = 0;
        for (int i = minX; i <= maxX; i++) {
            for (int j = minZ; j <= maxZ; j++) {
                result[index] = new BlockPos(i, y, j);
                index++;
            }
        }
        return result;
    }

}
