package de.emilio.ctf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.bossbar.BossBar;
import org.inventivetalent.bossbar.BossBarAPI;

public class Counter extends BukkitRunnable {
    private Game game;
    public Counter(Game game){
        this.game = game;
    }
    @Override
    public void run() {
        long seconds = System.currentTimeMillis()/1000;
        for (Player online:
                Bukkit.getOnlinePlayers()) {
            if(game.Timeoutmap.containsKey(online.getName())){
                long remaining = game.Timeoutmap.get(online.getName())-seconds;
                BossBar bar =game.Barmap.get(online.getName());
                System.out.println(remaining);
                //float tmp = bar.getProgress();
                //System.out.println(tmp);
                //bar.setProgress(tmp-0.05f);
                if (remaining>10){
                    online.sendMessage(ChatColor.BLUE+"Respwan in: "+ChatColor.BOLD+String.valueOf((int) remaining).substring(0,2));
                    bar.setMessage(ChatColor.BLUE+"Respwan in: "+ChatColor.BOLD+String.valueOf((int) remaining).substring(0,2));

                }else if (remaining >0 ){
                    bar.setColor(BossBarAPI.Color.RED);
                    online.sendMessage(ChatColor.RED+"Respwan in: "+ChatColor.BOLD+remaining);
                    bar.setMessage(ChatColor.RED+"Respwan in: "+ChatColor.BOLD+remaining);

                }
                else {

                    BossBarAPI.removeAllBars(online);
                    game.Barmap.remove(online.getName());
                    game.Timeoutmap.remove(online.getName());
                    online.teleport(Bukkit.getWorlds().get(0).getSpawnLocation().add(0,2,0));
                    online.setGameMode(GameMode.SURVIVAL);
                }
            }
        }

    }
}
