package de.emilio.ctf.commands;

import com.connorlinfoot.titleapi.TitleAPI;
import de.emilio.ctf.Countdown;
import de.emilio.ctf.Game;

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
                    if (player.hasPermission("none")) {
                        if(game.teams[1].getFlagCords()==null){
                            return true;
                        }

                        player.sendMessage(/*ChatColor.DARK_BLUE + */""  +/*ChatColor.BOLD+*/ "Hey welcome to the server" + game.teams[1].getScore());
                        //TitleAPI.sendTitle(player,2,10,2,"Your Flag was taken","");
                    }else{
                        player.sendMessage("No permission");
                    }
                }else {
                    //console
                    sender.sendMessage("Hey console welcome");
                }
                return true;
    }
}

