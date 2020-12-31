package xyz.scottc.additionalthings.blocks.diamondgenerator;

import net.minecraft.nbt.CompoundNBT;
import xyz.scottc.additionalthings.utils.IATIntArray;

public class DataDiamondGenerator implements IATIntArray {

    public int energy;
    public static final int ENERGY_INDEX = 0;

    public int[] energyOutputDirections = new int[] {0, 0, 0, 0, 0, 0};
    public static final int UP = 2;
    public static final int DOWN = 1;
    public static final int NORTH = 3;
    public static final int SOUTH = 4;
    public static final int EAST = 6;
    public static final int WEST = 5;


    private static final int LENGTH = 2;

    @Override
    public void read(CompoundNBT nbt) {
        this.energy = nbt.getInt("energy");
        this.energyOutputDirections = nbt.getIntArray("directions");
    }

    @Override
    public void write(CompoundNBT nbt) {
        nbt.putInt("energy", this.energy);
        nbt.putIntArray("directions", energyOutputDirections);
    }

    @Override
    public int get(int index) {
        switch (index) {
            case ENERGY_INDEX:
                return this.energy;
            case UP:
                return energyOutputDirections[1];
            case DOWN:
                return energyOutputDirections[0];
            case NORTH:
                return energyOutputDirections[2];
            case SOUTH:
                return energyOutputDirections[3];
            case EAST:
                return energyOutputDirections[5];
            case WEST:
                return energyOutputDirections[4];
            default:
                return 0;
        }
    }

    @Override
    public void set(int index, int value) {
        switch (index) {
            case ENERGY_INDEX:
                this.energy = value;
                break;
            case UP:
                this.energyOutputDirections[1] = value;
                break;
            case DOWN:
                this.energyOutputDirections[0] = value;
                break;
            case NORTH:
                this.energyOutputDirections[2] = value;
                break;
            case SOUTH:
                this.energyOutputDirections[3] = value;
                break;
            case EAST:
                this.energyOutputDirections[5] = value;
                break;
            case WEST:
                this.energyOutputDirections[4] = value;
                break;
        }
    }

    @Override
    public int size() {
        return LENGTH;
    }
}
