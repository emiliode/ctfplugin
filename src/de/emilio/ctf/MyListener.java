package de.emilio.ctf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scoreboard.*;

public class MyListener implements Listener {
    private Game game;
    public MyListener(Game game){
        this.game = game;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event ){
        event.getPlayer().sendMessage("Hello to the server");
        //createBoard(event.getPlayer());
    }
    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(!event.getInventory().getTitle().equals("Teamauswahl")){return;}
        if(event.getCurrentItem() == null){return;}
        if (event.getCurrentItem().getItemMeta() == null)return;
        if (event.getCurrentItem().getItemMeta().getDisplayName() == null)return;

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        System.out.println(game.teams.length);
        for (int i = 0; i < game.teams.length; i++) {
            if (event.getSlot() ==i){
                game.addPlayer(player, i);
                player.setPlayerListName(game.teams[i].getColor()+player.getName());
            }
        }
        if(event.getSlot() == 8){
            player.closeInventory();
        }

    }
    // Friendly fire
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player whoWasHit = (Player) e.getEntity();
            Player whoHit = (Player) e.getDamager();
            for (Team team:
                    game.teams) {
                if(team.isInTeam(whoWasHit) && team.isInTeam(whoHit)){
                    e.setCancelled(true);
                }
            }

        }
    }
    @EventHandler
    public void onRespwan(PlayerRespawnEvent event){
        game.Timeoutmap.put(event.getPlayer().getName(),System.currentTimeMillis()+ (20*1000));
        event.getPlayer().setGameMode(GameMode.SPECTATOR);
    }



}
