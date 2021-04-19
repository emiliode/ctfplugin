package de.emilio.ctf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

public class Main extends JavaPlugin  {
    @Override
    public void onEnable(){
        this.getServer().getPluginManager().registerEvents(new MyListener(),this);
        this.getCommand("hello").setExecutor(new CommandHello());
        Bukkit.getConsoleSender().sendMessage("HAllo bitte funktioniert");
        if (!Bukkit.getOnlinePlayers().isEmpty()){
            for (Player online: Bukkit.getOnlinePlayers()){
                MyListener.createBoard(online);
            }
        }
    }
    // hello <--- hey welcome!


    @Override
    public void onDisable(){

    }


}
