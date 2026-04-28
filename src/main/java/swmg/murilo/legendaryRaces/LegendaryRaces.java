package swmg.murilo.legendaryRaces;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import swmg.murilo.legendaryRaces.commands.CoinsCommand;
import swmg.murilo.legendaryRaces.commands.RaceCommand;
import swmg.murilo.legendaryRaces.listener.PlayerListener;
import swmg.murilo.legendaryRaces.manager.RaceManager;

public final class LegendaryRaces extends JavaPlugin {

    private static LegendaryRaces instance;


    public static LegendaryRaces getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // carrega as racas
        RaceManager.getInstance().load();
        // carrega os players
        //
        getCommand("raca").setExecutor(new RaceCommand());
        getCommand("coins").setExecutor(new CoinsCommand());
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);



        getLogger().info("Legendary Races: ON");
    }

    @Override
    public void onDisable() {

        getLogger().info("Legendary Races: OFF");
    }
}
