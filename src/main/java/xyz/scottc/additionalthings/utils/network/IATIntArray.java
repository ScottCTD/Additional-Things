package xyz.scottc.additionalthings.utils.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IIntArray;

public interface IATIntArray extends IIntArray {

    void read(CompoundNBT nbt);

    void write(CompoundNBT nbt);

    @Override
    int get(int index);

    @Override
    void set(int index, int value);

    @Override
    int size();
}
