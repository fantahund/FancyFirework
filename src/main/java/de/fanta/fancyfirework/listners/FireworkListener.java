package de.fanta.fancyfirework.listners;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.fireworks.AbstractFireWork;
import de.fanta.fancyfirework.fireworks.BlockFireWork;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class FireworkListener implements Listener {

    private final FancyFirework plugin = FancyFirework.getPlugin();
    private final String FIREWORK_META_KEY = "firework_type";

    @EventHandler
    public void onFireworkExplode(FireworkExplodeEvent e) {
        if (plugin.getFireWorkWorks().enabled()) { //TODO Check is fancyfirework firework
            Entity entity = e.getEntity();
            if (entity.hasMetadata("FancyFirework")) {
                entity.getLocation().getWorld().dropItem(entity.getLocation(), plugin.getRegistry().getRandomFireWork());
            }
        }
    }

    @EventHandler
    public void onFireWorkPlace(BlockPlaceEvent event) {
        ItemStack stack = event.getItemInHand();
        AbstractFireWork fireWork = plugin.getRegistry().getByItemStack(stack);
        if (fireWork instanceof BlockFireWork blockFireWork) {
            Block block = event.getBlockPlaced();
            Entity entity = block.getWorld().spawnEntity(block.getLocation(), EntityType.MARKER);
            entity.setMetadata(FIREWORK_META_KEY, new FixedMetadataValue(plugin, fireWork.getKey().asString()));
            blockFireWork.onPlace(block, event.getPlayer());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            ItemStack item = event.getItem();
            if (item != null && item.getType().equals(Material.FLINT_AND_STEEL)) {
                Block block = event.getClickedBlock();
                assert block != null;
                AbstractFireWork fireWork = plugin.getRegistry().getAtBlock(block);
                if (fireWork instanceof BlockFireWork blockFireWork) {
                    blockFireWork.onLit(block, event.getPlayer());
                    event.setCancelled(true);
                }
            }
        }
    }
}
