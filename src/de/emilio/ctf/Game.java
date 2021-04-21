package de.emilio.ctf;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import org.bukkit.scoreboard.Scoreboard;
import org.inventivetalent.bossbar.BossBar;
import org.inventivetalent.bossbar.BossBarAPI;


import java.util.HashMap;


public class Game {
    public  BossBar flagtaken;
    public Inventory inv = null;
    public Team[] teams;
    public HashMap<String, Long> Timeoutmap = new HashMap<String, Long>();
    public HashMap<String, BossBar> Barmap = new HashMap<String,BossBar>();
    public int pointstowin;
    public boolean pvp;
    public boolean started;
    public boolean useHelmet;
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
    public Team getTeam(Player player){
        for (Team team :
                teams) {
            if(team.isInTeam(player)){
                return team;
            }
        }
        return null;
    }

    public void addPlayer(Player player, int teamid){
        for (Team team:teams
             ) {
            if(teamid == team.getId()){
                team.addPlayer(player);
                player.sendMessage("You are now in team: "+team.getColor()+team.getName() );
            }else{
                team.removePlayer(player);
            }
        }
    }
}
