package de.fanta.fancyfirework.listners;

import com.Acrobot.ChestShop.Events.PreShopCreationItemDisplayNameEvent;
import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.fireworks.AbstractFireWork;
import de.fanta.fancyfirework.utils.FancyFireworkPriorityItemDisplayNameShortener;
import de.iani.cubesideutils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class ChestShopListener implements Listener {
    private final FancyFirework plugin;
    private final FancyFireworkPriorityItemDisplayNameShortener fancyFireworkPriorityItemDisplayNameShortener;

    public ChestShopListener(FancyFirework plugin) {
        this.plugin = plugin;
        this.fancyFireworkPriorityItemDisplayNameShortener = new FancyFireworkPriorityItemDisplayNameShortener();
    }

    @EventHandler
    public void onShopDisplayNaming(PreShopCreationItemDisplayNameEvent preShopCreationItemDisplayNameEvent) {
        ItemStack itemStack = preShopCreationItemDisplayNameEvent.getItemStack();
        AbstractFireWork fireWork = plugin.getRegistry().getByItemStack(itemStack);
        if (fireWork != null) {
            String displayName = fireWork.getItemStack().getItemMeta().getDisplayName();
            String convertColorName = StringUtil.revertColors(displayName);
            String displayNameWithoutColor = StringUtil.stripColors(convertColorName);
            preShopCreationItemDisplayNameEvent.setDisplayName(displayNameWithoutColor, fancyFireworkPriorityItemDisplayNameShortener);
        }
    }
}
