package de.fanta.fancyfirework;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;

public record bStats(FancyFirework plugin) {

    public void registerbStats() {
        int pluginId = 13728;
        Metrics metrics = new Metrics(plugin, pluginId);
        metrics.addCustomChart(new SimplePie("enabled", () -> String.valueOf(plugin.getFireWorkWorks().enabled())));
        metrics.addCustomChart(new SimplePie("vote_reward", () -> String.valueOf(plugin.getConfig().getBoolean("votereward"))));
        metrics.addCustomChart(new SimplePie("loottable", () -> String.valueOf(plugin.getConfig().getBoolean("loottable.enabled"))));
        metrics.addCustomChart(new SimplePie("redstone_mode", () -> String.valueOf(plugin.getConfig().getBoolean("redstonemode"))));
    }
}


