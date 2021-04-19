package de.emilio.ctf;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetTeams implements CommandExecutor {
    Game game;
    final private ChatColor[] colors = {ChatColor.BLUE, ChatColor.RED , ChatColor.LIGHT_PURPLE ,ChatColor.WHITE};
    public  CommandSetTeams(Game game){
        this.game = game;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("ctf.admin")){
            if(args.length != 1){
                sender.sendMessage("USAGE: /setteams <number of teams> ");
                return false;
            }else {
                int numteams;
                try {
                    numteams = Integer.parseInt(args[0]);
                    if (numteams> colors.length){
                        sender.sendMessage("Too many teams");
                    }
                } catch ( NumberFormatException exception){
                    sender.sendMessage("Invalid Number");
                    return false;
                }
                Team[] teams = new Team[numteams];
                for (int i=0; i< numteams; i++){
                    teams[i] = new Team(colors[i], 0,i);
                }
                this.game.teams = teams;
                this.game.printTeams();
            }
        }else {
            sender.sendMessage("You don't have enough permissions");
        }
        return true;
    }
}