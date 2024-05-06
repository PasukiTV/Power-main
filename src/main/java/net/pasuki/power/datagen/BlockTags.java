package net.pasuki.power.datagen;

import net.pasuki.power.Registration;
import net.pasuki.power.Power;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BlockTags extends BlockTagsProvider {

    public BlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Power.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Registration.GENERATOR_BLOCK.get(), Registration.CHARGER_BLOCK.get())
                .add(Registration.CABLE_BLOCK.get(), Registration.FACADE_BLOCK.get())
                .add(Registration.SOLARCELL_BLOCK.get())
                .add(Registration.BATTERY_BLOCK.get());
        tag(net.minecraft.tags.BlockTags.NEEDS_IRON_TOOL)
                .add(Registration.GENERATOR_BLOCK.get(), Registration.CHARGER_BLOCK.get())
                .add(Registration.CABLE_BLOCK.get(), Registration.FACADE_BLOCK.get())
                .add(Registration.SOLARCELL_BLOCK.get())
                .add(Registration.BATTERY_BLOCK.get());
     }
}
