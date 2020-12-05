package xyz.scottc.additionalthings.blocks.placer;

import net.minecraft.nbt.CompoundNBT;
import xyz.scottc.additionalthings.utils.network.IATIntArray;

public class DataPlacer implements IATIntArray {

    public boolean start = false;
    public static final int START_INDEX = 0;

    public static final int LENGTH = 1;

    @Override
    public void read(CompoundNBT nbt) {
        this.start = nbt.getBoolean("start");
    }

    @Override
    public void write(CompoundNBT nbt) {
        nbt.putBoolean("start", this.start);
    }

    @Override
    public int get(int index) {
        switch (index) {
            case START_INDEX:
                if (start) return 1;
                else return 0;
            default:
                return 0;
        }
    }

    @Override
    public void set(int index, int value) {
        switch (index) {
            case START_INDEX:
                if (value == 0) this.start = false;
                else this.start = true;
                break;
        }
    }

    @Override
    public int size() {
        return LENGTH;
    }
}
