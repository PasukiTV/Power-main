package net.pasuki.power;

// Import-Anweisungen

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pasuki.power.blocks.*;
import net.pasuki.power.cables.blocks.*;

import java.util.function.Supplier;

public class Registration {

    // Deferred-Register für Blöcke, Gegenstände, Block-Entitäten, Menütypen und kreative Modi-Tabs
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Power.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Power.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Power.MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Power.MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Power.MODID);

    // Registry-Objekte für verschiedene Blöcke, Gegenstände, Block-Entitäten und Menütypen

    // Generator-Block
    public static final RegistryObject<GeneratorBlock> GENERATOR_BLOCK = registerBlock("generator_block", GeneratorBlock::new, true);
    public static final RegistryObject<BlockEntityType<GeneratorBlockEntity>> GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("generator_block",
            () -> BlockEntityType.Builder.of(GeneratorBlockEntity::new, GENERATOR_BLOCK.get()).build(null));
    public static final RegistryObject<MenuType<GeneratorContainer>> GENERATOR_CONTAINER = MENU_TYPES.register("generator_block",
            () -> IForgeMenuType.create((windowId, inv, data) -> new GeneratorContainer(windowId, inv.player, data.readBlockPos())));

    // Generator-Block
    public static final RegistryObject<KaupenFurnaceBlock> KAUPEN_FURNACE_BLOCK = registerBlock("kaupen_furnace_block", KaupenFurnaceBlock::new, true);
    public static final RegistryObject<BlockEntityType<KaupenFurnaceBlockEntity>> KAUPEN_FURNACE_BLOCK_ENTITY = BLOCK_ENTITIES.register("kaupen_furnace_block",
            () -> BlockEntityType.Builder.of(KaupenFurnaceBlockEntity::new, KAUPEN_FURNACE_BLOCK.get()).build(null));
    public static final RegistryObject<MenuType<KaupenFurnaceMenu>> KAUPEN_FURNACE_MENU = MENU_TYPES.register("kaupen_furnace_block",
            () -> IForgeMenuType.create((windowId, inv, data) -> new KaupenFurnaceMenu(windowId, inv.player, data.readBlockPos())));

    // Charger-Block
    public static final RegistryObject<ChargerBlock> CHARGER_BLOCK = registerBlock("charger_block", ChargerBlock::new, true);
    public static final RegistryObject<BlockEntityType<ChargerBlockEntity>> CHARGER_BLOCK_ENTITY = BLOCK_ENTITIES.register("charger_block",
            () -> BlockEntityType.Builder.of(ChargerBlockEntity::new, CHARGER_BLOCK.get()).build(null));

    // SolarCell-Block
    public static final RegistryObject<SolarCellBlock> SOLARCELL_BLOCK = registerBlock("solarcell_block", SolarCellBlock::new, true);
    public static final RegistryObject<BlockEntityType<SolarCellBlockEntity>> SOLARCELL_BLOCK_ENTITY = BLOCK_ENTITIES.register("solarcell_block",
            () -> BlockEntityType.Builder.of(SolarCellBlockEntity::new, SOLARCELL_BLOCK.get()).build(null));

    // Battery-Block
    public static final RegistryObject<BatteryBlock> BATTERY_BLOCK = registerBlock("battery_block", BatteryBlock::new, true);
    public static final RegistryObject<BlockEntityType<BatteryBlockEntity>> BATTERY_BLOCK_ENTITY = BLOCK_ENTITIES.register("battery_block",
            () -> BlockEntityType.Builder.of(BatteryBlockEntity::new, BATTERY_BLOCK.get()).build(null));

    // Cable-Block
    public static final RegistryObject<CableBlock> CABLE_BLOCK = registerBlock("cable", CableBlock::new, true);
    public static final RegistryObject<BlockEntityType<CableBlockEntity>> CABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("cable",
            () -> BlockEntityType.Builder.of(CableBlockEntity::new, CABLE_BLOCK.get()).build(null));

    // Facade-Block
    public static final RegistryObject<FacadeBlock> FACADE_BLOCK = registerBlock("facade", FacadeBlock::new, true);
    public static final RegistryObject<BlockEntityType<FacadeBlockEntity>> FACADE_BLOCK_ENTITY = BLOCK_ENTITIES.register("facade",
            () -> BlockEntityType.Builder.of(FacadeBlockEntity::new, FACADE_BLOCK.get()).build(null));

    // Benutzerdefiniertes kreatives Modi-Tab für das Modul
    public static RegistryObject<CreativeModeTab> TAB = TABS.register("power", () -> CreativeModeTab.builder()
            .title(Component.translatable("tab.power"))
            .icon(() -> new ItemStack(GENERATOR_BLOCK.get()))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .displayItems((featureFlags, output) -> {
                output.accept(GENERATOR_BLOCK.get());
                output.accept(CHARGER_BLOCK.get());
                output.accept(SOLARCELL_BLOCK.get());
                output.accept(BATTERY_BLOCK.get());
                output.accept(CABLE_BLOCK.get());
                output.accept(FACADE_BLOCK.get());
                output.accept(KAUPEN_FURNACE_BLOCK.get());
            })
            .build());

    // Initialisierungsmethode zum Registrieren der Deferred-Register mit dem Mod-Ereignisbus
    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        MENU_TYPES.register(modEventBus);
        TABS.register(modEventBus);
    }
    // Hilfsmethode zum Registrieren von Blöcken und zugehörigen Gegenständen
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> blockSupplier, boolean withItem) {
        RegistryObject<T> blockRegistryObject = BLOCKS.register(name, blockSupplier);
        if (withItem) {
            ITEMS.register(name, () -> new BlockItem(blockRegistryObject.get(), new Item.Properties()));
        }
        return blockRegistryObject;
    }
}
