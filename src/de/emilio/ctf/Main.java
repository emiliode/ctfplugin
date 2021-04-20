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
        new Counter(game).runTaskTimer(this, 0, 20);
    }



    @Override
    public void onDisable(){

    }


}
