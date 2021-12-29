package de.fanta.fancyfirework.utils;

import org.bukkit.Color;

public class ColorUtils {

    private ColorUtils() {
    }

    public static Color getColor(long time, double speed) {

        float h = Math.floorMod((int) (time * speed * 21), 360);
        float s = 1;
        float v = 1;

        float c = v * s;
        float x = c * (1 - Math.abs((h / 60) % 2 - 1));
        float m = v - c;

        float r = 0, g = 0, b = 0;
        if (0 <= h && h < 60) {
            r = c;
            g = x;
            b = 0;
        } else if (60 <= h && h < 120) {
            r = x;
            g = c;
            b = 0;
        } else if (120 <= h && h < 180) {
            r = 0;
            g = c;
            b = x;
        } else if (180 <= h && h < 240) {
            r = 0;
            g = x;
            b = c;
        } else if (240 <= h && h < 300) {
            r = x;
            g = 0;
            b = c;
        } else if (300 <= h && h < 360) {
            r = c;
            g = 0;
            b = x;
        }
        return Color.fromRGB((int) ((r + m) * 255), (int) ((g + m) * 255), (int) ((b + m) * 255));
    }

    public static int blend(int color1, int color2, double ratio) {
        if (ratio > 1.0) {
            ratio = 1.0;
        } else if (ratio < 0.0) {
            ratio = 0.0;
        }
        double ratio2 = 1.0 - ratio;

        int a1 = (color1 >> 24) & 0xff;
        int r1 = (color1 >> 16) & 0xff;
        int g1 = (color1 >> 8) & 0xff;
        int b1 = color1 & 0xff;

        int a2 = (color2 >> 24) & 0xff;
        int r2 = (color2 >> 16) & 0xff;
        int g2 = (color2 >> 8) & 0xff;
        int b2 = color2 & 0xff;

        int a = (int) ((a1 * ratio2) + (a2 * ratio));
        int r = (int) ((r1 * ratio2) + (r2 * ratio));
        int g = (int) ((g1 * ratio2) + (g2 * ratio));
        int b = (int) ((b1 * ratio2) + (b2 * ratio));

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
