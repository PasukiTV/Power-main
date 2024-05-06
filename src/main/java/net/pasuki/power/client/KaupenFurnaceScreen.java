package net.pasuki.power.client;

import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.pasuki.power.Power;
import net.pasuki.power.blocks.KaupenFurnaceMenu;
import net.pasuki.power.recipe.KaupenFurnaceRecipeBookComponent;

public class KaupenFurnaceScreen extends AbstractFurnaceScreen<KaupenFurnaceMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Power.MODID, "textures/gui/kaupen_furnace.png");

    public KaupenFurnaceScreen(KaupenFurnaceMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, new KaupenFurnaceRecipeBookComponent(), pPlayerInventory, pTitle, TEXTURE);
    }
}
