package de.emilio.ctf;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team {
    private ChatColor color;
    private int colorcode;
    private int score;
    private int id;
    private ArrayList<Player> players = new ArrayList<Player>();
    public Team(ChatColor color , int colorcode,int score, int id){
        this.color = color;
        this.colorcode = colorcode;
        this.setScore(score);
        this.id = id;
    }
    public void setScore(int score){
        if (score > 0 ){
            this.score =  score;
        }
    }

    public int getScore() {
        return this.score;
    }

    public ChatColor getColor(){
        return this.color;
    }
    public int getId(){
        return this.id;
    }

    public int getColorcode() {
        return colorcode;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public boolean addPlayer(Player player) {
        if(this.players.contains(player)){
            return false;
        }
        this.players.add(player);
        return true;
    }
    public boolean contains(Player player){
        return this.players.contains(player);
    }
    public void removePlayer(Player player){
        if (players.contains(player)) {
            this.players.remove(player);
        }
    }
}
