package de.emilio.ctf.commands;

import de.emilio.ctf.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetRespawn implements CommandExecutor {
    private Game game;
    public CommandSetRespawn(Game game){
        this.game = game;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(args.length == 3)){
            return false;
        }
        if (!(sender instanceof Player)){
            sender.sendMessage("You must be a player to issue this command");
            return true;
        }
        Player player = (Player) sender;
        if(!sender.hasPermission("team.leader")){
            player.sendMessage("Du musst anführen um diesen Command auszuführen");
            return true;
        }
        System.out.println(args[0]);
        if (game.getTeam(player) == null){
            sender.sendMessage("Du hast kein team");
            return true;
        }

        int x ,y,z;
        try {
            x= Integer.parseInt(args[0]);
            y = Integer.parseInt(args[1]);
            z = Integer.parseInt(args[2]);
        }catch (Exception e){
            sender.sendMessage("Keine gültigen Koordinaten");
            return false;
        }
        System.out.println(Bukkit.getWorlds());
        Location loc = new Location(Bukkit.getWorlds().get(0), x,  y, z);
        game.getTeam(player).setTeamRespawn(loc);
        return true;
    }
}
