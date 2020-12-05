package xyz.scottc.additionalthings.blocks.diamondgenerator;

import net.minecraft.nbt.CompoundNBT;
import xyz.scottc.additionalthings.utils.network.IATIntArray;

public class DataDiamondGenerator implements IATIntArray {

    public int energy;
    public static final int ENERGY_INDEX = 0;

    private static final int LENGTH = 1;

    @Override
    public void read(CompoundNBT nbt) {
        this.energy = nbt.getInt("energy");
    }

    @Override
    public void write(CompoundNBT nbt) {
        nbt.putInt("energy", this.energy);
    }

    @Override
    public int get(int index) {
        switch (index) {
            case ENERGY_INDEX:
                return this.energy;
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
        }
    }

    @Override
    public int size() {
        return LENGTH;
    }
}
