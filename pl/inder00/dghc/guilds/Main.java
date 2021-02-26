package pl.inder00.dghc.guilds;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.inder00.dghc.guilds.commands.*;
import pl.inder00.dghc.guilds.listeners.PlayerJoinQuitListener;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        //REGISTER COMMANDS
        new ZalozCommand(this);
        new UsunCommand(this);
        new ZaprosCommand(this);
        new DolaczCommand(this);
        new WyrzucCommand(this);
        new OpuscCommand(this);
        new GildiaCommand(this);
        new GildiaCommand(this);

        Bukkit.getPluginManager().registerEvents(new PlayerJoinQuitListener(), this);

    }
}
