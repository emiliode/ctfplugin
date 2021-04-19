package de.emilio.ctf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.*;

public class MyListener implements Listener {
    private Game game;
    public MyListener(Game game){
        this.game = game;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event ){
        event.getPlayer().sendMessage("Hello to the server");
        createBoard(event.getPlayer());
    }
    public static void createBoard(Player player){
       // System.out.println("Creating Board");
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("CTF-scoreboard","dummy");
        obj.setDisplayName("<< CTF-Scores >>");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score = obj.getScore(ChatColor.BLUE+ "=-=-=-=-=-=-=-=-=-=");
        score.setScore(3);
        Score score2 = obj.getScore(ChatColor.AQUA+ "Online Players"+ ChatColor.DARK_AQUA+ Bukkit.getOnlinePlayers().size());
        score2.setScore(2);
        Score score3 = obj.getScore(ChatColor.BLUE+ "Total Kills(mobs): "+ChatColor.DARK_AQUA+
                player.getStatistic(Statistic.MOB_KILLS));
        score3.setScore(1);
        Score score4 = obj.getScore(ChatColor.BLUE+ "Rank: "+ChatColor.DARK_AQUA+
                player.getStatistic(Statistic.MOB_KILLS));
        score4.setScore(0);
        player.setScoreboard(board);
        //System.out.println(player.getScoreboard());

    }
}
