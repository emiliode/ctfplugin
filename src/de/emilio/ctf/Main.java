package de.emilio.ctf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable(){
        Bukkit.getConsoleSender().sendMessage("HAllo bitte funktioniert");
    }
    // hello <--- hey welcome!

    public boolean onCommand(CommandSender sender, Command cmd,String label, String[] args){
        if (label.equalsIgnoreCase("hello")){
            if(sender instanceof Player){
                //player
                Player player = (Player) sender;
                if (player.hasPermission("hello.use")) {
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
        return true;
    }

}
