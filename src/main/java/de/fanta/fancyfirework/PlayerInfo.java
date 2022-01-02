package de.fanta.fancyfirework;

public class PlayerInfo {
    public long accounted_time;

    public long last_check_time;

    public long last_move_time;

    public boolean afk;

    public PlayerInfo() {
        last_check_time = System.currentTimeMillis();
        last_move_time = last_check_time;
        accounted_time = 0;
        afk = false;
    }
}
