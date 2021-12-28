package de.fanta.fancyfirework;

import de.fanta.fancyfirework.commands.CommandRegistration;
import de.fanta.fancyfirework.fireworks.FireWorkBatterySimple;
import de.fanta.fancyfirework.listners.EventRegistration;
import de.fanta.fancyfirework.utils.ChatUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class FancyFirework extends JavaPlugin {

    public static final String PREFIX = ChatUtil.BLUE + "[" + ChatUtil.GREEN + "FancyFirework" + ChatUtil.BLUE + "]";
    public static final String ADMIN_PERMISSION = "fancyfirework.admin";
    public static final String MOD_PERMISSION = "fancyfirework.mod";
    public static Logger LOGGER;

    private FireWorkWorks fireWorkWorks;
    private FireWorksRegistry registry;

    private static FancyFirework plugin;

    @Override
    public void onEnable() {
        LOGGER = getLogger();
        plugin = this;

        new bStats(this).registerbStats();

        fireWorkWorks = new FireWorkWorks();
        this.registry = new FireWorksRegistry(this);

        new CommandRegistration(this).registerCommands();
        new EventRegistration(this).registerEvents();

        saveDefaultConfig();
        reloadConfig();

        registry.register(new FireWorkBatterySimple());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static FancyFirework getPlugin() {
        return plugin;
    }

    public FireWorkWorks getFireWorkWorks() {
        return fireWorkWorks;
    }

    public FireWorksRegistry getRegistry() {
        return registry;
    }
}
