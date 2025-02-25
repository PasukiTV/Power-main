package net.pasuki.power.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.pasuki.power.Registration;
import net.pasuki.power.recipe.KaupenFurnaceRecipe;

import java.util.Map;

public class KaupenFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    private Map<Item, Integer> BURN_DURATION_MAP =
            Map.of(
                    //ModItems.PEAT_BRICK.get(), 100,
                    //ModItems.KOHLRABI.get(), 200,
                    Items.BLAZE_POWDER, 800);

    public KaupenFurnaceBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(Registration.KAUPEN_FURNACE_BLOCK_ENTITY.get(), pPos, pBlockState, KaupenFurnaceRecipe.Type.INSTANCE);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.mccourse.kaupen_furnace");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new KaupenFurnaceMenu(pContainerId, pInventory, this, dataAccess);
    }

    @Override
    protected int getBurnDuration(ItemStack pFuel) {
        return BURN_DURATION_MAP.getOrDefault(pFuel.getItem(), 0);
    }
}
