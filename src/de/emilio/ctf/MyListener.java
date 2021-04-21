package de.emilio.ctf;

import com.sun.javafx.scene.traversal.Direction;
import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.enchantments.Enchantment;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.util.Vector;
import org.inventivetalent.bossbar.BossBar;
import org.inventivetalent.bossbar.BossBarAPI;

public class MyListener implements Listener {
    private Game game;
    private JavaPlugin plugin;
    public MyListener(Game game, JavaPlugin plugin){
        this.game = game;
        this.plugin=plugin;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event ){
        event.getPlayer().sendMessage("Hello to the server");}
    @EventHandler
    public void onItemClick(PlayerInteractEvent ev) {
        try {
            if (ev.getPlayer().getItemInHand().getType() != Material.AIR && ev.getClickedBlock().getType() != null) {
                ItemStack item = ev.getPlayer().getItemInHand();
                ItemMeta meta = item.getItemMeta();
                meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(meta);

                if (item.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS) == 10) {
                    byte color=(byte)game.getTeam(ev.getPlayer()).getColordata();
                    //ItemStack stack = new ItemStack(Material.STAINED_GLASS, 1, item.getDurability());
                    ev.getClickedBlock().setType(Material.STAINED_GLASS);
                    ev.getClickedBlock().setData(color);
                    if(ev.getPlayer().getItemInHand().getAmount()>1){
                        ev.getPlayer().getItemInHand().setAmount(ev.getPlayer().getItemInHand().getAmount()-1);
                    }else {
                        ev.getPlayer().getInventory().remove(item);
                    }
                    Location loc = ev.getClickedBlock().getLocation();

                    long timeInTicks = 2;
                    int[] intArray = {1, 1, -1, -1, -1, -1, +1, +1, 0};
                    int[] chArray = {'X', 'Y', 'X', 'X', 'Y', 'Y', 'X', 'X', 'X'};
                    final int[] yolo = {0};

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (chArray[yolo[0]] == 'X') {
                                loc.setX(loc.getX() + intArray[yolo[0]]);
                            } else {
                                loc.setZ(loc.getZ() + intArray[yolo[0]]);
                            }
                            loc.getBlock().setType(Material.STAINED_CLAY);
                            loc.getBlock().setData(color);

                            if (yolo[0] == 8) {
                                loc.setX(loc.getX() - 1);
                                loc.setZ((loc.getZ() + 1));
                                loc.setY(loc.getY() + 1);
                                loc.getBlock().setType(Material.STANDING_BANNER);
                                Banner banner = (Banner) loc.getBlock().getState();
                                banner.setBaseColor(DyeColor.getByDyeData((byte) (15-color)));
                                banner.update();
                                this.cancel();
                                Vector flagDirection = ev.getPlayer().getEyeLocation().getDirection().multiply(-1);
                                game.teams[game.getTeam(ev.getPlayer()).getId()].setFlagCords(loc);

                            }
                            yolo[0]++;

                        }
                    }.runTaskTimer(plugin, timeInTicks, timeInTicks);


                }
            }
        } catch (Exception e) {
        }
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
    public void onRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        game.Timeoutmap.put(player.getName(),( System.currentTimeMillis()/1000+ 20));
        player.setGameMode(GameMode.ADVENTURE);
        BossBar bossBar = BossBarAPI.addBar(player, // The receiver of the BossBar
                new TextComponent("Reentering  !"), // Displayed message
                BossBarAPI.Color.BLUE, // Color of the bar
                BossBarAPI.Style.NOTCHED_20, // Bar style
                1.0f); // Timeout-interval
        //BossBar bossBar1 = BossBarAPI.addBar(player, new TextComponent("Respawn"),BossBarAPI.Color.BLUE, BossBarAPI.Style.NOTCHED_20,1.0f,20,2);
        //bossBar.setProgress(-1.0f);
        System.out.println("BITTE SEI 1 DU HUSO:"+bossBar.getProgress());
        game.Barmap.put(player.getName(),bossBar);
    }
    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(game.Timeoutmap.containsKey(event.getPlayer().getName())){
            event.setCancelled(true);
           // event.getPlayer().sendMessage("You are not allowed to move");
        }
        for (Team team:
             game.teams) {
            if(team==null){
                return;
            }else if(team.getFlagCords()==null){return;}
            if(event.getTo()==team.getFlagCords()){
                event.getPlayer().sendMessage("über Flagge gelaufen");/*
                if(team.getId()!=game.getTeam(event.getPlayer()).getId()) {
                    event.getPlayer().sendMessage("über Gegner Flagge gelaufen");
                    ItemStack flag = (ItemStack) team.getFlagCords().getBlock();
                    ItemStack helmet = event.getPlayer().getInventory().getHelmet();
                    event.getPlayer().getInventory().setHelmet(flag);
                    event.getPlayer().getInventory().addItem(helmet);
                    team.getFlagCords().getBlock().setType(Material.AIR);
                */}/*else if(team.getId()==game.getTeam(event.getPlayer()).getId()){
                    event.getPlayer().sendMessage("über Team Flagge gelaufen");
                    ItemStack flagge = new ItemStack(Material.STANDING_BANNER);
                    if(event.getPlayer().getInventory().getHelmet()==flagge){
                        team.addScore();
                       // event.getPlayer().getInventory().setHelmet(helmet);
                    }

                }
            }*/
        }
    }
}
