package de.fanta.fancyfirework.listners;

import de.fanta.fancyfirework.FancyFirework;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public record EventRegistration(FancyFirework plugin) {

    public static final PluginManager pM = Bukkit.getPluginManager();

    public void registerEvents() {
        pM.registerEvents(new FireworkListener(), plugin);
    }
}
