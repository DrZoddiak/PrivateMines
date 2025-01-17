/**
 * MIT License
 * <p>
 * Copyright (c) 2021 - 2022 Kyle Hicks
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.untouchedodin0.privatemines.utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.themoep.minedown.MineDown;
import me.untouchedodin0.privatemines.PrivateMines;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {
    public static final String TABLE_NAME = "privatemines";
    public static final Pattern pastePattern = Pattern.compile("[a-z]{10}");

    public Utils(PrivateMines privateMines) {
    }

    public static Location getRelative(Region region, int x, int y, int z) {
        final BlockVector3 point = region.getMinimumPoint().getMinimum(region.getMaximumPoint());
        final int regionX = point.getX();
        final int regionY = point.getY();
        final int regionZ = point.getZ();
        final BlockVector3 maxPoint = region.getMaximumPoint().getMaximum(region.getMinimumPoint());
        final int maxX = maxPoint.getX();
        final int maxY = maxPoint.getY();
        final int maxZ = maxPoint.getZ();
//        if (x < 0 || y < 0 || z < 0
//                || x > maxX - regionX || y > maxY - regionY || z > maxZ - regionZ) {
//            throw new IndexOutOfBoundsException("Relative location outside bounds of structure: " + x + ", " + y + ", " + z);
//        }
        final World worldeditWorld = region.getWorld();
        final org.bukkit.World bukkitWorld;
        if (worldeditWorld != null) {
            bukkitWorld = BukkitAdapter.asBukkitWorld(worldeditWorld).getWorld();
        } else {
            bukkitWorld = null;
        }
        return new Location(bukkitWorld, regionX + x, regionY + y, regionZ + z);
    }

    public static Location toLocation(BlockVector3 vector3, org.bukkit.World world) {
        return new Location(world, vector3.getX(), vector3.getY(), vector3.getZ());
    }

    // Credits to Redempt for this method
    // https://github.com/Redempt/RedLib/blob/master/src/redempt/redlib/region/CuboidRegion.java#L78-L87
    public static boolean contains(Location min, Location max, Location loc) {
        if (min.getWorld() != null && max.getWorld() != null && loc.getWorld() != null) {
            return loc.getWorld().getName().equals(min.getWorld().getName()) &&
                    loc.getX() >= min.getX() && loc.getY() >= min.getY() && loc.getZ() >= min.getZ() &&
                    loc.getX() < max.getX() && loc.getY() < max.getY() && loc.getZ() < max.getZ();
        }
        return false;
    }

    public static CuboidRegion toWorldEditCuboid(me.untouchedodin0.privatemines.utils.regions.CuboidRegion cuboidRegion) {
        var min = BlockVector3.at(
                cuboidRegion.getMinimumPoint().getBlockX(),
                cuboidRegion.getMinimumPoint().getBlockY(),
                cuboidRegion.getMinimumPoint().getBlockZ()
        );

        var max = BlockVector3.at(
                cuboidRegion.getMaximumPoint().getBlockX(),
                cuboidRegion.getMaximumPoint().getBlockY(),
                cuboidRegion.getMaximumPoint().getBlockZ()
        );

        return new CuboidRegion(min, max);
    }

    public static void complain() {
        PrivateMines.getPrivateMines().getLogger().info(ChatColor.RED + "This version of Minecraft is extremely outdated and support\n for it has reached its end of life.");
        PrivateMines.getPrivateMines().getLogger().info(ChatColor.RED + "You will be unable to run Private Mines on this Minecraft version,");
        PrivateMines.getPrivateMines().getLogger().info(ChatColor.RED + "and we will not to provide any further fixes or help with problems specific to legacy Minecraft versions.");
        PrivateMines.getPrivateMines().getLogger().info(ChatColor.RED + "Please consider updating to give your players a better experience and to avoid issues that have long been fixed.");
    }

    @SuppressWarnings("all") // I know I know, this is bad to do but ffs it wont' shut up
    public static void setMineFlags(ProtectedRegion protectedRegion, Map<String, Boolean> flags) {
        FlagRegistry flagRegistry = WorldGuard.getInstance().getFlagRegistry();

        //todo fix this

//        flags.entrySet().stream().forEach(stringBooleanEntry -> {
//            String flag = stringBooleanEntry.getKey();
//            boolean value = stringBooleanEntry.getValue();
//
//            Bukkit.getLogger().info("flag: " + flag);
//            Bukkit.getLogger().info("value: " + value);
////            Optional<IWrappedFlag<WrappedState>> iWrappedFlag = worldGuardWrapper.getFlag(flag, WrappedState.class);
////            if (value == false) {
////                iWrappedRegion.get().setFlag(iWrappedFlag.get(), WrappedState.DENY);
////            } else if (value == true) {
////                iWrappedRegion.get().setFlag(iWrappedFlag.get(), WrappedState.ALLOW);
////            }
//        });
    }

    /**
     * Utility method to set a flag.
     * <p>
     * Borrowed from <a href="https://github.com/EngineHub/WorldGuard/blob/bc63119373d4603e5b040460c41e712275a4d062/worldguard-core/src/main/java/com/sk89q/worldguard/commands/region/RegionCommandsBase.java#L414-L427">...</a>
     *
     * @param region the region
     * @param flag   the flag
     * @param value  the value
     * @throws InvalidFlagFormat thrown if the value is invalid
     */
    public static <V> void setFlag(ProtectedRegion region, Flag<V> flag, String value) throws InvalidFlagFormat {
        V val = flag.parseInput(FlagContext.create().setInput(value).setObject("region", region).build());
        region.setFlag(flag, val);
    }

    /**
     * @param location - The location where you want the schematic to be pasted at
     * @param file     - The file of the schematic you want to paste into the world
     * @see org.bukkit.Location
     * @see java.io.File
     */

    public void paste(Location location, File file) {

        ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(file);
        Clipboard clipboard;

        // Create a block vector 3 at the location you want the schematic to be pasted at
        BlockVector3 blockVector3 = BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        // If the clipboard format isn't null meaning it found the file load it in and read the data
        if (clipboardFormat != null) {
            try (ClipboardReader clipboardReader = clipboardFormat.getReader(new FileInputStream(file))) {

                // Get the world from the location
                World world = BukkitAdapter.adapt(Objects.requireNonNull(location.getWorld()));

                // Make a new Edit Session by building one.
                EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(world).build();

                // Read the clipboard reader and set the clipboard data.
                clipboard = clipboardReader.read();

                // Create an operation and paste the schematic

                Operation operation = new ClipboardHolder(clipboard) // Create a new operation instance using the clipboard
                        .createPaste(editSession) // Create a builder using the edit session
                        .to(blockVector3) // Set where you want the paste to go
                        .ignoreAirBlocks(true) // Tell world edit not to paste air blocks (true/false)
                        .build(); // Build the operation

                // Now we try to complete the operation and catch any exceptions

                try {
                    Operations.complete(operation);
                    editSession.close(); // We now close it to flush the buffers and run the cleanup tasks.
                } catch (WorldEditException worldEditException) {
                    worldEditException.printStackTrace();
                }
            } catch (IOException e) {
                // Print any stack traces of which may occur.
                e.printStackTrace();
            }
        }
    }

    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent((message)));
    }

    public static int getInventorySize(int max) {
        if (max <= 0) return 9;
        int quotient =  (int) Math.ceil(max / 9.0);
        return quotient > 5 ? 54: quotient * 9;
    }

    public static int rowsToSlots(int rows) {
        return rows * 9;
    }

    public static BaseComponent[] colorComponent(String string) {
        return new MineDown(string).toComponent();
    }

    public static String color(String string) {
        return BaseComponent.toLegacyText(colorComponent(string));
    }

    public static List<String> color(List<String> list) {
        List<String> stringList = new ArrayList<>();
        list.forEach(string -> stringList.add(color(string)));
        return stringList;
    }

    public static String colorBukkit(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String format(Material material) {
        return WordUtils.capitalize(material.name().toLowerCase().replaceAll("_", " "));
    }
}
