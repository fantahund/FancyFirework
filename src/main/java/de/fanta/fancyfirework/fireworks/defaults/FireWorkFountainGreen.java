package de.fanta.fancyfirework.fireworks.defaults;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.utils.CustomFireworkHeads;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class FireWorkFountainGreen extends FireWorkFountain {

    private static final FancyFirework plugin = FancyFirework.getPlugin();

    public FireWorkFountainGreen() {
        super(new NamespacedKey(FancyFirework.getPlugin(), "fountain_green"));
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = CustomFireworkHeads.getCustomTextureHead(UUID.fromString("55bfe88f-7668-40f3-9c17-e694e857f1c0"), "Firework Rocket (black / lime)",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmNiOTNlZjViNjk0OTA1NDdhNTg5MTZhZjk0MjRiYTg4YmMyNzY5ZWYwM2QzNmY3NTY4ZGY3OTU1ZDU2ZDcifX19");
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Green Fountain");
        meta.setLore(FancyFirework.getPlugin().getConfig().getStringList("itemlore"));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    protected Permission createPermission() {
        Permission permission = new Permission("fancyfirework.use.fountain_green");
        permission.setDefault(PermissionDefault.TRUE);
        plugin.registerPermission(permission);
        return permission;
    }

    @Override
    public Color randomColor() {
        int red = (int) (Math.random() * 20);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 30);
        return Color.fromRGB(red, green, blue);
    }

}



