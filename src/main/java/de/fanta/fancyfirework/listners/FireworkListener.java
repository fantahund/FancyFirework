package de.fanta.fancyfirework.listners;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.fireworks.AbstractFireWork;
import de.fanta.fancyfirework.fireworks.BlockFireWork;
import de.iani.cubesideutils.RandomUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class FireworkListener implements Listener {

    private final FancyFirework plugin = FancyFirework.getPlugin();

    @EventHandler
    public void onFireworkExplode(FireworkExplodeEvent e) {
        if (plugin.getFireWorkWorks().enabled()) { //TODO Check is fancyfirework firework
            Entity entity = e.getEntity();
            if (entity.hasMetadata("FancyFirework")) {
                entity.getLocation().getWorld().dropItem(entity.getLocation(), plugin.getRegistry().getRandomFireWorkItem());
            }
        }
    }

    @EventHandler
    public void onFireWorkPlace(BlockPlaceEvent event) {
        ItemStack stack = event.getPlayer().getEquipment().getItem(event.getHand());
        AbstractFireWork fireWork = plugin.getRegistry().getByItemStack(stack);
        if (fireWork instanceof BlockFireWork blockFireWork) {
            Block block = event.getBlockPlaced();
            blockFireWork.onPlace(block, blockFireWork.spawnAtBlock(block.getRelative(BlockFace.DOWN)), event.getPlayer());
            event.setCancelled(true);
            stack.setAmount(stack.getAmount() - 1);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity instanceof ArmorStand stand) {
            AbstractFireWork fireWork = plugin.getRegistry().getFromArmorStand(stand);
            if (fireWork instanceof BlockFireWork blockFireWork) {
                if(!blockFireWork.hasActiveTask(stand)) {
                    ItemStack stack = event.getPlayer().getEquipment().getItem(event.getHand());
                    if (stack.getType().equals(Material.FLINT_AND_STEEL)) {
                        blockFireWork.onLit(stand, event.getPlayer());
                        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                            ItemMeta meta = stack.getItemMeta();
                            int maxDurability = stack.getType().getMaxDurability();
                            if (maxDurability > 0) {
                                int durability = meta.getEnchantLevel(Enchantment.DURABILITY);
                                if (durability <= 0 || RandomUtil.SHARED_RANDOM.nextInt(durability + 1) == 0) {
                                    Damageable damageableMeta = (Damageable) meta;
                                    int damageOld = damageableMeta.getDamage();
                                    if (damageOld + 1 <= maxDurability) {
                                        damageableMeta.setDamage(damageOld + 1);
                                        stack.setItemMeta(meta);
                                        if (event.getHand() == EquipmentSlot.HAND) {
                                            event.getPlayer().getInventory().setItemInMainHand(stack);
                                        } else {
                                            event.getPlayer().getInventory().setItemInOffHand(stack);
                                        }
                                    } else {
                                        event.getPlayer().getWorld().playSound(event.getPlayer().getLocation().add(0.5, 0.5, 0.5), Sound.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1 + (float) Math.random() * 0.1f, 1 + (float) Math.random() * 0.1f);
                                        if (event.getHand() == EquipmentSlot.HAND) {
                                            event.getPlayer().getInventory().setItemInMainHand(null);
                                        } else {
                                            event.getPlayer().getInventory().setItemInOffHand(null);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                event.setCancelled(true);
            }
        }
    }
}
