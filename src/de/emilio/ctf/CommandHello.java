package de.emilio.ctf;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHello implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
                if(sender instanceof Player){
                    //player
                    Player player = (Player) sender;
                    if (player.hasPermission("none")) {
                        player.sendMessage(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Hey welcome to the server");
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

