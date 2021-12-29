package de.fanta.fancyfirework.fireworks.defaults;

import de.fanta.fancyfirework.FancyFirework;
import de.iani.cubesideutils.bukkit.items.CustomHeads;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class FireWorkBatteryBlue extends FireWorkBattery {

    public FireWorkBatteryBlue() {
        super(new NamespacedKey(FancyFirework.getPlugin(), "battery_blue"));
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = CustomHeads.createHead(UUID.fromString("0c0080ae-5b66-410a-8506-a118a26e1f39"), "Firework Rocket Cyan", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWFkMjYzMmVlNTU2NDUxMmU2Y2M0OThkZDgwOTM2M2JkYTlkYTUxOGQzNjg4NWFjYmJjZGM5ZmI1NWEifX19");
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Blue Battery");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public void onPlace(Block block, ArmorStand stand, Player player) {
        //Nothing
    }

    @Override
    public void onTick(Task task, boolean active) {
        Location loc = task.getArmorStand().getLocation().add(0, 1.5, 0);
        loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 1, 0, 0, 0, 0.025);
    }

    @Override
    public Color randomColor() {
        java.awt.Color color = java.awt.Color.getHSBColor(0.66f, random.nextFloat(0.5f, 1), random.nextFloat(0.3f, 1));
        return Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
    }
}


