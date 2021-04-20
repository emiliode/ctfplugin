package de.emilio.ctf.commands;

import de.emilio.ctf.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class CommandjoinTeam implements CommandExecutor {
    private Game game;
    public CommandjoinTeam(Game game){
        this.game = game;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("You must be a player too join a team");
            return true;
        }
        Player player = (Player) sender;
        // open GUI
        if(!(game.inv == null)) {
            player.openInventory(game.inv);
            return true;
        }
        player.sendMessage("You must create some teams first");
        return false;
    }

}
