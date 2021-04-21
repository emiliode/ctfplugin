package de.emilio.ctf.commands;

import com.connorlinfoot.titleapi.TitleAPI;
import de.emilio.ctf.Countdown;
import de.emilio.ctf.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHello implements CommandExecutor {
    private Game game;
    public CommandHello(Game game){
        this.game = game;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
                if(sender instanceof Player) {
                    //player
                    Player player = (Player) sender;
                }
                return true;
    }
}

