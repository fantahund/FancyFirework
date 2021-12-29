package de.fanta.fancyfirework.fireworks.defaults;

import de.fanta.fancyfirework.FancyFirework;
import de.iani.cubesideutils.bukkit.items.CustomHeads;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class FireWorkBatteryPink extends FireWorkBattery {

    public FireWorkBatteryPink() {
        super(new NamespacedKey(FancyFirework.getPlugin(), "battery_pink"));
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = CustomHeads.createHead(UUID.fromString("e0074ce5-decd-4c73-9376-8616adcb84b8"), "Firework Rocket Pink", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjlkNzNmYWMwZGI3YmI3YmQ4OTJhODdiZDQ0NzA4N2Q2YTQyOWMwODA4Mjg5MTg5YmM0NWNjZDk4YTNmYTFhIn19fQ==");
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.of("#ff14d4") + "Pink Battery");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public void onPlace(Block block, ArmorStand stand, Player player) {
        //Nothing
    }

    @Override
    public Color randomColor() {
        java.awt.Color color = java.awt.Color.getHSBColor(0.83f, random.nextFloat(0.5f, 1), random.nextFloat(0.5f, 1));
        return Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
    }
}


