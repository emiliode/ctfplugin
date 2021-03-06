package de.emilio.ctf.commands;

import de.emilio.ctf.Game;
import de.emilio.ctf.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandStart implements CommandExecutor {
    private Game game;
    public CommandStart(Game game){
        this.game = game;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(game.started){
            commandSender.sendMessage("The game has alreay started use /ready to start pvp");
        }
        if(game.teams == null){
            commandSender.sendMessage(ChatColor.RED+"Create some teams first !");
        }
        for (Team team:
                game.teams) {
            if(team.getTeamRespawn() == null){
                commandSender.sendMessage("You must set the team respawns first");
                return true;
            }
            for (String playername:
                 team.getPlayers()) {
                Player player = Bukkit.getPlayer(playername);
                player.teleport(team.getTeamRespawn());
            }
        }
        game.started = true;
        return true;
    }
}
