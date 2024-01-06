package de.fanta.fancyfirework.utils;

import com.Acrobot.ChestShop.ItemNaming.PriorityItemDisplayNameShortener;

public class FancyFireworkPriorityItemDisplayNameShortener extends PriorityItemDisplayNameShortener {

    public FancyFireworkPriorityItemDisplayNameShortener() {
        addMapping(0, "Battery", "Batt");
        addMapping(1, "Batt", "Ba");

        addMapping(0, "Fountain", "Foun");
        addMapping(1, "Foun", "Fo");

        addMapping(0, "Bomb", "Bo");
    }
}
