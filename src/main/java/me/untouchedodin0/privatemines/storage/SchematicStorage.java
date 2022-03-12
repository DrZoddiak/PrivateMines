package me.untouchedodin0.privatemines.storage;

import me.untouchedodin0.privatemines.PrivateMines;
import me.untouchedodin0.privatemines.iterator.SchematicIterator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SchematicStorage {

    public Map<File, SchematicIterator.MineBlocks> mineBlocksMap = new HashMap<>();
    public PrivateMines privateMines = PrivateMines.getPrivateMines();

    public void addSchematic(File file, SchematicIterator.MineBlocks mineBlocks) {
        if (mineBlocksMap.containsKey(file)) {
            privateMines.getLogger().info(String.format("File %s was already stored in the map!", file.getName()));
        } else {
            mineBlocksMap.put(file, mineBlocks);
        }
    }

    public Map<File, SchematicIterator.MineBlocks> getMineBlocksMap() {
        return mineBlocksMap;
    }
}
