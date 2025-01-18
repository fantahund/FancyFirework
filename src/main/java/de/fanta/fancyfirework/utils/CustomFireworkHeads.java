package de.fanta.fancyfirework.utils;

import de.iani.cubesideutils.bukkit.items.CustomHeads;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CustomFireworkHeads {

    public static ItemStack getCustomTextureHead(UUID uuid, String name, String value) {
        return CustomHeads.createHead(uuid, name, value);
    }
}
