package de.emilio.ctf;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Game {
    public Team[] teams;
    public Game(){
        System.out.println("Game created");
    }
    public void updateScores(int[] scores){
        for (Player player:Bukkit.getOnlinePlayers()){
            MyListener.createBoard(player);
        }

    }
    public void printTeams(){
        for (Team team:teams
             ) {
            Bukkit.broadcastMessage(team.getColor()+String.valueOf(team.getId()));
        }
    }
}
