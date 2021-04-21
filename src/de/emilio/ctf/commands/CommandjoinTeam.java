package de.emilio.ctf.commands;

import de.emilio.ctf.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
        if (game.teams == null){
            player.sendMessage("You must create some teams first");
            return true;
        }


        openTeamInv(player, this.game);

        return true;
    }
    public static void openTeamInv(Player p, Game game){
        Inventory inv = Bukkit.createInventory(null,9,"Teamauswahl");
        for (int i=0; i<game.teams.length;i++){
            inv.setItem(i,game.teams[i].getIcon());

        }
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("CANCEL");
        ArrayList<String>lore = new ArrayList<String>();
        lore.add("Click to cancel");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(8,item);
        p.openInventory(inv);
    }
}
