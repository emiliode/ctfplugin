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
                //System.out.println(remaining);
                if (remaining>10){
                    bar.setMessage(ChatColor.BLUE+"Respwan in: "+ChatColor.BOLD+String.valueOf((int) remaining).substring(0,2));

                }else if (remaining >0 ){
                    bar.setColor(BossBarAPI.Color.RED);
                    bar.setMessage(ChatColor.RED+"Respwan in: "+ChatColor.BOLD+remaining);

                }
                else {

                    BossBarAPI.removeAllBars(online);
                    game.Barmap.remove(online.getName());
                    game.Timeoutmap.remove(online.getName());
                    game.SentMap.remove(online.getName());
                    //System.out.println(game.getTeam(online).getTeamRespawn());
                    if(game.getTeam(online)!= null ) {
                        if(game.getTeam(online).getTeamRespawn()!= null) {
                            online.teleport(game.getTeam(online).getTeamRespawn());
                        }else{
                            online.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                        }
                    }else {
                        online.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                    }
                    online.setGameMode(GameMode.SURVIVAL);

                }
            }
        }

    }
}
