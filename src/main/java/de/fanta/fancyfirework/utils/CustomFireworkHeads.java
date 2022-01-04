package de.fanta.fancyfirework.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.fanta.fancyfirework.FancyFirework;
import de.iani.cubesideutils.bukkit.items.CustomHeads;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class CustomFireworkHeads {

    public static ItemStack getCustomTextureHead(UUID uuid, String name, String value) {
        if (!FancyFirework.getPlugin().hasPlayerProfileAPI()) {
            ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            GameProfile profile = new GameProfile(uuid, "");
            profile.getProperties().put("textures", new Property("textures", value));
            Field profileField;
            try {
                profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(meta, profile);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            head.setItemMeta(meta);
            return head;
        } else {
            return CustomHeads.createHead(uuid, name, value);
        }
    }
}
