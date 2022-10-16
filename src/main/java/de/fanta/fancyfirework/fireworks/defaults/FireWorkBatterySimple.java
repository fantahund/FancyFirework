package de.fanta.fancyfirework.fireworks.defaults;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.utils.CustomFireworkHeads;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class FireWorkBatterySimple extends FireWorkBattery {

    private static final FancyFirework plugin = FancyFirework.getPlugin();

    public FireWorkBatterySimple() {
        super(new NamespacedKey(FancyFirework.getPlugin(), "battery_simple"));
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = CustomFireworkHeads.getCustomTextureHead(UUID.fromString("c27a85e4-cb0b-4366-be42-198122f5058f"), "Firework Rocket White",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWVjMWMxMTFmMzdjMTQ0ZDY4YTJjODlmY2I2MDlhODU1YmE3ZmIxNjkyOGFmYTQxM2M2ZjhjZDE5NDMxNSJ9fX0=");
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Simple Battery");
        meta.setLore(FancyFirework.getPlugin().getConfig().getStringList("itemlore"));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    protected Permission createPermission() {
        Permission permission = new Permission("fancyfirework.use.battery_simple");
        permission.setDefault(PermissionDefault.TRUE);
        plugin.registerPermission(permission);
        return permission;
    }

    @Override
    public void onLit(Entity entity, Player player) {
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_CREEPER_PRIMED, SoundCategory.AMBIENT, 1f, 1f);
        Task task = new Task(player, entity, 20 * 60, 20 * 5, 20, () -> spawnRandomFirework(entity.getLocation()));
        task.start();
    }

    public Color randomColor() {
        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);
        return Color.fromRGB(red, green, blue);
    }

}
