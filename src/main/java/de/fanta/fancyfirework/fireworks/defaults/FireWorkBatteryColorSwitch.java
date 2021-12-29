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

public class FireWorkBatteryColorSwitch extends FireWorkBattery {

    private final FancyFirework plugin = FancyFirework.getPlugin();

    public FireWorkBatteryColorSwitch() {
        super(new NamespacedKey(FancyFirework.getPlugin(), "battery_colorswitch"));
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = CustomHeads.createHead(UUID.fromString("67e8c714-b4f1-4feb-8034-4a693723fdae"), "Easter Egg (rainbow)", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2JlNzU0NTI5N2RmZDYyNjZiYmFhMjA1MTgyNWU4ODc5Y2JmYTQyYzdlN2UyNGU1MDc5NmYyN2NhNmExOCJ9fX0=");
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.of("#eb000c") + "Co" + ChatColor.of("#f24206") + "lo" + ChatColor.of("#fa8500") + "r S" + ChatColor.of("#f7ac00") + "wi" + ChatColor.of("#f5d400") + "tc" + ChatColor.of("#7ae920") + "h B" + ChatColor.of("#00ff40") + "at" + ChatColor.of("#00aa9f") + "te" + ChatColor.of("#0055ff") + "r" + ChatColor.of("#a600ff") + "y");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public void onPlace(Block block, ArmorStand stand, Player player) {
        //Nothing
    }

    @Override
    public void onLit(ArmorStand stand, Player player) {
        super.onLit(stand, player);
        BatteryTask task = new BatteryTask(player, stand, 20 * 60, 20 * 5, 20, () -> spawnRandomFirework(stand.getLocation()));
        task.setFountainFunction(batteryTask -> List.of(new FountainEffect(random.nextInt(6, 20), random.nextDouble(0.4, 1), random.nextDouble(359))));
        task.start();
    }

    @Override
    public Color randomColor() {
        return ColorUtils.getColor(plugin.getTime(), 2);
    }
}



