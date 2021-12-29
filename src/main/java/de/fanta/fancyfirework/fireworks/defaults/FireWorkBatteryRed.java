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

import java.util.List;
import java.util.UUID;

public class FireWorkBatteryRed extends FireWorkBattery {

    public FireWorkBatteryRed() {
        super(new NamespacedKey(FancyFirework.getPlugin(), "battery_red"));
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = CustomHeads.createHead(UUID.fromString("fd2a127d-f1ff-40b7-a7ab-117d0d81dcc0"), "Firework Rocket Red", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzE1YmJkYTEyZTFiODMyYTZhNmFmODVkODQzOTE1MmQ5MTU3Y2UxMDRlNmE3ZjdiMzZhZWFjY2M4NjM1NDQifX19");
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Red Battery");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public void onPlace(Block block, ArmorStand stand, Player player) {
        //Nothing
    }

    @Override
    public Color randomColor() {
       java.awt.Color color = java.awt.Color.getHSBColor(1f, random.nextFloat(0.5f, 1), random.nextFloat(0.3f, 1));
        return Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
    }
}


