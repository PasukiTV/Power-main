package net.pasuki.power.datagen;

import net.pasuki.power.Registration;
import net.pasuki.power.Power;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModels extends ItemModelProvider {

    public ItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Power.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(Registration.GENERATOR_BLOCK.getId().getPath(), modLoc("block/generator_block_off"));
        withExistingParent(Registration.CHARGER_BLOCK.getId().getPath(), modLoc("block/charger_block_on"));
        withExistingParent(Registration.SOLARCELL_BLOCK.getId().getPath(), modLoc("block/solarcell_block_off"));
        withExistingParent(Registration.BATTERY_BLOCK.getId().getPath(), modLoc("block/battery_block_off"));
        withExistingParent(Registration.CABLE_BLOCK.getId().getPath(), modLoc("block/cable"));
        withExistingParent(Registration.FACADE_BLOCK.getId().getPath(), modLoc("block/facade"));
    }
}
