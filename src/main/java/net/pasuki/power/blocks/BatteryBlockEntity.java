package net.pasuki.power.blocks;

import net.minecraft.core.BlockPos; // Import für die Blockposition
import net.minecraft.core.Direction; // Import für die Richtung
import net.minecraft.nbt.CompoundTag; // Import für NBT-Tags
import net.minecraft.world.level.block.entity.BlockEntity; // Import für Blockentitäten
import net.minecraft.world.level.block.state.BlockState; // Import für Blockzustände
import net.minecraft.world.level.block.state.properties.BlockStateProperties; // Import für Blockeigenschaften
import net.minecraftforge.common.capabilities.Capability; // Import für Fähigkeiten (Capabilities)
import net.minecraftforge.common.capabilities.ForgeCapabilities; // Import für Forge-Fähigkeiten
import net.minecraftforge.common.util.LazyOptional; // Import für LazyOptional
import net.minecraftforge.energy.EnergyStorage; // Import für Energiespeicherung
import net.minecraftforge.energy.IEnergyStorage; // Import für Energiespeicher
import net.pasuki.power.Registration; // Import für die Registrierung von Blöcken
import net.pasuki.power.tools.AdaptedEnergyStorage; // Import für angepasste Energiespeicher

public class BatteryBlockEntity extends BlockEntity { // Definition der Klasse BatteryBlockEntity, die eine Blockentität repräsentiert

    // Tags für NBT-Speicherung
    public static final String ENERGY_TAG = "Energy"; // Tag-Name für die Speicherung von Energie in NBT
    public static final int MAX_TRANSFER = 100; // Maximale Übertragungsrate für Energie
    public static final int CAPACITY = 10000; // Maximale Kapazität der Batterie

    // Energie-Storage und LazyOptional für Energie-Capability
    private final EnergyStorage energy = new EnergyStorage(CAPACITY, MAX_TRANSFER, MAX_TRANSFER); // Energiespeicher-Objekt mit angegebener Kapazität und Übertragungsrate
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> new AdaptedEnergyStorage(energy)); // LazyOptional, um Energiespeicher als Fähigkeit anzubieten

    // Boolean-Array zur Speicherung der Ausgaberichtungen für jede Richtung
    private boolean[] outputs = new boolean[Direction.values().length]; // Array zur Speicherung von Ausgaberichtungen

    // Konstruktor für BatteryBlockEntity, der die Position und den Zustand des Blocks erhält
    public BatteryBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.BATTERY_BLOCK_ENTITY.get(), pos, state); // Aufruf des Konstruktors der Superklasse
    }

    // Speichern der vorherigen Energiemenge
    private int previousEnergy = 0;

    // Methode, die beim Server-Tick ausgeführt wird
    public void tickServer() {
        // Energie verteilen und aktualisieren
        distributeEnergy(); // Energie verteilen

        boolean powered = false; // Standardmäßig wird der Block als nicht aktiviert betrachtet

        // Überprüfen, ob die Batterie Energie empfängt und ihre Energiemenge im Vergleich zum vorherigen Tick angestiegen ist
        if (energy.getEnergyStored() > previousEnergy) {
            powered = true; // Wenn Energie empfangen wird und die Energiemenge angestiegen ist, setze powered auf true
        }

        // Blockzustand aktualisieren, falls notwendig
        if (powered != getBlockState().getValue(BlockStateProperties.POWERED)) {
            level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockStateProperties.POWERED, powered));
        }

        // Aktualisieren der vorherigen Energiemenge für den nächsten Tick
        previousEnergy = energy.getEnergyStored();
    }


    // Methode zum Verteilen von Energie an benachbarte Blöcke
    private void distributeEnergy() {
        if (energy.getEnergyStored() <= 0) {
            return; // Keine Energie zum Verteilen vorhanden, daher beenden
        }

        // Überprüfen, ob Energie an benachbarte Blöcke mit aktivierten Ausgaberichtungen gesendet werden soll
        for (Direction direction : Direction.values()) { // Iteration über alle Richtungen
            if (!shouldOutput(direction)) {
                continue; // Ausgabe in dieser Richtung ist nicht aktiviert, daher überspringen
            }

            // Nachbarn abfragen und Energie senden
            BlockEntity be = level.getBlockEntity(getBlockPos().relative(direction)); // Abrufen der Blockentität in der Richtung
            if (be != null) {
                LazyOptional<IEnergyStorage> capability = be.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()); // Abrufen der Energiespeicher-Fähigkeit der Blockentität
                capability.ifPresent(e -> { // Überprüfen, ob die Fähigkeit vorhanden ist
                    if (e.canReceive()) { // Überprüfen, ob die Blockentität Energie empfangen kann
                        int received = e.receiveEnergy(Math.min(energy.getEnergyStored(), MAX_TRANSFER), false); // Energie an die Blockentität senden
                        energy.extractEnergy(received, false); // Energie aus dem Speicher abziehen
                        setChanged(); // Änderungen am Block speichern
                    }
                });
            }
        }
    }

    // Methode zur Überprüfung, ob Energie in eine bestimmte Richtung ausgegeben werden soll
    private boolean shouldOutput(Direction direction) {
        // Überprüfen, ob für eine bestimmte Richtung die Ausgabe aktiviert ist
        int index = direction.ordinal(); // Index der Richtung im Array
        return index >= 0 && index < outputs.length && outputs[index]; // Rückgabe des Ausgabestatus
    }

    // Überschreiben der Methode zum Speichern zusätzlicher Daten in NBT-Tags
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag); // Aufruf der Methode der Superklasse
        // Speichern der aktuellen Energie
        tag.put(ENERGY_TAG, energy.serializeNBT()); // Speichern der Energie im Tag

        // Speichern der Ausgaberichtungen als separate Tags
        for (Direction direction : Direction.values()) { // Iteration über alle Richtungen
            tag.putBoolean(direction.getName(), outputs[direction.ordinal()]); // Speichern des Ausgabestatus im Tag
        }
    }

    // Überschreiben der Methode zum Laden zusätzlicher Daten aus NBT-Tags
    @Override
    public void load(CompoundTag tag) {
        super.load(tag); // Aufruf der Methode der Superklasse
        // Laden der gespeicherten Energie
        if (tag.contains(ENERGY_TAG)) {
            energy.deserializeNBT(tag.get(ENERGY_TAG)); // Laden der Energie aus dem Tag
        }

        // Laden der Ausgaberichtungen aus separaten Tags
        for (Direction direction : Direction.values()) { // Iteration über alle Richtungen
            if (tag.contains(direction.getName())) {
                outputs[direction.ordinal()] = tag.getBoolean(direction.getName()); // Laden des Ausgabestatus aus dem Tag
            }
        }
    }

    // Überschreiben der Methode zum Abrufen von Fähigkeiten (Capabilities)
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        // Rückgabe des Energie-Capabilities
        if (cap == ForgeCapabilities.ENERGY) {
            return energyHandler.cast(); // Rückgabe des LazyOptional mit der Energiespeicher-Fähigkeit
        }
        return super.getCapability(cap, side); // Rückgabe der Fähigkeit der Superklasse
    }

    // Methode zum Umkehren des Ausgabestatus für eine bestimmte Richtung
    public void toggleOutput(Direction direction) {
        int index = direction.ordinal(); // Index der Richtung im Array
        if (index >= 0 && index < outputs.length) {
            outputs[index] = !outputs[index]; // Umkehren des Ausgabestatus
        }
    }

    // Methode zum Überprüfen des Ausgabestatus für eine bestimmte Richtung
    public boolean isOutput(Direction direction) {
        int index = direction.ordinal(); // Index der Richtung im Array
        return index >= 0 && index < outputs.length && outputs[index]; // Rückgabe des Ausgabestatus
    }
}
