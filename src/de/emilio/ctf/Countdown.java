package de.emilio.ctf;

import com.connorlinfoot.titleapi.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {
    // 1 second = 20 ticks
    private int  interval,end,current;
    boolean down;
    private Game game;
    public Countdown(int start, int end,int interval,boolean down, Game game){
        this.current = start;
        this.interval = interval;
        this.end = end;
        this.down = down;
        this.game = game;
    }
    @Override
    public void run() {
        for(Player player: Bukkit.getOnlinePlayers()){
            TitleAPI.sendTitle(player,0,20,0,""+current,"");

        }
        if(down){
            current=current-interval;
            if(current<=end){
                game.startPvP();
                this.cancel();
            }
        }else{
            current=current+interval;
            if(current>=end){
                this.cancel();
            }
        }
    }
}
