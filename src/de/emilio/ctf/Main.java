package de.emilio.ctf;

import de.emilio.ctf.commands.CommandHello;
import de.emilio.ctf.commands.CommandSetTeams;
import de.emilio.ctf.commands.CommandjoinTeam;
import org.bukkit.Bukkit;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
                MyListener.createBoard(online);
            }
        }
    }



    @Override
    public void onDisable(){

    }


}
