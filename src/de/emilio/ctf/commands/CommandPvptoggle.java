package de.emilio.ctf.commands;

import de.emilio.ctf.Game;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandPvptoggle implements CommandExecutor {
    private Game game;
    public CommandPvptoggle(Game game){
        this.game = game;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(game.pvp){
            game.pvp = false;
        }else{
            game.pvp = true;
        }
        return true;
    }
}
