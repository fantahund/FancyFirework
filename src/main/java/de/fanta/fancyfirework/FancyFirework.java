package de.fanta.fancyfirework;

import de.fanta.fancyfirework.commands.CommandRegistration;
import de.fanta.fancyfirework.fireworks.FireWorkRegistration;
import de.fanta.fancyfirework.listners.AFKListener;
import de.fanta.fancyfirework.listners.EventRegistration;
import de.fanta.fancyfirework.schedular.BukkitScheduler;
import de.fanta.fancyfirework.schedular.FoliaScheduler;
import de.fanta.fancyfirework.schedular.Scheduler;
import de.fanta.fancyfirework.utils.ChatUtil;
import de.fanta.fancyfirework.utils.WorldGuardHelper;
import de.myzelyam.api.vanish.VanishAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.vanish.VanishPlugin;

import java.util.Set;
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
    private boolean isSuperVanishPluginInstalled = false;
    private WorldGuardHelper worldGuardHelper;
    private long time;
    private boolean redstonemode;

    private boolean disableFireWorkUse = false;

    private boolean HAS_PLAYER_PROFILE_API;
    private Scheduler scheduler;

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

        if (Bukkit.getPluginManager().isPluginEnabled("SuperVanish") || Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) {
            isSuperVanishPluginInstalled = true;
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

        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.ScheduledTask");
            getLogger().log(Level.INFO, "Paper/Folia found. Use Folia Scheduler");
            scheduler = new FoliaScheduler(this);
        } catch (Throwable ignored) {
            getLogger().log(Level.INFO, "Spigot found. Use Bukkit Scheduler");
            scheduler = new BukkitScheduler(this);
        }

        new bStats(this).registerbStats();

        fireWorkWorks = new FireWorkWorks();
        this.registry = new FireWorksRegistry(this);

        new CommandRegistration(this).registerCommands();
        new EventRegistration(this).registerEvents();
        new FireWorkRegistration(this).registerFirework();

        loadConfig();
        saveConfig();

        this.time = 0;
        this.startColorTimeTask(1);

        getScheduler().runGlobalAtFixedRate(AFKListener::onTimer, 2 * 20, 10);

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
        } else if (isSuperVanishPluginInstalled) {
            return VanishAPI.isInvisible(p);
        } else {
            return false;
        }
    }

    public void startColorTimeTask(long l) {
        getScheduler().runGlobalAtFixedRate(() -> FancyFirework.this.time++, 1L, l);
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

    public void reloadFireworkConfig() {
        reloadConfig();
        loadConfig();
    }

    public void loadConfig() {
        FileConfiguration cfg = getConfig();
        cfg.options().copyDefaults(true);
        redstonemode = cfg.getBoolean("redstonemode");

        //AFK
        AFKListener.maxIdleTime = Math.max(plugin.getConfig().getInt("maxAFKTime"), 2) * 1000;
    }

    public boolean isRedstonemode() {
        return redstonemode;
    }

    public boolean isDisableFireWorkUse() {
        return disableFireWorkUse;
    }

    public void setDisableFireWorkUse(boolean disableFireWorkUse) {
        this.disableFireWorkUse = disableFireWorkUse;
    }

    public void registerPermission(Permission permission) {
        PluginManager pm = getServer().getPluginManager();
        Set<Permission> permissions = pm.getPermissions();
        if (!permissions.contains(permission)) {
            pm.addPermission(permission);
        }
    }

    public Scheduler getScheduler() {
        return scheduler;
    }
}
