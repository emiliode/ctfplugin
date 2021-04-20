package de.emilio.ctf;

import de.emilio.ctf.commands.CommandHello;
import de.emilio.ctf.commands.CommandSetTeams;
import de.emilio.ctf.commands.CommandjoinTeam;
import io.netty.util.Timeout;
import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin  {
    @Override
    public void onEnable(){
        Game game = new Game();
        System.out.println(game);
        this.getServer().getPluginManager().registerEvents(new MyListener(game),this);
        this.getCommand("hello").setExecutor(new CommandHello());
        this.getCommand("setteams").setExecutor(new CommandSetTeams(game));
        this.getCommand("join").setExecutor(new CommandjoinTeam(game));
        Bukkit.getConsoleSender().sendMessage("HAllo bitte funktioniert");
        if (!Bukkit.getOnlinePlayers().isEmpty()){
            for (Player online: Bukkit.getOnlinePlayers()){
                //MyListener.createBoard(online);
            }
        }
        new BukkitRunnable() {

            @Override
            public void run() {
                //The code inside will be executed in {timeInTicks} ticks.
                //After that, it'll be re-executed every {timeInTicks} ticks;
                //Task can also cancel itself from running, if you want to.
                int seconds = (int) System.currentTimeMillis()*1000;
                for (Player online:
                     Bukkit.getOnlinePlayers()) {
                    if(game.Timeoutmap.containsKey(online.getName())){
                        long remaining = (int)(game.Timeoutmap.get(online.getName())*1000)-seconds;
                        if (remaining>10){
                            online.sendMessage(ChatColor.BLUE+"Respwan in: "+ChatColor.BOLD+String.valueOf((int) remaining).substring(0,2));
                            online.sendTitle("","");
                        }else if (remaining>1){
                            online.sendMessage(ChatColor.RED+"Respwan in: "+ChatColor.BOLD+String.valueOf((int) remaining).substring(0,1));
                        }
                        else {
                            game.Timeoutmap.remove(online.getName());
                            online.teleport(Bukkit.getWorlds().get(0).getSpawnLocation().add(0,2,0));
                            online.setGameMode(GameMode.SURVIVAL);
                        }
                    }
                }

            }
        }.runTaskTimer(this, 0, 20);
    }



    @Override
    public void onDisable(){

    }


}
