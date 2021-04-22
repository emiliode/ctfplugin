package de.emilio.ctf;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Team {
    private ChatColor color;
    private short colorData;
    private int score;
    private int id;
    private Location flagCords= null;
    private Location teamRespawn = null;
    private String name;
    private ArrayList<String> players;
    private String Flaggenträger;
    private boolean ready =false;
    public Team(ChatColor color , short colorcode,int score, int id, String name){
        this.color = color;
        this.colorData = colorcode;
        this.setScore(score);
        this.id = id;
        this.players = new ArrayList<>();
        this.name = name;
    }
    public void setScore(int score){
        if (score >= 0 ){
            this.score =  score;
        }
    }
    public void setFlagCords(Location cords){
        flagCords = cords;
    }
    public void addScore(){
        score++;
    }
    public Location getFlagCords(){
        return flagCords;
    }

    public String getName() {
        return name;
    }

    public void setTeamRespawn(Location loc){
        this.teamRespawn=loc;
    }

    public Location getTeamRespawn(){
        return this.teamRespawn;
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

    public short getColordata() {
        return colorData;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }//171 103 336

    public boolean addPlayer(Player player) {
        if(this.players.contains(player.getName())){
            return false;
        }
        this.players.add(player.getName());
        return true;
    }
    public boolean isInTeam(Player player){
        return players.contains(player.getName());
    }
    public void removePlayer(Player player){
        if (players.contains(player.getName())) {
            this.players.remove(player.getName());
        }
    }

    public ItemStack getIcon(){
        ItemStack it = new ItemStack(Material.WOOL,1,colorData);
        ItemMeta m = it.getItemMeta();
        m.setDisplayName(this.color+name);
        m.setLore(players);
        it.setItemMeta(m);
        return it;
    }



    public String getFlaggenträger() {
        return Flaggenträger;
    }

    public void setFlaggenträger(String flaggenträger) {
        Flaggenträger = flaggenträger;
    }
    public boolean isReady(){
        return ready;
    }
    public void setReady(boolean ready){
        this.ready = ready;
    }
}
