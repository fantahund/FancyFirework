package de.fanta.fancyfirework.listners;

import de.fanta.fancyfirework.FancyFirework;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootTableListener implements Listener {
    private final FancyFirework plugin = FancyFirework.getPlugin();
    private final List<Location> lootTableSaveList = new ArrayList<>();

    @EventHandler
    public void onPlayerUseLootChest(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }

        if (!(block.getState() instanceof Chest chest)) {
            return;
        }

        if (chest.getLootTable() == null) {
            return;
        }

        if (plugin.getConfig().getBoolean("loottable.enabled")) {
            addFireworktoLootChest(chest.getInventory());
        }
    }

    @EventHandler
    public void onPlayerUseEntityLootChest(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();

        if (!(entity instanceof StorageMinecart minecart)) {
            return;
        }

        if (minecart.getLootTable() == null) {
            return;
        }

        if (plugin.getConfig().getBoolean("loottable.enabled")) {
            addFireworktoLootChest(minecart.getInventory());
        }

    }

    public void addFireworktoLootChest(Inventory chest) {
        if (lootTableSaveList.contains(chest.getLocation())) {
            return;
        }
        lootTableSaveList.add(chest.getLocation());
        Random random = new Random();
        double chance = plugin.getConfig().getDouble("loottable.chance", 0.5);
        boolean hasFireWork = random.nextDouble() < chance;
        if (hasFireWork) {
            ItemStack randomFirework = plugin.getRegistry().getRandomFireWorkItem();
            plugin.getScheduler().runLocalDelayed(chest.getLocation(), () -> {
                List<Integer> freeSlots = new ArrayList<>();
                for (int i = 0; i < chest.getSize(); i++) {
                    if (chest.getItem(i) == null) {
                        freeSlots.add(i);
                    }
                }
                if (!freeSlots.isEmpty()) {
                    chest.setItem(freeSlots.get(random.nextInt(freeSlots.size())), randomFirework);
                }
            }, 1L);
        }
    }
}
