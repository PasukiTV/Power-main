package net.pasuki.power.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;
import net.pasuki.power.Registration;
import net.pasuki.power.recipe.KaupenFurnaceRecipe;

public class KaupenFurnaceMenu extends AbstractFurnaceMenu {
    public KaupenFurnaceMenu(int pContainerId, Player pPlayerInventory, BlockPos friendlyByteBuf) {
         this(pContainerId, pPlayerInventory.getInventory());
    }

    public KaupenFurnaceMenu(int pContainerId, Inventory pPlayerInventory, Container container, ContainerData data) {
        super(Registration.KAUPEN_FURNACE_MENU.get(), KaupenFurnaceRecipe.Type.INSTANCE, RecipeBookType.FURNACE, pContainerId, pPlayerInventory, container, data);
    }

    public KaupenFurnaceMenu(int pContainerId, Inventory pPlayerInventory) {
        super(Registration.KAUPEN_FURNACE_MENU.get(), KaupenFurnaceRecipe.Type.INSTANCE, RecipeBookType.FURNACE, pContainerId, pPlayerInventory);
    }

    @Override
    protected boolean isFuel(ItemStack pStack) {
        return true;
    }
}
