package xyz.scottc.additionalthings.blocks.diamondgenerator;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.scottc.additionalthings.AdditionalThings;
import xyz.scottc.additionalthings.registries.TileentityTypeRegistry;
import xyz.scottc.additionalthings.utils.ATEnergyStorage;

import java.util.concurrent.atomic.AtomicInteger;

public class TileentityDiamondGenerator extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    static final int CAPACITY = 100000;
    private static final int MAX_TRANSFER_PER_TICK = 500;
    private static final int FE_PER_DIAMOND = 10000;
    private static final int TICKS_PER_DIAMOND = 20 * 10;

    private final ItemStackHandler itemHandler = this.createItemHandler();
    private final ATEnergyStorage energyStorage = this.createEnergyStorage();

    private final LazyOptional<IItemHandler> item = LazyOptional.of(() -> this.itemHandler);
    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> this.energyStorage);

    private int counter = 0;

    public TileentityDiamondGenerator() {
        super(TileentityTypeRegistry.DIAMOND_GENERATOR.get());
    }

    @Override
    public void tick() {
        if (this.world != null && !this.world.isRemote) {
            /*
            counter = 0 -> Not started yet
            counter between 1 and 10 * 20 -> Processing
            counter = 21 -> Finished
            接收钻石 每个钻石总共产生10000FE
            产生速率为 1000 / s -> 50 / tick
             */
            final int FEPerTick = FE_PER_DIAMOND / TICKS_PER_DIAMOND;

            ItemStack itemStack = this.itemHandler.getStackInSlot(0);
            BlockState blockState = this.world.getBlockState(this.pos);
            int energyStored = this.energyStorage.getEnergyStored();

            if (!(energyStored >= CAPACITY)) {
                if (itemStack.getItem().equals(Items.DIAMOND) && this.counter == 0) {
                    this.counter++;
                    this.itemHandler.extractItem(0, 1, false);
                    this.world.setBlockState(this.pos, blockState.with(BlockDiamondGenerator.START, true));
                }
                if (this.counter > 0 && this.counter <= TICKS_PER_DIAMOND) {
                    if (!(energyStored + FEPerTick > CAPACITY)) {
                        this.counter++;
                        this.energyStorage.setEnergy(energyStored + FEPerTick);
                    } else {
                        this.counter = TICKS_PER_DIAMOND + 1;
                    }
                }
                if (this.counter > TICKS_PER_DIAMOND) {
                    if (!this.itemHandler.getStackInSlot(0).getItem().equals(Items.DIAMOND)) {
                        this.world.setBlockState(this.pos, blockState.with(BlockDiamondGenerator.START, false));
                    }
                    this.counter = 0;
                }
            }

            // 向外push能量
            this.pushEnergy();
        }
    }

    private void pushEnergy() {
        if (this.world != null) {
            AtomicInteger availableEnergy = new AtomicInteger(this.energyStorage.getEnergyStored());
            if (availableEnergy.get() > 0) {
                // 获取当前机器每个方向的机器，然后push能量
                for (Direction direction : Direction.values()) {
                    // 获取目标机器
                    TileEntity target = this.world.getTileEntity(this.pos.offset(direction));
                    if (target != null) {
                        // 如果有机器，就获取他的capability然后输送能量
                        LazyOptional<IEnergyStorage> capability = target.getCapability(CapabilityEnergy.ENERGY, direction);
                        capability.ifPresent(iEnergyStorage -> {
                            if (iEnergyStorage.canReceive()) {
                                // 目标机器接收到了能量
                                int received = iEnergyStorage.receiveEnergy(Math.min(availableEnergy.get(), MAX_TRANSFER_PER_TICK), false);
                                // 减少当前机器的能量
                                availableEnergy.getAndAdd(-received);
                                this.energyStorage.setEnergy(availableEnergy.get());
                            }
                        });
                    }
                    // 如果当前机器没能量了，就return
                    if (availableEnergy.get() <= 0) return;
                }
            }
        }
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                if (!stack.getItem().equals(Items.DIAMOND)) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return stack.getItem().equals(Items.DIAMOND);
            }
        };
    }

    private ATEnergyStorage createEnergyStorage() {
        return new ATEnergyStorage(CAPACITY) {
            @Override
            protected void onEnergyChanged() {
                markDirty();
            }
        };
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
            return this.item.cast();
        }
        if (cap.equals(CapabilityEnergy.ENERGY)) {
            return this.energy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public @NotNull ITextComponent getDisplayName() {
        return new TranslationTextComponent("screen." + AdditionalThings.MODID + ".diamond_generator");
    }

    @Nullable
    @Override
    public Container createMenu(int id, @NotNull PlayerInventory playerInv, @NotNull PlayerEntity player) {
        return new ContainerDiamondGenerator(id, player.getEntityWorld(), this.pos, playerInv, player);
    }

    @Override
    public void remove() {
        super.remove();
        this.energy.invalidate();
        this.item.invalidate();
    }

    @Override
    public void read(@NotNull BlockState state, CompoundNBT nbt) {
        this.itemHandler.deserializeNBT(nbt.getCompound("inv"));
        this.energyStorage.deserializeNBT(nbt.getCompound("energy"));
        this.counter = nbt.getInt("counter");
        super.read(state, nbt);
    }

    @Override
    public @NotNull CompoundNBT write(CompoundNBT compound) {
        compound.put("inv", this.itemHandler.serializeNBT());
        compound.put("energy", this.energyStorage.serializeNBT());
        compound.putInt("counter", this.counter);
        return super.write(compound);
    }
}
