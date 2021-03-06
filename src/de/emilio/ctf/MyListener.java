package de.emilio.ctf;

import de.emilio.ctf.Helper.*;
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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.inventivetalent.bossbar.BossBar;
import org.inventivetalent.bossbar.BossBarAPI;

import java.util.HashMap;

import static de.emilio.ctf.Helper.*;

public class MyListener implements Listener {
    private Game game;
    private JavaPlugin plugin;
    private HashMap<String, ItemStack> helmMap= new HashMap<String, ItemStack>();

    private String[] SUPERDEATHMESSAGESTEIL1 = {"wude von ","wurde von ", "hat von "," unterschätzte","", " wurde von "};
    private String[] SUPERDEATHMESSAGESTEIL2 = {" mies in den Arsch gebuttert", " aus der Welt gejeetet", "mies aufs maul bekommen","","dababyt !! LESSSS GO"};


    //helmMap.put(player.getName(),ItemStack);
    //helmMap.get(player.getName());
    //helmMap.remove(player.getName());
    public MyListener(Game pGame, JavaPlugin plugin){
        game = pGame;
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
                    placeflag(loc,color,game.getTeam(ev.getPlayer()),plugin);



                }
            }
        } catch (Exception e) {
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        if(!game.pvp)return;

        if(event.getEntity().getKiller() != null) {
            String killer = event.getEntity().getKiller().getName();
            String killed = event.getEntity().getName();
            if (game.getTeam(killer)!=null){
                int index= (int) (Math.random() * (SUPERDEATHMESSAGESTEIL1.length -1));

                if (game.getTeam(killed) != null && game.getTeam(killer) != null){
                    event.setDeathMessage( game.getTeam(killed).getColor()+killed +ChatColor.WHITE+  SUPERDEATHMESSAGESTEIL1[index] + game.getTeam(killer).getColor()+killer+ChatColor.WHITE+SUPERDEATHMESSAGESTEIL2[index]);
                }else if (game.getTeam(killer)!= null && game.getTeam(killed) == null){
                    event.setDeathMessage( killed + SUPERDEATHMESSAGESTEIL1[index] + game.getTeam(killer).getColor()+killer+ChatColor.WHITE+SUPERDEATHMESSAGESTEIL2[index]);
                }else if (game.getTeam(killer)== null  && game.getTeam(killed) !=null){
                    event.setDeathMessage( game.getTeam(killed).getColor() +killed+ChatColor.WHITE+ SUPERDEATHMESSAGESTEIL1[index] +killer+SUPERDEATHMESSAGESTEIL2[index]);
                }else {
                    event.setDeathMessage(killed+SUPERDEATHMESSAGESTEIL1[index]+killer+SUPERDEATHMESSAGESTEIL2[index]);
                }
            }

        }   if(event.getEntity().getInventory().getHelmet()==null){
            System.out.println("hier");
            return;
        }
            if(event.getEntity().getInventory().getHelmet().getType()!= Material.BANNER) {
                System.out.println("bitte nicht");
                return;
            }
            event.getEntity().setGameMode(GameMode.SPECTATOR);

            if(game.teams[Integer.parseInt(event.getEntity().getInventory().getHelmet().getItemMeta().getDisplayName())]!=null){

                System.out.println("hiersdfsf");
                game.teams[Integer.parseInt(event.getEntity().getInventory().getHelmet().getItemMeta().getDisplayName())].setFlagCords(event.getEntity().getLocation());
            }

            int teamId = Integer.parseInt(event.getEntity().getInventory().getHelmet().getItemMeta().getDisplayName());
            int x = game.teams[teamId].getColordata();
            event.getEntity().getInventory().setHelmet(new ItemStack(Material.AIR));

            if(helmMap.get(event.getEntity().getName())!=null) {
                event.getEntity().getInventory().setHelmet(helmMap.get(event.getEntity().getName()));

                helmMap.remove(event.getEntity().getName());
            }
            event.getEntity().getLocation().getBlock().setType(Material.STANDING_BANNER);
            Banner banner = (Banner)event.getEntity().getLocation().getBlock().getState();
            banner.setBaseColor(DyeColor.getByDyeData((byte) (15 - x)));
            banner.update();
            Location todesPunkt = event.getEntity().getLocation();
            long timeInTicks = 200;
            new BukkitRunnable() {

            @Override
            public void run() {
                if(todesPunkt.getBlock().getType()==Material.STANDING_BANNER){
                    Location someLocation =game.teams[teamId].getTeamRespawn();
                    someLocation.getBlock().setType(Material.STANDING_BANNER);
                    Banner banner = (Banner)someLocation.getBlock().getState();
                    banner.setBaseColor(DyeColor.getByDyeData((byte) (15 - game.teams[teamId].getColordata())));
                    banner.update();
                    someLocation.setZ(someLocation.getZ()+1);
                    game.teams[teamId].setFlagCords(someLocation);
                    todesPunkt.getBlock().setType(Material.AIR);
                    notifyPlayers("The Flag of team "+game.teams[teamId].getName()+" is back");
                }
                //The code inside will be executed in {timeInTicks} ticks.
            }
        }.runTaskLater(plugin, timeInTicks);


    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent ev){
        if(game.teams==null)return;
        for (Team team:
             game.teams) {
            System.out.println(team);
            if(team.getTeamRespawn()!=null){
                Location loc = new Location(Bukkit.getWorlds().get(0),team.getTeamRespawn().getX(),team.getTeamRespawn().getY()-1,team.getTeamRespawn().getZ()-1);
                Location loc2 = new Location(Bukkit.getWorlds().get(0),team.getTeamRespawn().getX(),team.getTeamRespawn().getY(),team.getTeamRespawn().getZ()-1);
               // System.out.println(loc.getBlock()+"\n"+ev.getBlock());
                if(ev.getBlock().getLocation().equals(loc) ||ev.getBlock().getLocation().equals(team.getTeamRespawn())||loc2.equals(ev.getBlock().getLocation())){
                    ev.getPlayer().sendMessage(ChatColor.DARK_RED+"Cannot break this Block");
                    ev.setCancelled(true);
                }
            }
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
        //System.out.println(game.teams.length);
        for (int i = 0; i < game.teams.length; i++) {
            if (event.getSlot() ==i){
                game.addPlayer(player, i);
                player.setGameMode(GameMode.SURVIVAL);
               // player.setPlayerListName(game.teams[i].getColor()+player.getName());

            }
                player.closeInventory();
                CommandjoinTeam.openTeamInv(player, game);
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
        if((game.getTeam(player)!= null) && (game.getTeam(player).getTeamRespawn() != null)){
            event.setRespawnLocation(game.getTeam(player).getTeamRespawn());
        }else {
            event.setRespawnLocation(Bukkit.getWorlds().get(0).getSpawnLocation().add(0, 2, 0));
        }
        if(game.pvp) {
            game.Timeoutmap.put(player.getName(), (System.currentTimeMillis() / 1000 + 10));
            player.setGameMode(GameMode.SPECTATOR);


            BossBar bossBar = BossBarAPI.addBar(player, // The receiver of the BossBar
                    new TextComponent("Reentering  !"), // Displayed message
                    BossBarAPI.Color.BLUE, // Color of the bar
                    BossBarAPI.Style.NOTCHED_20, // Bar style
                    1.0f); // Timeout-interval
            game.Barmap.put(player.getName(), bossBar);
        }
    }
    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(game.Timeoutmap.containsKey(event.getPlayer().getName())){
            if( event.getTo().getBlockX() != event.getFrom().getBlockX() || event.getTo().getBlockY() != event.getFrom().getBlockY() ||
                    event.getTo().getBlockZ() != event.getFrom().getBlockZ()){
                event.setCancelled(true);
            }
            if(game.SentMap.get(event.getPlayer().getName()) == null) {
                event.getPlayer().sendMessage(ChatColor.RED+"You are not allowed to move");
                game.SentMap.put(event.getPlayer().getName(),true);
            }
        }
        if(game.pvp){
        if(game.teams == null){
            return;
        }
        //System.out.println("Checking for flag");
            if(event.getPlayer().getGameMode()==GameMode.SPECTATOR)return;
        for (Team team:
             game.teams) {

            if(team==null){
                return;
            }

            if(event.getPlayer().getGameMode()!=GameMode.SPECTATOR&&team.getFlagCords()!=null&&((int)event.getTo().getX())==((int)team.getFlagCords().getX())&&((int)event.getTo().getY())==((int)team.getFlagCords().getY())&&((int)event.getTo().getZ())==((int)team.getFlagCords().getZ())) {
                if (team.getId() != game.getTeam(event.getPlayer()).getId()) {
                    ItemStack flag = new ItemStack(Material.BANNER, 1, (short) (15-(team.getColordata())));
                    notifyPlayers("Team "+team.getName()+" Flag was taken");
                    ItemMeta meta = flag.getItemMeta();
                    meta.setDisplayName(team.getId()+"");
                    flag.setItemMeta(meta);
                    giveFlag(event.getPlayer(),flag, team.getName(),game, helmMap);
                    team.setFlaggenträger(event.getPlayer().getName());
                    Location someLocation =team.getFlagCords();
                    someLocation.setZ(someLocation.getZ()-1);
                    event.getTo().getBlock().setType(Material.AIR);
                    team.setFlagCords(null);
                }
            } if(team.getTeamRespawn()!=null&&team==game.getTeam(event.getPlayer())&&((int)event.getTo().getX())==((int)team.getTeamRespawn().getX())&&((int)event.getTo().getY())==((int)team.getTeamRespawn().getY())&&((int)event.getTo().getZ())==((int)team.getTeamRespawn().getZ())) {
                //System.out.println("DU BRINGST DIE FLAGGE ZURÜCK");
                ItemStack flagge;
                if (event.getPlayer().getInventory().getHelmet() == null) {
                    return;
                }
                if (event.getPlayer().getInventory().getHelmet().getType() == Material.BANNER) {
                    flagge = event.getPlayer().getInventory().getHelmet();
                    team.addScore();
                    notifyPlayers("Team " + game.getTeam(event.getPlayer()).getName() + " scored");
                    //event.getPlayer().sendMessage("SUPER KLASSE PUNKT");
                    game.updateBoard();
                    event.getPlayer().getInventory().setHelmet(helmMap.get(event.getPlayer().getName()));
                    Team pTeam = game.teams[Integer.parseInt(flagge.getItemMeta().getDisplayName())];
                    Location someLocation =pTeam.getTeamRespawn();
                    someLocation.getBlock().setType(Material.STANDING_BANNER);
                    Banner banner = (Banner)someLocation.getBlock().getState();
                    banner.setBaseColor(DyeColor.getByDyeData((byte) (15 - pTeam.getColordata())));
                    banner.update();
                    someLocation.setZ(someLocation.getZ()+1);
                    pTeam.setFlagCords(someLocation);
                    pTeam.setFlaggenträger(null);
                    if (team.getScore() >= game.pointstowin) {
                        notifyPlayers(team.getName() + " has won");
                        game.started = false;
                        game.pvp = false;
                        for (Team zTeam :
                                game.teams) {
                            zTeam.setReady(false);
                            zTeam.setScore(0);
                        }
                    }
                }
            }
            }
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
}
