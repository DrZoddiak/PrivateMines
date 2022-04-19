package me.untouchedodin0.privatemines.mine.data;

import com.sk89q.worldedit.regions.Region;
import me.untouchedodin0.kotlin.mine.type.MineType;
import org.bukkit.Location;
import org.bukkit.Material;
import redempt.redlib.region.CuboidRegion;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class MineDataBuilder {

    UUID mineOwner;
    UUID coOwner;
    Location mineLocation;
    Location spawnLocation;
    Location npcLocation;
    Location minimumMining;
    Location maximumMining;
    Location minimumFullRegion;
    Location maximumFullRegion;

    CuboidRegion miningRegion;
    Region fullRegion;

    String worldName;
    MineType mineType;

    boolean isOpen;
    double tax = 5;

    Map<Material, Double> materials = new EnumMap<>(Material.class);

    public MineDataBuilder setOwner(UUID uuid) {
        this.mineOwner = uuid;
        return this;
    }

    public MineDataBuilder setCoOwner(UUID uuid) {
        this.coOwner = uuid;
        return this;
    }

    public MineDataBuilder setMineLocation(Location location) {
        this.mineLocation = location;
        return this;
    }

    public MineDataBuilder setSpawnLocation(Location location) {
        this.spawnLocation = location;
        return this;
    }

    public MineDataBuilder setNpcLocation(Location location) {
        this.npcLocation = location;
        return this;
    }

    public MineDataBuilder setMinimumMining(Location location) {
        this.minimumMining = location;
        return this;
    }

    public MineDataBuilder setMaximumMining(Location location) {
        this.maximumMining = location;
        return this;
    }

    public MineDataBuilder setMinimumFullRegion(Location location) {
        this.minimumFullRegion = location;
        return this;
    }

    public MineDataBuilder setMaximumFullRegion(Location location) {
        this.maximumFullRegion = location;
        return this;
    }

    public MineDataBuilder setMiningRegion(CuboidRegion cuboidRegion) {
        this.miningRegion = cuboidRegion;
        return this;
    }

    public MineDataBuilder setMineType(MineType mineType) {
        this.mineType = mineType;
        return this;
    }

    public MineData build() {
        // Create the mine data object and return it
        MineData mineData = new MineData();
        mineData.mineOwner = mineOwner;
        mineData.coOwner = coOwner;
        mineData.mineLocation = mineLocation;
        mineData.spawnLocation = spawnLocation;
        mineData.npcLocation = npcLocation;
        mineData.minimumMining = minimumMining;
        mineData.maximumMining = maximumMining;
        mineData.minimumFullRegion = minimumFullRegion;
        mineData.maximumFullRegion = maximumFullRegion;
        mineData.miningRegion = miningRegion;
        mineData.mineType = mineType;
        return mineData;
    }
}