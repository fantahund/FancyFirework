package de.fanta.fancyfirework.fireworks.defaults;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.utils.ColorUtils;
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

public class FireWorkBatteryRainbow extends FireWorkBattery {

    private final FancyFirework plugin = FancyFirework.getPlugin();

    public FireWorkBatteryRainbow() {
        super(new NamespacedKey(FancyFirework.getPlugin(), "battery_rainbow"));
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = CustomHeads.createHead(UUID.fromString("a4873b7d-e892-4502-b435-a6090b3d5210"), "Firework Rocket Rainbow", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjU1ZWE0M2U1OTI3ODVkMDE2YWNkZWVhOWE0YTZmOWNmMjJjMjc1M2U2OTU0MDVlOGM4M2QyZTA5ZWJjZjY0NyJ9fX0=");
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.of("#f26970") + "Ra" + ChatColor.of("#f88e66") + "in" + ChatColor.of("#ffb35c") + "bo" + ChatColor.of("#fbcb60") + "w B" + ChatColor.of("#f7e364") + "a" + ChatColor.of("#b4f07c") + "t" + ChatColor.of("#71fe94") + "t" + ChatColor.of("#73d0c9") + "e" + ChatColor.of("#75a3ff") + "r" + ChatColor.of("#cd70ff") + "y");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public Color randomColor() {
        return ColorUtils.getColor(plugin.getTime(), 0.1);
    }
}


