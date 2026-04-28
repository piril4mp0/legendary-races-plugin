package swmg.murilo.legendaryRaces.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import org.bukkit.event.player.PlayerRespawnEvent;

import org.bukkit.scheduler.BukkitRunnable;
import swmg.murilo.legendaryRaces.LegendaryRaces;
import swmg.murilo.legendaryRaces.manager.RaceManager;
import swmg.murilo.legendaryRaces.manager.RaceSkinManager;
import swmg.murilo.legendaryRaces.storage.PlayerStorage;

import java.util.Objects;


public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player p = event.getPlayer();

            PlayerStorage.getInstance().onPlayerFirstJoin(p);

        RaceSkinManager.getInstance().setPlayerSkin(p, PlayerStorage.getInstance().getPlayerRace(p));
    }

    // logica que coloca os efeitos no player de volta mesmo quando ele bebe leite
    @EventHandler
    public void onPlayerDrinkMilk(PlayerItemConsumeEvent itemConsumeEvent) {
        if(itemConsumeEvent.getItem().getType().toString() == "MILK_BUCKET") {

            Player p = itemConsumeEvent.getPlayer();
            String raceName = PlayerStorage.getInstance().getPlayerRace(p);
            new BukkitRunnable() {
                @Override
                public void run() {
                    RaceManager.getInstance().applyRaceEffects(p, raceName); // aplica efeitos novamente quando o player bebe leite
                }
            }.runTaskLater(LegendaryRaces.getInstance(), 5);
        }
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        // pega o Objeto Player
        Player player = event.getPlayer();

        // O codigo abaixo adiciona um delay para aplicar os efeitos da raca
        // Quando o player clicava para respawnar, o codigo estava sendo executado imediatamente, e isso fazia com que
        // o jogador nao recebesse os efeitos da raca
        new BukkitRunnable() {
            @Override
            public void run() {
                RaceManager.getInstance().applyRaceEffects(player, PlayerStorage.getInstance().getPlayerRace(player));
            }
        }.runTaskLater(LegendaryRaces.getInstance(), 5);


    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {

        if(!(e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player)) {

            return;
        }

        Player killed = e.getEntity();
        Player killer = e.getEntity().getKiller();
        String killerRace = PlayerStorage.getInstance().getPlayerRace(killer);
        if((Objects.equals(RaceManager.getInstance().getEnemyRace(killerRace),
                PlayerStorage.getInstance().getPlayerRace(killed))) &&
                !(killed.getName().equals(PlayerStorage.getInstance().getLastKill(killer)))) {


                PlayerStorage.getInstance().addPlayerCoins(killer, 150);


        } else {

            PlayerStorage.getInstance().addPlayerCoins(killer, 50);
        }
        PlayerStorage.getInstance().setLastKill(killer, killed.getName());

    }
}
