package de.emilio.ctf;

import com.connorlinfoot.titleapi.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Helper {
    public static void giveFlag(Player player, ItemStack flag, String name, Game game, HashMap helmMap) {
        TitleAPI.sendTitle(player, 2, 25, 2, "You have the Flag", "");
        if (game.useHelmet) {
            if (player.getInventory().getHelmet() != null) {
                helmMap.put(player.getName(), player.getInventory().getHelmet());
            } else {
                helmMap.put(player.getName(), new ItemStack(Material.AIR, 1));
            }
            player.getInventory().setHelmet(flag);

        } else {
            Location ploc = player.getLocation().add(0, 1.5, 0);
            ArmorStand as = (ArmorStand) ploc.getWorld().spawn(ploc, ArmorStand.class);
            as.setGravity(false);
            as.setCanPickupItems(false);
            //as.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
            as.setCustomName(name);
            ItemStack Banner = new ItemStack(Material.BANNER, 1, (short) 14);
            as.setHelmet(Banner);
            as.setCustomNameVisible(true);
            as.setVisible(false);
            player.setPassenger(as);
        }
    }
    public static void notifyPlayers(String message){
        for (Player player:
                Bukkit.getOnlinePlayers()) {
            TitleAPI.sendTitle(player,2,25,2,message,"");
        }
    }
    public static boolean isTool(Material material) {
        return material == Material.WOOD_SWORD || material == Material.STONE_SWORD || material == Material.GOLD_SWORD || material == Material.IRON_SWORD || material == Material.DIAMOND_SWORD || material == Material.WOOD_PICKAXE || material == Material.STONE_PICKAXE || material == Material.GOLD_PICKAXE || material == Material.IRON_PICKAXE || material == Material.DIAMOND_PICKAXE || material == Material.WOOD_AXE || material == Material.STONE_AXE || material == Material.GOLD_AXE || material == Material.IRON_AXE || material == Material.DIAMOND_AXE || material == Material.WOOD_SPADE || material == Material.STONE_SPADE || material == Material.GOLD_SPADE || material == Material.IRON_SPADE || material == Material.DIAMOND_SPADE || material == Material.WOOD_HOE || material == Material.STONE_HOE || material == Material.GOLD_HOE || material == Material.IRON_HOE || material == Material.DIAMOND_HOE;
    }
    public static void placeflag(Location loc, byte color, Team team, JavaPlugin plugin){
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
                    loc.setY(loc.getY() + 1);
                    loc.setZ(loc.getZ() + 1);
                    loc.getBlock().setType(Material.STANDING_BANNER);
                    Banner banner = (Banner) loc.getBlock().getState();
                    banner.setBaseColor(DyeColor.getByDyeData((byte) (15-color)));
                    banner.update();
                    loc.setZ(loc.getZ() + 1);
                    this.cancel();
                    team.setFlagCords(loc);
                    team.setTeamRespawn(loc);

                }
                yolo[0]++;

            }
        }.runTaskTimer(plugin, timeInTicks, timeInTicks);
    }
}
