package de.emilio.ctf;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Tutorial extends JavaPlugin {
    @Override
    public void onEnable(){
        Bukkit.getConsoleSender().sendMessage("HAllo bitte funktioniert");
    }


}
