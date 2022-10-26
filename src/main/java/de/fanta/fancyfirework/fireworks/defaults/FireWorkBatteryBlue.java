package de.fanta.fancyfirework.fireworks.defaults;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.utils.CustomFireworkHeads;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class FireWorkBatteryBlue extends FireWorkBattery {

    private static final FancyFirework plugin = FancyFirework.getPlugin();

    public FireWorkBatteryBlue() {
        super(new NamespacedKey(FancyFirework.getPlugin(), "battery_blue"));
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = CustomFireworkHeads.getCustomTextureHead(UUID.fromString("0c0080ae-5b66-410a-8506-a118a26e1f39"), "Firework Rocket Cyan",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWFkMjYzMmVlNTU2NDUxMmU2Y2M0OThkZDgwOTM2M2JkYTlkYTUxOGQzNjg4NWFjYmJjZGM5ZmI1NWEifX19");
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Blue Battery");
        meta.setLore(FancyFirework.getPlugin().getConfig().getStringList("itemlore")); //TODO for all fireworks later replace with language system
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public void onTick(Task task, boolean active) {
        Location loc = task.getEntity().getLocation().add(0, 1.5, 0);
        loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 1, 0, 0, 0, 0.025);
    }

    @Override
    public Color randomColor() {
        java.awt.Color color = java.awt.Color.getHSBColor(0.66f, random.nextFloat(0.5f, 1), random.nextFloat(0.3f, 1));
        return Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
    }
}


