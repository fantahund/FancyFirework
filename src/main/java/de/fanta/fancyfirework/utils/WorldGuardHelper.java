package de.fanta.fancyfirework.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WorldGuardHelper {
    private final WorldGuardPlugin worldGuard;

    public WorldGuardHelper(Plugin worldGuard) {
        this.worldGuard = (WorldGuardPlugin) worldGuard;
    }

    public boolean can(Player player, Location location, StateFlag flag) {
        com.sk89q.worldedit.util.Location wgLocation = BukkitAdapter.adapt(location);
        LocalPlayer wgPlayer = worldGuard.wrapPlayer(player);
        if (hasOverride(wgPlayer)) {
            return true;
        }
        RegionQuery query = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        return query.testBuild(wgLocation, wgPlayer, flag);
    }

    public boolean canBuild(Player player, Location location) {
        com.sk89q.worldedit.util.Location wgLocation = BukkitAdapter.adapt(location);
        LocalPlayer wgPlayer = worldGuard.wrapPlayer(player);
        if (hasOverride(wgPlayer)) {
            return true;
        }
        RegionQuery query = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        return query.testBuild(wgLocation, wgPlayer);
    }

    public boolean hasOverride(Player player) {
        return hasOverride(worldGuard.wrapPlayer(player));
    }

    public boolean hasOverride(LocalPlayer wgPlayer) {
        return WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(wgPlayer, wgPlayer.getWorld());
    }

    public WorldGuardPlugin getWorldGuard() {
        return worldGuard;
    }
}
