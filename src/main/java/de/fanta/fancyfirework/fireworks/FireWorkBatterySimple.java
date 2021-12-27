package de.fanta.fancyfirework.fireworks;

import de.fanta.fancyfirework.FancyFirework;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class FireWorkBatterySimple extends BlockFireWork {

    public FireWorkBatterySimple() {
        super(new NamespacedKey(FancyFirework.getPlugin(), "battery_simple"));
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.DRIED_KELP_BLOCK);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Simple Battery");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public void onPlace(Block block, Player player) {

    }

    @Override
    public void onLit(Block block, Player player) {
        block.getWorld().playSound(block.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1f, 1f);
        Random rand = new Random();
        Task task = new Task(player, block, 20 * 60, 20 * 5, 10, () -> {
            Firework firework = (Firework) block.getWorld().spawnEntity(block.getLocation().add(0.5, 1, 0.5), EntityType.FIREWORK);
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
