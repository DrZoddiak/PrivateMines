package me.untouchedodin0.privatemines.utils.inventory;

import co.aikar.commands.PaperCommandManager;
import me.untouchedodin0.privatemines.PrivateMines;
import me.untouchedodin0.privatemines.utils.SkullCreator;
import me.untouchedodin0.privatemines.utils.locale.LocaleManager;
import me.untouchedodin0.privatemines.utils.locale.LocaleObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.commandmanager.Messages;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.Locale;
import java.util.Objects;

public class LocaleInventory {

    /**
     * Opens the locale inventory for the player
     * Get the heads from <a href="https://minecraft-heads.com/"</a>
     * To get the Base64 of the head, go into the head and find the Base64 under the "Other" thing at the bottom
     */
    static PrivateMines privateMines = PrivateMines.getPrivateMines();

    public static void openLocaleMenu(Player player) {

        InventoryGUI changeLocale = new InventoryGUI(Bukkit.createInventory(null, 9, "Change locale"));
        ItemStack englishSkull = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODc5ZDk5ZDljNDY0NzRlMjcxM2E3ZTg0YTk1ZTRjZTdlOGZmOGVhNGQxNjQ0MTNhNTkyZTQ0MzVkMmM2ZjlkYyJ9fX0=");
        ItemBuilder english = new ItemBuilder(englishSkull)
                .setName(ChatColor.YELLOW + "English")
                .setLore(ChatColor.GRAY + "Click to change to " + ChatColor.GREEN + "English");
        ItemButton englishButton = ItemButton.create(english, event -> {
            event.setCancelled(true);
            player.sendMessage(String.format(ChatColor.GREEN + "You have changed your locale to %s",
                                             Objects.requireNonNull(Objects.requireNonNull(event.getCurrentItem()).getItemMeta()).getDisplayName()));
            player.closeInventory();

        });
        changeLocale.addButton(0, englishButton);
        changeLocale.open(player);
    }
}
