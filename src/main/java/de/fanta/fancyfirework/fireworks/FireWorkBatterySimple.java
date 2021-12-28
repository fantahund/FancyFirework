package de.fanta.fancyfirework.fireworks;

import de.fanta.fancyfirework.FancyFirework;
import de.iani.cubesideutils.bukkit.items.CustomHeads;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.UUID;

public class FireWorkBatterySimple extends BlockFireWork {

    public FireWorkBatterySimple() {
        super(new NamespacedKey(FancyFirework.getPlugin(), "battery_simple"));
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = CustomHeads.createHead(UUID.fromString("c27a85e4-cb0b-4366-be42-198122f5058f"), "Firework Rocket White", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWVjMWMxMTFmMzdjMTQ0ZDY4YTJjODlmY2I2MDlhODU1YmE3ZmIxNjkyOGFmYTQxM2M2ZjhjZDE5NDMxNSJ9fX0=");
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Simple Battery");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public void onPlace(Block block, ArmorStand stand, Player player) {

    }

    @Override
    public void onLit(ArmorStand stand, Player player) {
        stand.getWorld().playSound(stand.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1f, 1f);
        Random rand = new Random();
        Task task = new Task(player, stand, 20 * 60, 20 * 5, 20, () -> {
            Firework firework = (Firework) stand.getWorld().spawnEntity(stand.getLocation().add(0, 1.5, 0), EntityType.FIREWORK);
            firework.setVelocity(new Vector((rand.nextBoolean() ? 1 : -1) * rand.nextDouble(0.2), rand.nextDouble(0.5, 1.5), (rand.nextBoolean() ? 1 : -1) * rand.nextDouble(0.2)));
            FireworkMeta fireworkMeta = firework.getFireworkMeta();
            FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.values()[rand.nextInt(FireworkEffect.Type.values().length)]).withColor(randomColor()).withFade(randomColor()).withFlicker().withTrail().build();
            fireworkMeta.addEffect(effect);
            fireworkMeta.setPower(rand.nextInt(2) + 1);
            firework.setFireworkMeta(fireworkMeta);
        });
        task.start();
    }

    public Color randomColor() {
        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);
        return Color.fromRGB(red, green, blue);
    }

}
