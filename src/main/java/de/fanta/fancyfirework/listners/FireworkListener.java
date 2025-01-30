package de.fanta.fancyfirework.listners;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.google.common.base.Objects;
import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.events.FireworkDeathEvent;
import de.fanta.fancyfirework.fireworks.AbstractFireWork;
import de.fanta.fancyfirework.fireworks.BlockFireWork;
import de.fanta.fancyfirework.fireworks.ItemFireWork;
import de.fanta.fancyfirework.utils.ChatUtil;
import de.iani.cubesideutils.RandomUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.data.Directional;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockVector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class FireworkListener implements Listener {

    private final FancyFirework plugin = FancyFirework.getPlugin();

    private final ConcurrentHashMap<BlockVector, Long> dispenserPlaceTimes = new ConcurrentHashMap<>();

    @EventHandler
    public void onFireworkExplode(FireworkExplodeEvent e) {
        Firework entity = e.getEntity();
        AbstractFireWork fireWork = plugin.getRegistry().getByEntity(entity);
        if (fireWork != null) {
            fireWork.onExplode(e.getEntity());
        }
        if (plugin.getFireWorkWorks().enabled()) {
            if (entity.getCustomName() != null && entity.getCustomName().equals("FancyFirework")) {
                entity.getLocation().getWorld().dropItem(entity.getLocation(), plugin.getRegistry().getRandomFireWorkItem());
            }
        }
    }

    @EventHandler
    public void onFireWorkPlace(BlockPlaceEvent event) {
        ItemStack stack = event.getPlayer().getEquipment().getItem(event.getHand());
        AbstractFireWork fireWork = plugin.getRegistry().getByItemStack(stack);
        if (!plugin.canBuild(event.getPlayer(), event.getBlock().getLocation())) {
            return;
        }
        if (fireWork instanceof BlockFireWork blockFireWork) {
            if (!event.getPlayer().hasPermission(blockFireWork.getPermission())) {
                ChatUtil.sendErrorMessage(event.getPlayer(), "You do not have the permission to use this firework here!");
                event.setCancelled(true);
                return;
            }
            if (plugin.isDisableFireWorkUse()) {
                ChatUtil.sendErrorMessage(event.getPlayer(), "Fireworks can not currently be used!");
                event.setCancelled(true);
                return;
            }
            Block block = event.getBlockPlaced();
            blockFireWork.onPlace(block, blockFireWork.spawnAtBlock(block.getRelative(BlockFace.DOWN)), event.getPlayer());
            event.setCancelled(true);
            stack.setAmount(stack.getAmount() - 1);
        }
    }

    @EventHandler
    public void onFireWorkPlaceByDispenser(BlockDispenseEvent event) {
        if (!plugin.isDispenserMode()) {
            return;
        }

        ItemStack stack = event.getItem();
        AbstractFireWork fireWork = plugin.getRegistry().getByItemStack(stack);
        if (!(fireWork instanceof BlockFireWork blockFireWork)) {
            return;
        }

        if (plugin.isDisableFireWorkUse()) {
            return;
        }

        Block block = event.getBlock();
        if (!(block.getState() instanceof org.bukkit.block.Dispenser dispenser)) {
            return;
        }
        event.setCancelled(true);

        plugin.getScheduler().runLocalDelayed(event.getBlock().getLocation(), () -> {
            BlockVector blockVector = block.getLocation().toVector().toBlockVector();
            long lastPlaceTime = dispenserPlaceTimes.getOrDefault(blockVector, 0L);

            if (System.currentTimeMillis() - lastPlaceTime < (plugin.getDispenserDelaSeconds() * 1000L)) {
                return;
            }
            dispenserPlaceTimes.remove(blockVector);

            Inventory dispenserInventory = dispenser.getInventory();
            for (int i = 0; i < dispenserInventory.getSize(); i++) {
                ItemStack item = dispenserInventory.getItem(i);

                if (item != null && item.isSimilar(stack)) {
                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                        dispenserInventory.setItem(i, item);
                    } else {
                        dispenserInventory.setItem(i, null);
                    }
                    break;
                }
            }

            //Spawn and lit firework
            if (block.getBlockData() instanceof Directional directional) {
                Block placeBlock = block.getRelative(directional.getFacing());
                ArmorStand armorStand = blockFireWork.spawnAtBlock(placeBlock.getRelative(BlockFace.DOWN));
                blockFireWork.onDispenserPlace(placeBlock, armorStand);
                blockFireWork.onLit(armorStand, null);
                dispenserPlaceTimes.put(blockVector, System.currentTimeMillis());
            }
        }, 1L);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        AbstractFireWork fireWork = null;
        if (entity instanceof ArmorStand) {
            fireWork = plugin.getRegistry().getByEntity(entity);
        }
        if (plugin.getConfig().getBoolean("litplayer") && entity instanceof Player player) {
            ItemStack fireworkStack = player.getInventory().getHelmet();
            if (fireworkStack != null) {
                fireWork = plugin.getRegistry().getByItemStack(fireworkStack);
            }
        }

        if (fireWork instanceof BlockFireWork blockFireWork) {
            if (!event.getPlayer().hasPermission(blockFireWork.getPermission())) {
                ChatUtil.sendErrorMessage(event.getPlayer(), "You do not have the permission to use this firework here!");
                event.setCancelled(true);
                return;
            }
            if (plugin.isDisableFireWorkUse()) {
                ChatUtil.sendErrorMessage(event.getPlayer(), "Fireworks can not currently be used!");
                event.setCancelled(true);
                return;
            }
            if (!blockFireWork.hasActiveTask(entity)) {
                ItemStack stack = event.getPlayer().getEquipment().getItem(event.getHand());
                if (stack.getType().equals(Material.FLINT_AND_STEEL)) {
                    if (!plugin.canBuild(event.getPlayer(), entity.getLocation().add(0, 1.5, 0))) {
                        return;
                    }
                    blockFireWork.onLit(entity, event.getPlayer());
                    damageFNS(event.getPlayer(), event.getHand());
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent e) {
        ItemStack stack = e.getCurrentItem();
        if (stack != null) {
            AbstractFireWork fireWork = plugin.getRegistry().getByItemStack(stack);
            if (fireWork instanceof BlockFireWork blockFireWork) {
                if (blockFireWork.hasActiveTask(e.getWhoClicked())) {
                    e.setCancelled(true);
                }
            }
        }

        if (e.getClick() == ClickType.NUMBER_KEY) {
            int slot = e.getHotbarButton();
            if (slot >= 0 && slot < 9) {
                if (!e.getWhoClicked().getInventory().equals(e.getInventory())) {
                    ItemStack swap = e.getWhoClicked().getInventory().getItem(slot);
                    if (swap != null) {
                        AbstractFireWork fireWork = plugin.getRegistry().getByItemStack(swap);
                        if (fireWork instanceof BlockFireWork blockFireWork) {
                            if (blockFireWork.hasActiveTask(e.getWhoClicked())) {
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEquip(PlayerInteractEvent e) {
        ItemStack handStack = e.getItem();
        if (handStack == null || e.getHand() == null) {
            return;
        }
        final EquipmentSlot armorItemSlot = handStack.getType().getEquipmentSlot();
        if (armorItemSlot != EquipmentSlot.HEAD) {
            return;
        }

        final PlayerInventory inventory = e.getPlayer().getInventory();
        final ItemStack headStack = inventory.getItem(armorItemSlot);
        AbstractFireWork fireWork = plugin.getRegistry().getByItemStack(headStack);
        if (fireWork instanceof BlockFireWork blockFireWork) {
            if (blockFireWork.hasActiveTask(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ItemStack stack = event.getPlayer().getInventory().getHelmet();
        AbstractFireWork fireWork = plugin.getRegistry().getByItemStack(stack);
        if (fireWork instanceof BlockFireWork blockFireWork) {
            if (blockFireWork.hasActiveTask(event.getPlayer())) {
                BlockFireWork.Task task = blockFireWork.getActiveTask(event.getPlayer());
                if (task != null) {
                    task.stop();
                }
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        ItemStack stack = e.getItemDrop().getItemStack();
        AbstractFireWork fireWork = plugin.getRegistry().getByItemStack(stack);
        if (fireWork instanceof BlockFireWork blockFireWork) {
            if (blockFireWork.hasActiveTask(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteractItem(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack stack = e.getItem();
            if (stack != null) {
                if (stack.getType() == Material.FLINT_AND_STEEL) {
                    Player player = e.getPlayer();
                    if (player.getLocation().getPitch() < -50) {
                        ItemStack fireworkStack = player.getInventory().getHelmet();
                        if (fireworkStack != null) {
                            AbstractFireWork fireWork = plugin.getRegistry().getByItemStack(fireworkStack);
                            if (plugin.getConfig().getBoolean("litplayer") && fireWork instanceof BlockFireWork blockFireWork) {
                                if (!e.getPlayer().hasPermission(blockFireWork.getPermission())) {
                                    ChatUtil.sendErrorMessage(e.getPlayer(), "You do not have the permission to use this firework here!");
                                    e.setCancelled(true);
                                    return;
                                }
                                if (plugin.isDisableFireWorkUse()) {
                                    ChatUtil.sendErrorMessage(e.getPlayer(), "Fireworks can not currently be used!");
                                    e.setCancelled(true);
                                    return;
                                }
                                if (!blockFireWork.hasActiveTask(player)) {
                                    blockFireWork.onLit(player, player);
                                    damageFNS(player, e.getHand());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void damageFNS(Player player, EquipmentSlot slot) {
        ItemStack stack = player.getEquipment().getItem(slot);
        if (player.getGameMode() != GameMode.CREATIVE || stack.getItemMeta().isUnbreakable()) {
            ItemMeta meta = stack.getItemMeta();
            int maxDurability = stack.getType().getMaxDurability();
            if (maxDurability > 0) {
                int durability = meta.getEnchantLevel(Enchantment.UNBREAKING);
                if (durability <= 0 || RandomUtil.SHARED_RANDOM.nextInt(durability + 1) == 0) {
                    Damageable damageableMeta = (Damageable) meta;
                    int damageOld = damageableMeta.getDamage();
                    if (damageOld + 1 <= maxDurability) {
                        damageableMeta.setDamage(damageOld + 1);
                        stack.setItemMeta(meta);
                        if (slot == EquipmentSlot.HAND) {
                            player.getInventory().setItemInMainHand(stack);
                        } else {
                            player.getInventory().setItemInOffHand(stack);
                        }
                    } else {
                        player.getWorld().playSound(player.getLocation().add(0.5, 0.5, 0.5), Sound.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1 + (float) Math.random() * 0.1f, 1 + (float) Math.random() * 0.1f);
                        if (slot == EquipmentSlot.HAND) {
                            player.getInventory().setItemInMainHand(null);
                        } else {
                            player.getInventory().setItemInOffHand(null);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof ArmorStand stand)) {
            return;
        }

        AbstractFireWork fireWork = plugin.getRegistry().getByEntity(stand);
        if (fireWork instanceof BlockFireWork blockFireWork) {

            if (!(e.getDamager() instanceof Player player)) {
                e.setCancelled(true);
                return;
            }

            Location loc = stand.getLocation().add(0, 1.5, 0);
            if (!plugin.canBuild(player, loc)) {
                ChatUtil.sendErrorMessage(player, "You can not build here");
                return;
            }

            if (!blockFireWork.hasActiveTask(stand)) {
                ItemStack stack = blockFireWork.getItemStack();
                List<ItemStack> stackList = new ArrayList<>();
                stackList.add(stack);
                stand.setLastDamageCause(e);
                FireworkDeathEvent fireworkDeathEvent = new FireworkDeathEvent(stand, stackList, e.getDamageSource());
                player.getServer().getPluginManager().callEvent(fireworkDeathEvent);
                if (fireworkDeathEvent.isCancelled()) {
                    e.setCancelled(true);
                    return;
                }
                stand.getWorld().dropItem(loc, stack);
                stand.getEquipment().clear();
                stand.remove();
            }
        }
    }

    @EventHandler
    public void onItemFireworkLaunch(ProjectileLaunchEvent event) {
        Projectile entity = event.getEntity();
        if (entity.getShooter() instanceof Player player) {
            ItemStack hand = player.getEquipment().getItemInMainHand();
            AbstractFireWork fireWork = plugin.getRegistry().getByItemStack(hand);
            if (!(fireWork instanceof ItemFireWork)) {
                fireWork = plugin.getRegistry().getByItemStack(player.getEquipment().getItemInOffHand());
            }
            if (fireWork instanceof ItemFireWork itemFireWork) {
                if (!player.hasPermission(itemFireWork.getPermission())) {
                    ChatUtil.sendErrorMessage(player, "You do not have the permission to use this firework here!");
                    event.setCancelled(true);
                    return;
                }
                if (plugin.isDisableFireWorkUse()) {
                    ChatUtil.sendErrorMessage(player, "Fireworks can not currently be used!");
                    event.setCancelled(true);
                    return;
                }
                fireWork.applyToEntity(entity);
                itemFireWork.onLaunch(player, entity);
            }
        }
    }

    @EventHandler
    public void onItemFireworkHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        if (entity.getShooter() instanceof Player player) {
            AbstractFireWork fireWork = plugin.getRegistry().getByEntity(entity);
            if (fireWork instanceof ItemFireWork itemFireWork) {
                itemFireWork.onHit(player, event);
            }
        }
    }

    @EventHandler
    public void onItemFireworkHit(PlayerEggThrowEvent event) {
        AbstractFireWork fireWork = plugin.getRegistry().getByEntity(event.getEgg());
        if (fireWork instanceof ItemFireWork) {
            event.setHatching(false);
        }
    }

    @EventHandler
    public void onRedstoneSignal(BlockRedstoneEvent e) {
        if (!plugin.isRedstonemode()) {
            return;
        }
        Block block = e.getBlock();
        Location loc = block.getLocation();
        World world = loc.getWorld();
        if (world == null) {
            return;
        }
        Collection<Entity> entities = world.getNearbyEntities(loc.add(0.5, 0.5, 0.5), 1.5, 1, 1.5);
        for (Entity entity : entities) {
            if (entity instanceof ArmorStand stand) {
                AbstractFireWork fireWork = plugin.getRegistry().getByEntity(stand);
                if (fireWork instanceof BlockFireWork blockFireWork) {
                    if (!blockFireWork.hasActiveTask(stand)) {
                        if (e.getNewCurrent() > e.getOldCurrent()) {
                            blockFireWork.onLit(stand, null);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onWanderingTraderSpawn(EntitySpawnEvent e) {
        if (!(e.getEntity() instanceof WanderingTrader trader)) {
            return;
        }

        if (!plugin.getConfig().getBoolean("loottable.wanderingtrader.enabled")) {
            return;
        }

        Random random = new Random();
        double fireworkChance = plugin.getConfig().getDouble("loottable.wanderingtrader.chance", 0.3);
        boolean hasFirework = random.nextDouble() < fireworkChance;

        if (!hasFirework) {
            return;
        }

        List<MerchantRecipe> recipes = new ArrayList<>(trader.getRecipes());

        int minprice = plugin.getConfig().getInt("loottable.wanderingtrader.minprice");
        int maxprice = plugin.getConfig().getInt("loottable.wanderingtrader.maxprice");
        int maxuse = plugin.getConfig().getInt("loottable.wanderingtrader.maxuse");

        MerchantRecipe recipe = new MerchantRecipe(plugin.getRegistry().getRandomFireWorkItem(), 0, maxuse, true);
        recipe.addIngredient(new ItemStack(Material.EMERALD, minprice + random.nextInt(maxprice - minprice)));
        recipes.add(random.nextInt(recipes.size() + 1), recipe);
        trader.setRecipes(recipes);
    }

    @EventHandler
    public void InventoryOpenEvent(InventoryOpenEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof BlockInventoryHolder || holder instanceof DoubleChest || e.getInventory().getType() == InventoryType.ENDER_CHEST) {
            int modifyCount = 0;
            ItemStack[] storage = e.getInventory().getStorageContents();
            for (int i = 0; i < storage.length; i++) {
                ItemStack newStack = plugin.getFireWorkWorks().fixFirework(storage[i]);
                if (!Objects.equal(storage[i], newStack)) {
                    storage[i] = newStack;
                    modifyCount++;
                }
            }
            if (modifyCount > 0) {
                e.getInventory().setStorageContents(storage);
            }
        }
    }

    @EventHandler
    public void EntityAddToWorld(EntityAddToWorldEvent e) {
        Entity entity = e.getEntity();
        AbstractFireWork fireWork = plugin.getRegistry().getByEntity(entity);
        if (fireWork instanceof BlockFireWork blockFireWork) {
            if (blockFireWork.hasActiveTask(entity)) {
                plugin.getScheduler().runOnEntityDelayed(entity, () -> blockFireWork.getActiveTask(entity).stop(), 1);
            }
        }
    }
}
