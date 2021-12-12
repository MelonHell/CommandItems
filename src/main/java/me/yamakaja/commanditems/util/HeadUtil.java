package me.yamakaja.commanditems.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
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
        setTexture(skullMeta, owner, texture);
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
        setTexture((SkullMeta) meta, owner, texture);
        itemStack.setItemMeta(meta);
    }

    public static void setTexture(SkullMeta meta, OfflinePlayer owner, String texture) {
        if (texture != null) {
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            if (owner != null) profile = new GameProfile(owner.getUniqueId(), owner.getName());
            profile.getProperties().put("textures", new Property("textures", texture));
            try {
                Method metaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                metaSetProfileMethod.setAccessible(true);
                metaSetProfileMethod.invoke(meta, profile);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                try {
                    Field metaProfileField = meta.getClass().getDeclaredField("profile");
                    metaProfileField.setAccessible(true);
                    metaProfileField.set(meta, profile);
                } catch (NoSuchFieldException | IllegalAccessException ex2) {
                    ex2.printStackTrace();
                }
            }
        }
    }
}
