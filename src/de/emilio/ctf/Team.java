package de.emilio.ctf;

import org.bukkit.ChatColor;

public class Team {
    private ChatColor color;
    private int score;
    private int id;
    public Team(ChatColor color , int score, int id){
        this.color = color;
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
}
