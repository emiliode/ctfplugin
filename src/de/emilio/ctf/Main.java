package de.emilio.ctf;

import de.emilio.ctf.commands.*;
import io.netty.util.Timeout;
import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin  {
    @Override
    public void onEnable(){
        Game game = new Game(this);
        if(this.getConfig() == null){
            this.saveDefaultConfig();
        }
        getConfig();
        getConfig().options().copyDefaults(true);
        getConfig().set("variables.debug",true);
        saveConfig();
        game.pointstowin = getConfig().getInt("variables.pointsTowin");
        //System.out.println(game.pointstowin);
        game.useHelmet = getConfig().getBoolean("variables.useHelmet");
        this.getServer().getPluginManager().registerEvents(new MyListener(game, this),this);
        this.getCommand("hello").setExecutor(new CommandHello(game));
        this.getCommand("setteams").setExecutor(new CommandSetTeams(game));
        this.getCommand("start").setExecutor(new CommandStart(game));
        this.getCommand("join").setExecutor(new CommandjoinTeam(game));
        this.getCommand("ready").setExecutor(new CommandReady(game,this));
        this.getCommand("setrespawn").setExecutor(new CommandSetRespawn(game));
        Bukkit.getConsoleSender().sendMessage("Hallo bin ich da??");
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