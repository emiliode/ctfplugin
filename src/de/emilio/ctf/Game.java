package de.emilio.ctf;

import com.connorlinfoot.titleapi.TitleAPI;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
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
    public boolean unbreaking;
    public Scoreboard board;

    private JavaPlugin plugin;
    public Game(JavaPlugin plugin){
        this.plugin = plugin;
        System.out.println("Game created");

    }

    public void printTeams(){
        for (Team team:teams
             ) {
            Bukkit.broadcastMessage(team.getColor()+String.valueOf(team.getId()));
        }
    }
    public Team getTeam(Player player){
        if (teams == null){return null;}
        for (Team team :
                teams) {
            if(team.isInTeam(player)){
                return team;
            }
        }
        return null;
    }
    public Team getTeam(String name){
        if (teams == null){return null;}
        for(Team team: teams){
            //System.out.println(team.getName());
          if (team.getName().equalsIgnoreCase(name)){
              return team;
          }
        }
        return null;
    }

    public void addPlayer(Player player, int teamid){
        for (Team team:teams
             ) {
            if(team.isInTeam(player)){
                team.removePlayer(player);
            }if(teamid == team.getId()){
                team.addPlayer(player);
                player.sendMessage("You are now in team: "+team.getColor()+team.getName() );
            }


        }
    }
    public void startPvP(){
        this.pvp =true;
        for (Player player:
             Bukkit.getOnlinePlayers()) {
            TitleAPI.sendTitle(player,0,40,2,"ATTACK !!!","");
        }
    }

    public void updateBoard() {
        this.board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = this.board.registerNewObjective("Points", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("Points");
        for (Team team :
                teams) {
            objective.getScore(team.getColor()+team.getName()).setScore(team.getScore());
        }
        for (Player online :
                Bukkit.getOnlinePlayers()) {
            online.setScoreboard(this.board);
        }
    }
}
