package de.fanta.fancyfirework;

import de.fanta.fancyfirework.commands.CommandRegistration;
import de.fanta.fancyfirework.fireworks.FireWorkRegistration;
import de.fanta.fancyfirework.listners.AFKListener;
import de.fanta.fancyfirework.listners.EventRegistration;
import de.fanta.fancyfirework.utils.ChatUtil;
import de.fanta.fancyfirework.utils.WorldGuardHelper;
import de.myzelyam.supervanish.SuperVanishPlugin;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.vanish.VanishPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class FancyFirework extends JavaPlugin {

    public static final String PREFIX = ChatUtil.BLUE + "[" + ChatUtil.GREEN + "FancyFirework" + ChatUtil.BLUE + "]";
    public static final String ADMIN_PERMISSION = "fancyfirework.admin";
    public static final String MOD_PERMISSION = "fancyfirework.mod";
    public static Logger LOGGER;
    private static FancyFirework plugin;
    private FireWorkWorks fireWorkWorks;
    private FireWorksRegistry registry;
    private VanishPlugin vanishNoPacketPlugin;
    private SuperVanishPlugin superVanishPlugin;
    private WorldGuardHelper worldGuardHelper;
    private long time;
    private int taskId;

    private boolean HAS_PLAYER_PROFILE_API;

    public static FancyFirework getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        LOGGER = getLogger();
        plugin = this;

        if (plugin.getServer().getPluginManager().getPlugin("VanishNoPacket") != null) {
            vanishNoPacketPlugin = (VanishPlugin) plugin.getServer().getPluginManager().getPlugin("VanishNoPacket");
        }

        //Super Vanish and Premium Vanish has not tested!
        if (plugin.getServer().getPluginManager().getPlugin("SuperVanish") != null) {
            superVanishPlugin = (SuperVanishPlugin) plugin.getServer().getPluginManager().getPlugin("SuperVanish");
        }
        if (plugin.getServer().getPluginManager().getPlugin("PremiumVanish") != null) {
            superVanishPlugin = (SuperVanishPlugin) plugin.getServer().getPluginManager().getPlugin("PremiumVanish");
        }

        if (plugin.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            worldGuardHelper = new WorldGuardHelper(getServer().getPluginManager().getPlugin("WorldGuard"));
        }

        try {
            SkullMeta.class.getDeclaredMethod("getPlayerProfile");
            setPlayerProfileAPI(true);
        } catch (Exception e) {
            getLogger().log(Level.INFO, "Server version spigot. We recommend to use Paper.");
            setPlayerProfileAPI(false);
        }

        new bStats(this).registerbStats();

        fireWorkWorks = new FireWorkWorks();
        this.registry = new FireWorksRegistry(this);

        new CommandRegistration(this).registerCommands();
        new EventRegistration(this).registerEvents();
        new FireWorkRegistration(this).registerFirework();

        loadConfig();
        saveConfig();

        this.taskId = -1;
        this.time = 0;
        this.restartTask(1);

        //AFK
        AFKListener.maxIdleTime = Math.max(plugin.getConfig().getInt("maxAFKTime"), 2) * 1000;

        getServer().getScheduler().scheduleSyncRepeatingTask(this, AFKListener::onTimer, 2 * 20, 10);

        for (Player p : getServer().getOnlinePlayers()) {
            AFKListener.handleJoin(p);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public FireWorkWorks getFireWorkWorks() {
        return fireWorkWorks;
    }

    public FireWorksRegistry getRegistry() {
        return registry;
    }

    public boolean isVanish(Player p) {
        if (vanishNoPacketPlugin != null) {
            return vanishNoPacketPlugin.getManager().isVanished(p);
        } else if (superVanishPlugin != null) {
            return superVanishPlugin.getVanishStateMgr().isVanished(p.getUniqueId());
        } else {
            return false;
        }
    }

    public void restartTask(long l) {
        if (this.taskId != -1) {
            this.getServer().getScheduler().cancelTask(this.taskId);
        }
        this.taskId = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> FancyFirework.this.time++, 0L, l);
    }

    public long getTime() {
        return this.time;
    }

    public boolean canBuild(Player player, Location loc) {
        if (worldGuardHelper == null) {
            return true;
        } else {
            return worldGuardHelper.canBuild(player, loc);
        }
    }

    public boolean hasPlayerProfileAPI() {
        return HAS_PLAYER_PROFILE_API;
    }

    public void setPlayerProfileAPI(boolean HAS_PLAYER_PROFILE_API) {
        this.HAS_PLAYER_PROFILE_API = HAS_PLAYER_PROFILE_API;
    }

    public void loadConfig() {
        FileConfiguration cfg = getConfig();
        cfg.options().copyDefaults(true);
    }
}
