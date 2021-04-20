package de.emilio.ctf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.inventivetalent.bossbar.BossBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    public Scoreboard sb;
    public Inventory inv = null;
    public Team[] teams;
    public HashMap<String, Long> Timeoutmap = new HashMap<String, Long>();
    public HashMap<String, BossBar> Barmap = new HashMap<String,BossBar>();
    public Game(){

        System.out.println("Game created");

    }
    public void updateScores(int[] scores){
        for (Player player:Bukkit.getOnlinePlayers()){
           // MyListener.createBoard(player);
        }

    }
    public void printTeams(){
        for (Team team:teams
             ) {
            Bukkit.broadcastMessage(team.getColor()+String.valueOf(team.getId()));
        }
    }
/*    public void createInv(){
        this.inv = Bukkit.createInventory(null,9 , ChatColor.GOLD+""+ChatColor.BOLD+"Change Team");
        List<String> lore = new ArrayList<String >();
        lore.add("Click to join team");
        lore.add("Some other lore");
        for (int i = 0; i < this.teams.length; i++) {
            ItemStack item  = new ItemStack(Material.WOOL,1,this.teams[i].getColordata());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(this.teams[i].getColor()+""+ this.teams[i].getId());
            meta.setLore(lore);
            item.setItemMeta(meta);
            this.inv.setItem(i,item);
        }
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("CANCEL");
        lore.remove(0);
        lore.add("Click to cancel");
        meta.setLore(lore);
        item.setItemMeta(meta);
        this.inv.setItem(8,item);
    }*/
    public void addPlayer(Player player, int teamid){
        for (Team team:teams
             ) {
            if(teamid == team.getId()){
                team.addPlayer(player);
                player.sendMessage("You are now in the team with this "+team.getColor()+"color" );
            }else{
                team.removePlayer(player);
            }
        }
    }
}
