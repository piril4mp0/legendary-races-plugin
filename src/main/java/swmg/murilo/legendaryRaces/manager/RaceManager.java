package swmg.murilo.legendaryRaces.manager;

import org.bukkit.*;

import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import swmg.murilo.legendaryRaces.LegendaryRaces;
import swmg.murilo.legendaryRaces.storage.PlayerStorage;

import java.io.File;
import java.util.ArrayList;


public class RaceManager {

    private File file;
    private YamlConfiguration config;
    private final String defaultRaceName;
    private String raceList;
    private static RaceManager instance;
    World world = Bukkit.getWorld("world");


    private RaceManager() {
        defaultRaceName =  "HUMANO";
    }

    public static RaceManager getInstance() {
        if(instance == null){
            instance = new RaceManager();
        }
        return instance;
    }

    public String getRaceList() {
        return raceList;
    }

    private void buildRaceList() {
        StringBuilder races = new StringBuilder();
        ConfigurationSection racesSection = config.getConfigurationSection("racas");
        for(String key : racesSection.getKeys(false)) {
            races.append(key).append(" ");
        }
        raceList = races.toString();
    }

    public void load() {
        // esse codigo carrega o conteudo das classes em config.yml
        // esse metodo e chamado em LegendaryRaces.java

        if (!LegendaryRaces.getInstance().getDataFolder().exists()){
            Bukkit.getLogger().info("Creating "+ LegendaryRaces.getInstance().getDataFolder() +" main directory ");
            LegendaryRaces.getInstance().getDataFolder().mkdir();
            //creating main plugin directory if not exists
            LegendaryRaces.getInstance().saveDefaultConfig();
            // then saving the config
        }


        file = new File(LegendaryRaces.getInstance().getDataFolder(), "config.yml");
        config = new YamlConfiguration();
        config.options().parseComments(true);

        try {
            config.load(file);
            buildRaceList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean checkIfRaceExists(String raceName) {
        boolean success = false;

        Object result = config.getString("racas." + raceName);
        if(result != null) {
            success = true;
        }
        return success;

    }

    private Location getRaceSpawnLocation(String raceName) {
        ConfigurationSection coordsSection = config.getConfigurationSection("racas." + raceName + ".spawn");
        double xCord = coordsSection.getDouble("x");
        double yCord = coordsSection.getDouble("y");
        double zCord = coordsSection.getDouble("z");
        return new Location(world, xCord, yCord, zCord);
    }

    public void visitRaceSpawnLocation(String raceName, Player player) {
        Location location = getRaceSpawnLocation(raceName);
        player.teleport(location);
    }

    public void setPlayerSpawnToRaceSpawn(String raceName, Player player) {
        Location location = getRaceSpawnLocation(raceName);
        player.setRespawnLocation(location, true);

    }

    private ArrayList<PotionEffect> getRaceEffects(String raceName) {
        int count = 1; // valor inicial escrito no config.yml
        ArrayList<PotionEffect> effectList = new ArrayList<PotionEffect>();
        while(config.getString("racas." + raceName + ".efeitos" + "." + count) != null) {
            String infos = config.getString("racas." + raceName + ".efeitos" + "." + count);
            if(infos.contains("none")) {

                return null;

            }
            String[] infosArray = infos.split(" ");
           // [0] = NOME_EFEITO, [1] = AMPLIFICADOR
            PotionEffectType effectType = Registry.POTION_EFFECT_TYPE.get(NamespacedKey.minecraft(infosArray[0].toLowerCase())); //tem que transformar em caixa baixa antes de mandar
            PotionEffect potionEffect = new PotionEffect(effectType, PotionEffect.INFINITE_DURATION, Integer.parseInt(infosArray[1]));
            effectList.add(potionEffect);

            count++;
        }

        return effectList;

    }

   private String getRaces() {
        String races = "";



        return races;
   }

    public void applyRaceEffects(Player player, String raceName) {
        player.clearActivePotionEffects();
        ArrayList<PotionEffect> effectList = instance.getRaceEffects(raceName);
        if(!(effectList == null)) {
            effectList.forEach(player::addPotionEffect);
        }
    }

    public void applyRaceAttributes(Player player, String raceName) {
        resetAttributes(player);
        double scale = Double.parseDouble(config.getString("racas." + raceName + ".altura"));
        double maxHealth = Double.parseDouble(config.getString("racas." + raceName + ".vida_maxima"));
        double naturalArmor = Double.parseDouble(config.getString("racas." + raceName + ".armadura_natural"));
        double aquatic = Double.parseDouble(config.getString("racas." + raceName + ".aquatico"));
        player.getAttribute(Attribute.SCALE).setBaseValue(scale);
        player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(maxHealth);
        player.getAttribute(Attribute.ARMOR).setBaseValue(naturalArmor);
         player.getAttribute(Attribute.WATER_MOVEMENT_EFFICIENCY).setBaseValue(aquatic);
    }

    public void resetAttributes(Player player) {
    player.getAttribute(Attribute.SCALE).setBaseValue(1.0);
    player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(20.0);
        player.getAttribute(Attribute.ARMOR).setBaseValue(0.0);
     player.getAttribute(Attribute.WATER_MOVEMENT_EFFICIENCY).setBaseValue(0.0);
    }

    public void setPlayerRace(Player player, String raceName) {
        raceName = raceName.toLowerCase();
        applyRaceEffects(player, raceName);
        applyRaceAttributes(player, raceName);
        PlayerStorage.getInstance().savePlayer(player, PlayerStorage.getInstance().getPlayerCoins(player), raceName, PlayerStorage.getInstance().getLastKill(player));
        RaceSkinManager.getInstance().setPlayerSkin(player, raceName); // seta a skin
    }

    public String getEnemyRace(String raceName) {
        return config.getString("racas." + raceName + ".raca_inimiga");
    }

}