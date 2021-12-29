package de.fanta.fancyfirework.fireworks;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.FireWorksRegistry;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatteryBlue;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatteryColorSwitch;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatteryGreen;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatteryOrange;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatteryRainbow;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatteryRed;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatterySimple;

public record FireWorkRegistration(FancyFirework plugin) {

    public void registerFirework() {
        FireWorksRegistry registry = plugin.getRegistry();
        registry.register(new FireWorkBatterySimple());
        registry.register(new FireWorkBatteryGreen());
        registry.register(new FireWorkBatteryRed());
        registry.register(new FireWorkBatteryBlue());
        registry.register(new FireWorkBatteryOrange());
        registry.register(new FireWorkBatteryRainbow());
        registry.register(new FireWorkBatteryColorSwitch());
    }
}
