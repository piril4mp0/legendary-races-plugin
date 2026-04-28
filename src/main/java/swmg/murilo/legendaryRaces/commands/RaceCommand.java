package swmg.murilo.legendaryRaces.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import swmg.murilo.legendaryRaces.manager.RaceManager;
import swmg.murilo.legendaryRaces.storage.PlayerStorage;

public class RaceCommand implements CommandExecutor {
    private final int changeRacePrice = 1000;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Component.text("Esse comando só pode ser utilizado por players!")
                    .color(NamedTextColor.RED));
            return true;
        }

        if (args.length != 1) {
            String races = RaceManager.getInstance().getRaceList();

            Component message = Component.text("Raças disponíveis: ", NamedTextColor.YELLOW)
                    .append(Component.text(races, NamedTextColor.GREEN));

            commandSender.sendMessage(message);
            return true;
        }

        Player player = (Player) commandSender;

        if (PlayerStorage.getInstance().getPlayerCoins(player) < changeRacePrice) {
            commandSender.sendMessage(Component.text("Você precisa de pelo menos ")
                    .color(NamedTextColor.RED)
                    .append(Component.text(changeRacePrice + " coins", NamedTextColor.GOLD))
                    .append(Component.text(" para trocar de raça."))
                    .append(Component.newline())
                    .append(Component.text("Adquira mais coins caçando players das raças inimigas.", NamedTextColor.GRAY)));
            return true;
        }




        // Pega a raça digitada
        String selectedRace = args[0];
        boolean raceExists = RaceManager.getInstance().checkIfRaceExists(selectedRace);

        if (!raceExists) {
            player.sendMessage(Component.text("Raça inválida.")
                    .color(NamedTextColor.RED)
                    .append(Component.newline())
                    .append(Component.text("Digite uma raça válida para alterar sua raça.", NamedTextColor.YELLOW)));
            return true;
        }
        // Desconta o valor em coins
        PlayerStorage.getInstance().removePlayerCoins(player, changeRacePrice);
        RaceManager.getInstance().visitRaceSpawnLocation(selectedRace, player);
        RaceManager.getInstance().setPlayerSpawnToRaceSpawn(selectedRace, player);



        RaceManager.getInstance().setPlayerRace(player, selectedRace);

        player.sendMessage(Component.text("Você alterou sua raça com sucesso para: ")
                .color(NamedTextColor.GREEN)
                .append(Component.text(selectedRace, NamedTextColor.AQUA)));

        return true;
    }
    }
