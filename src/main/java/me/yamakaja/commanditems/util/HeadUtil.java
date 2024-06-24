package me.yamakaja.commanditems.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

public class HeadUtil {
    public static ItemStack getHead(String texture, OfflinePlayer owner, String displayName, List<String> lore) {
        ItemStack head = createSkull();
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        if (skullMeta == null || texture == null || texture.isEmpty()) return head;
        if (displayName != null) skullMeta.setDisplayName(displayName);
        if (lore != null) skullMeta.setLore(lore);
        setTexture(skullMeta, texture);
        head.setItemMeta(skullMeta);
        return head;
    }

    public static ItemStack createSkull() {
        try {
            return new ItemStack(Material.valueOf("PLAYER_HEAD"));
        } catch (IllegalArgumentException e) {
            return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (byte) 3);
        }
    }

    public static void setTexture(ItemStack itemStack, OfflinePlayer owner, String texture) {
        ItemMeta meta = itemStack.getItemMeta();
        if (!(meta instanceof SkullMeta)) return;
        setTexture((SkullMeta) meta, texture);
        itemStack.setItemMeta(meta);
    }

    public static void setTexture(SkullMeta skullMeta, String textureEncoded) {
        if (textureEncoded != null && !textureEncoded.isEmpty()) {
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);  // Assurez-vous que vous travaillez avec un item de type SKULL/PLAYER_HEAD
            ItemMeta meta = skull.getItemMeta();
            if (!(meta instanceof SkullMeta)) {
                Bukkit.getLogger().warning("ItemMeta n'est pas une instance de SkullMeta");
                return;
            }
            skullMeta = (SkullMeta) meta;

            // Utiliser l’API Bukkit/Spigot pour définir le profil de joueur avec une texture personnalisée
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), null);
            profile.getProperties().add(new ProfileProperty("textures", textureEncoded));

            // Utiliser Reflection pour appeler setPlayerProfile sur SkullMeta
            try {
                Method setPlayerProfileMethod = skullMeta.getClass().getDeclaredMethod("setPlayerProfile", PlayerProfile.class);
                setPlayerProfileMethod.setAccessible(true);
                setPlayerProfileMethod.invoke(skullMeta, profile);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            skull.setItemMeta(skullMeta);
        }
    }
}
