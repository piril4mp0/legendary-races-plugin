package swmg.murilo.legendaryRaces.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import swmg.murilo.legendaryRaces.LegendaryRaces;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerStorage {
    private File file;
    private YamlConfiguration config;
    private static PlayerStorage instance;

    private PlayerStorage() {

    }

    public static PlayerStorage getInstance() {
        if(instance == null){
            instance = new PlayerStorage();
        }
        return instance;
    }

    public void onPlayerFirstJoin(Player player) {
        String playerName = player.getName();
        int initialCoinAmount = 1000;
        String defaultRace = "humano";
        File userdata = new File(LegendaryRaces.getInstance().getDataFolder(), File.separator + "playerDatabase");
        File f = new File(userdata, File.separator + playerName + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
        if (!f.exists()) {
            try {
                playerData.set("raca", defaultRace);
                playerData.set("coins", initialCoinAmount);
                playerData.set("ultima_kill", "SEM KILLS");
                playerData.save(f);
            } catch (IOException exception) {

                exception.printStackTrace();
            }
        }


    }

    public String getPlayerRace(Player player) {

        return getValue(player, "raca");
    }

    public int getPlayerCoins(Player player) {
        return Integer.parseInt(getValue(player, "coins"));
    }

    public String getLastKill(Player player) {
        return getValue(player, "ultima_kill");
    }

    public void setLastKill(Player player, String killedPlayerName) {
        File file = getPlayerFile(player);
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(file);
        playerData.set("ultima_kill", killedPlayerName);
        try {
            playerData.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getValue(Player player, String path) {
        File file = getPlayerFile(player);
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(file);
        return playerData.getString(path);
    }



    public void savePlayer(Player player, int coins, String raceName, String lastKill) {
        File file = getPlayerFile(player);
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(file);
        try {
            playerData.set("raca", raceName);
            playerData.set("coins", coins);
            playerData.set("ultima_kill", lastKill);
            // adicionar mais campos futuramente
            playerData.save(file);
        } catch (IOException exception) {

            exception.printStackTrace();
        }
    }

    public File getPlayerFile(Player player) {

        File playerFile =  new File (LegendaryRaces.getInstance().getDataFolder() + File.separator + "playerDatabase", player.getName() + ".yml");

        return playerFile;
    }

    public void addPlayerCoins(Player player, int amount) {
        File file = getPlayerFile(player);
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(file);
        playerData.set("coins", getPlayerCoins(player) + amount);
        try {
            playerData.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePlayerCoins(Player player, int amount) {
        File file = getPlayerFile(player);
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(file);
        playerData.set("coins", getPlayerCoins(player) - amount);
        try {
            playerData.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
