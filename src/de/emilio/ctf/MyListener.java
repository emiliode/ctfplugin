package de.emilio.ctf;

import com.sun.javafx.scene.traversal.Direction;
import com.connorlinfoot.titleapi.TitleAPI;
import de.emilio.ctf.commands.CommandjoinTeam;
import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.enchantments.Enchantment;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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

import java.util.HashMap;

public class MyListener implements Listener {
    private Game game;
    private JavaPlugin plugin;
    private HashMap<String, ItemStack> helmMap= new HashMap<String, ItemStack>();
    //helmMap.put(player.getName(),ItemStack);
    //helmMap.get(player.getName());
    //helmMap.remove(player.getName());
    public MyListener(Game game, JavaPlugin plugin){
        this.game = game;
        this.plugin=plugin;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event ){
        event.getPlayer().sendMessage("Hello to the server");
        if(game.getTeam(event.getPlayer()) == null ){
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
        }
    }
    @EventHandler
    public void onItemClick(PlayerInteractEvent ev) {
        try {
            if (game.pvp&&game.unbreaking){
                if (ev.getItem() != null) {
                    if (isTool(ev.getItem().getType()) || ev.getItem().getType() == Material.FISHING_ROD || ev.getItem().getType() == Material.FLINT_AND_STEEL){
                        ev.getItem().setDurability((short) 1);
                    }
                }
            }


            if (ev.getPlayer().getItemInHand().getType() != Material.AIR && ev.getClickedBlock().getType() != null) {
                ItemStack item = ev.getPlayer().getItemInHand();
                ItemMeta meta = item.getItemMeta();
                meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(meta);

                if (item.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS) == 10) {
                    if(game.getTeam(ev.getPlayer())==null){
                        item.setAmount(1);
                        ev.getPlayer().getInventory().addItem(item);
                        ev.getPlayer().sendMessage(ChatColor.RED+"Enter a team with /join before using this block");
                        ev.setCancelled(true);
                        return;
                    }
                    byte color=(byte)game.getTeam(ev.getPlayer()).getColordata();
                    ev.getClickedBlock().setType(Material.STAINED_GLASS);
                    ev.getClickedBlock().setData(color);

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
                                game.teams[game.getTeam(ev.getPlayer()).getId()].setTeamRespawn(loc);

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
    public void onDeath(PlayerDeathEvent event){
        if(event.getEntity().getInventory().getHelmet() == null) {return;};
        if(event.getEntity().getInventory().getHelmet().getType()==Material.BANNER){
            if(game.teams[Integer.parseInt(event.getEntity().getInventory().getHelmet().getItemMeta().getDisplayName())]!=null){
                game.teams[Integer.parseInt(event.getEntity().getInventory().getHelmet().getItemMeta().getDisplayName())].setFlagCords(event.getEntity().getLocation());
            }
            event.getEntity().getLocation().getBlock().setType(Material.STANDING_BANNER);
            Banner banner = (Banner) event.getEntity().getLocation().getBlock().getState();
            banner.setBaseColor(DyeColor.getByDyeData((byte) (event.getEntity().getInventory().getHelmet().getDurability())));
            banner.update();
            event.getEntity().getInventory().setHelmet(helmMap.get(event.getEntity()));


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
                player.setGameMode(GameMode.SURVIVAL);
                player.setPlayerListName(game.teams[i].getColor()+player.getName());
                player.closeInventory();
                CommandjoinTeam.openTeamInv(player,game);
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
            if(!game.pvp){
                e.setCancelled(true);
            }
            Player whoWasHit = (Player) e.getEntity();
            Player whoHit = (Player) e.getDamager();
            if(game.pvp&&game.unbreaking) {
                    whoHit.getItemInHand().setDurability((short) 1);
            }
            else if (e.getEntity() instanceof Player) {
                ItemStack[] armour = ((Player) e.getEntity()).getInventory().getArmorContents();
                for (ItemStack i : armour) i.setDurability((short) 0);
                ((Player) e.getEntity()).getInventory().setArmorContents(armour);
            }
            if(game.getTeam(whoHit)==null||game.getTeam(whoWasHit)==null){
                e.setCancelled(true);
                return;
            }
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
        game.Timeoutmap.put(player.getName(),( System.currentTimeMillis()/1000+ 10));
        player.setGameMode(GameMode.SPECTATOR);

        if((game.getTeam(player)!= null) && (game.getTeam(player).getTeamRespawn() != null)){
             event.setRespawnLocation(game.getTeam(player).getTeamRespawn());
        }else {
           event.setRespawnLocation(Bukkit.getWorlds().get(0).getSpawnLocation().add(0, 2, 0));
        }
        BossBar bossBar = BossBarAPI.addBar(player, // The receiver of the BossBar
                new TextComponent("Reentering  !"), // Displayed message
                BossBarAPI.Color.BLUE, // Color of the bar
                BossBarAPI.Style.NOTCHED_20, // Bar style
                1.0f); // Timeout-interval
        game.Barmap.put(player.getName(),bossBar);
    }
    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(game.Timeoutmap.containsKey(event.getPlayer().getName())){
            if( event.getTo().getBlockX() != event.getFrom().getBlockX() || event.getTo().getBlockY() != event.getFrom().getBlockY() || event.getTo().getBlockZ() != event.getFrom().getBlockZ()){
                event.setCancelled(true);
            }
           event.getPlayer().sendMessage(ChatColor.RED+"You are not allowed to move");
        }
        if(game.teams == null){
            return;}
        //System.out.println("Checking for flag");
        for (Team team:
             game.teams) {

            if(team==null){
                return;
            }

            if(team.getFlagCords()!=null&&((int)event.getTo().getX())==((int)team.getFlagCords().getX())&&((int)event.getTo().getY())==((int)team.getFlagCords().getY())&&((int)event.getTo().getZ())==((int)team.getFlagCords().getZ())) {
                if (team.getId() != game.getTeam(event.getPlayer()).getId()) {
                    ItemStack flag = new ItemStack(Material.BANNER, 1, (short) (15-(team.getColordata())));
                    notifyPlayers("Team "+team.getName()+" Flag was taken");
                    ItemMeta meta = flag.getItemMeta();
                    meta.setDisplayName(team.getId()+"");
                    flag.setItemMeta(meta);
                    giveFlag(event.getPlayer(),flag, team.getName());
                    showBoards(team);
                    team.setFlaggenträger(event.getPlayer().getName());
                    team.getFlagCords().getBlock().setType(Material.AIR);
                    team.setFlagCords(null);
                }
            } if(team.getTeamRespawn()!=null&&team==game.getTeam(event.getPlayer())&&((int)event.getTo().getX())==((int)team.getTeamRespawn().getX())&&((int)event.getTo().getY())==((int)team.getTeamRespawn().getY())&&((int)event.getTo().getZ())==((int)team.getTeamRespawn().getZ())){
                    //System.out.println("DU BRINGST DIE FLAGGE ZURÜCK");
                    ItemStack flagge;
                    if(event.getPlayer().getInventory().getHelmet() == null) {return;}
                    if(event.getPlayer().getInventory().getHelmet().getType()==Material.BANNER){
                        flagge =event.getPlayer().getInventory().getHelmet();
                        ItemStack air = new ItemStack(Material.AIR,1);
                        team.addScore();
                        notifyPlayers("Team "+ game.getTeam(event.getPlayer()).getName()+" scored");
                        //event.getPlayer().sendMessage("SUPER KLASSE PUNKT");
                        game.updateBoard();
                        event.getPlayer().getInventory().setHelmet(helmMap.get(event.getPlayer().getName()));
                        Team pTeam = game.teams[Integer.parseInt(flagge.getItemMeta().getDisplayName())];
                        pTeam.getTeamRespawn().getBlock().setType(Material.STANDING_BANNER);
                        pTeam.setFlagCords(pTeam.getTeamRespawn());
                        pTeam.setFlaggenträger(null);
                        Banner banner = (Banner) pTeam.getTeamRespawn().getBlock().getState();
                        banner.setBaseColor(DyeColor.getByDyeData((byte) (15- pTeam.getColordata())));
                        banner.update();
                        if(team.getScore()==game.pointstowin){
                            notifyPlayers(team.getName()+ " has won");
                            game.started = false;
                            game.pvp = false;
                            for (Team zTeam:
                                 game.teams) {
                                zTeam.setScore(0);
                            }
                        }
                    }

            }
        }
    }
    private void notifyPlayers(String message){
        for (Player player:
             Bukkit.getOnlinePlayers()) {
            TitleAPI.sendTitle(player,2,25,2,message,"");
        }
    }
    private void giveFlag(Player player, ItemStack flag, String name){
        TitleAPI.sendTitle(player, 2,25,2,"You have the Flag","");
        if(game.useHelmet){
            if(player.getInventory().getHelmet()!=null){
                helmMap.put(player.getName(), player.getInventory().getHelmet());
            }else{
                helmMap.put(player.getName(), new ItemStack(Material.AIR, 1));
            }
            player.getInventory().setHelmet(flag);

        }else {
            Location ploc = player.getLocation().add(0, 1.5, 0);
            ArmorStand as = (ArmorStand) ploc.getWorld().spawn(ploc, ArmorStand.class);
            as.setGravity(false);
            as.setCanPickupItems(false);
            //as.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
            as.setCustomName(name);
            ItemStack Banner = new ItemStack(Material.BANNER,1,(short)14);
            as.setHelmet(Banner);
            as.setCustomNameVisible(true);
            as.setVisible(false);
            player.setPassenger(as);
        }

    }
    @EventHandler(ignoreCancelled = true)
    public void noWeaponBreakDamage(EntityShootBowEvent event) {
        try {
            if (game.pvp&&game.unbreaking) {
                if (event.getEntity() instanceof Player) event.getBow().setDurability((short) 1);
            }
        } catch (Exception ex) {
        }
    }
    private void removeFlag(Player player, String name){
        if(game.useHelmet){
            return;
        }else{
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),  "kill @e[name="+name+"]");
        }
    }
    private void showBoards(Team team){
        for (String playername:
             team.getPlayers()) {
            //TitleAPI.sendTitle(Bukkit.getPlayer(playername),2,10,2,"Title","Subtitle");
        }
    }
    private boolean isTool(Material material) {
        return material == Material.WOOD_SWORD || material == Material.STONE_SWORD || material == Material.GOLD_SWORD || material == Material.IRON_SWORD || material == Material.DIAMOND_SWORD || material == Material.WOOD_PICKAXE || material == Material.STONE_PICKAXE || material == Material.GOLD_PICKAXE || material == Material.IRON_PICKAXE || material == Material.DIAMOND_PICKAXE || material == Material.WOOD_AXE || material == Material.STONE_AXE || material == Material.GOLD_AXE || material == Material.IRON_AXE || material == Material.DIAMOND_AXE || material == Material.WOOD_SPADE || material == Material.STONE_SPADE || material == Material.GOLD_SPADE || material == Material.IRON_SPADE || material == Material.DIAMOND_SPADE || material == Material.WOOD_HOE || material == Material.STONE_HOE || material == Material.GOLD_HOE || material == Material.IRON_HOE || material == Material.DIAMOND_HOE;
    }

}
