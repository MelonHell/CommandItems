package me.yamakaja.commanditems.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public class GlowUtil {

    /**
     * Applique un effet de brillance à l'item donné sans utiliser un enchantement spécifique.
     * @param item L'ItemStack auquel l'effet de brillance doit être appliqué.
     */
    public static void addGlow(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        // Vérifie que meta n'est pas null
        if (meta == null) {
            return;
        }

        meta.addEnchant(Enchantment.LUCK, 1, true); // Appliquer un enchantement invisible.
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Cache l'enchantement pour le rendre "invisible".

        item.setItemMeta(meta);
    }
}
