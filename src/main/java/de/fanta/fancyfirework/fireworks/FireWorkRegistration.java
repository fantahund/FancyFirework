package de.fanta.fancyfirework.fireworks;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.FireWorksRegistry;
import de.fanta.fancyfirework.fireworks.defaults.BangSnap;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatteryBirthday;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatteryBlue;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatteryPink;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatteryPride;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatteryGreen;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatteryOrange;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatteryPurple;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatteryRainbow;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatteryRed;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatterySimple;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatteryValentine;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkBatteryYellow;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkFountainBlue;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkFountainGreen;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkFountainOrange;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkFountainPink;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkFountainPride;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkFountainPurple;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkFountainRainbow;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkFountainRed;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkFountainValentine;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkFountainWhite;
import de.fanta.fancyfirework.fireworks.defaults.FireWorkFountainYellow;
import de.fanta.fancyfirework.fireworks.defaults.FlashBomb;
import de.fanta.fancyfirework.fireworks.defaults.SmokeBomb;
import de.fanta.fancyfirework.fireworks.defaults.SmokeBombColor;

public record FireWorkRegistration(FancyFirework plugin) {

    public void registerFirework() {
        FireWorksRegistry registry = plugin.getRegistry();
        registry.register(new FireWorkBatterySimple());
        registry.register(new FireWorkBatteryGreen());
        registry.register(new FireWorkBatteryRed());
        registry.register(new FireWorkBatteryBlue());
        registry.register(new FireWorkBatteryOrange());
        registry.register(new FireWorkBatteryRainbow());
        registry.register(new FireWorkBatteryPride());
        registry.register(new FireWorkBatteryPurple());
        registry.register(new FireWorkBatteryPink());
        registry.register(new FireWorkBatteryYellow());
        registry.register(new FireWorkBatteryValentine());
        registry.register(new FireWorkBatteryBirthday()); //Not ready

        registry.register(new FireWorkFountainPride());
        registry.register(new FireWorkFountainWhite());
        registry.register(new FireWorkFountainYellow());
        registry.register(new FireWorkFountainBlue());
        registry.register(new FireWorkFountainGreen());
        registry.register(new FireWorkFountainOrange());
        registry.register(new FireWorkFountainPink());
        registry.register(new FireWorkFountainPurple());
        registry.register(new FireWorkFountainRainbow());
        registry.register(new FireWorkFountainRed());
        registry.register(new FireWorkFountainValentine());

        registry.register(new BangSnap());
        registry.register(new SmokeBomb());
        registry.register(new SmokeBombColor());
        registry.register(new FlashBomb());
    }
}
