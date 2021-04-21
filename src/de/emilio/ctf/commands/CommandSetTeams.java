package de.emilio.ctf.commands;

import de.emilio.ctf.Game;
import de.emilio.ctf.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class CommandSetTeams implements CommandExecutor {
    Game game;
    final private ChatColor[] colors = {ChatColor.BLUE, ChatColor.RED , ChatColor.LIGHT_PURPLE ,ChatColor.WHITE};
    final private short[] colorcodes = {11,14,6,0};
    final private String[] names = {"Blau","Rot","Pink","Weiß"};
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
                this.game.sb = Bukkit.getScoreboardManager().getNewScoreboard();
                for (int i=0; i< numteams; i++){
                    teams[i] = new Team(colors[i], colorcodes[i],0,i,names[i] );
                    if(sender instanceof Player){
                        Player player = (Player) sender;
                        ItemStack item = new ItemStack(Material.STAINED_GLASS,1, DyeColor.BLACK.getData());
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName( ChatColor.BLACK+"Flaggenblock");
                        meta.addEnchant(Enchantment.DAMAGE_ARTHROPODS, 10, true);
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        ArrayList<String> lore = new ArrayList<String>();
                        lore.add("Die Flagge deines Teams wird auf diesem Block spawnen");
                        lore.add("Du wirst bei der Flagge respawnen");
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        player.getInventory().addItem(item);
                    }
                }
                for (Team team :
                        teams) {
                    sender.sendMessage(team.getName());


                }
                this.game.teams = teams;



            }
        }else {
            sender.sendMessage("You don't have enough permissions");
        }
        return true;
    }
}