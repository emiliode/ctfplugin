package de.emilio.ctf.commands;

import com.connorlinfoot.titleapi.TitleAPI;
import com.mysql.jdbc.Buffer;
import de.emilio.ctf.Countdown;
import de.emilio.ctf.Game;
import de.emilio.ctf.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandReady implements CommandExecutor {
    private Game game;
    private JavaPlugin plugin;
    public CommandReady(Game game,JavaPlugin plugin){
        this.game = game;
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!game.started){
            sender.sendMessage("You must start the game first");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to do this");
            return true;
        }
        Player player= (Player) sender;
        if(game.getTeam(player) == null){
            player.sendMessage("You must have a team to set your team ready");
            return true;
        }
        if (!(player.hasPermission("ctf.teamleader"))){
            player.sendMessage("You must be teamleader too use this Command");
            return true;
        }
        if (game.getTeam(player).getFlagCords() == null){
            player.sendMessage(ChatColor.RED+"Du musst zuerst die Flagge platzieren");
            return true;
        }
        game.getTeam(player).setReady(true);
        int count= 0;
        for (Team team:
             game.teams) {
            if(team.isReady()){
                count++;
            }
        }if (count <= game.teams.length) {
            Bukkit.broadcastMessage(game.getTeam(player).getName() + " ist ready insgesamt [" + count + "/" + game.teams.length + "]");
        }if(count==game.teams.length){
            for (Player online:
                 Bukkit.getOnlinePlayers()) {
                TitleAPI.sendTitle(online,2,60,4,"PVP in 30 Seconds","");
            }
            new Countdown(10, 0, 1, true, game).runTaskTimer(plugin, 400, 20);
            game.updateBoard();
        }
        return true;

    }
}
