package xyz.scottc.additionalthings.utils;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class ATEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT> {

    public ATEnergyStorage(int capacity) {
        super(capacity);
    }

    public ATEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public ATEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public ATEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    // Leave for override
    protected void onEnergyChanged() {}

    public void setEnergy(int energy) {
        this.energy = energy;
        this.onEnergyChanged();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putInt("energy", this.getEnergyStored());
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.setEnergy(nbt.getInt("energy"));
    }
}
