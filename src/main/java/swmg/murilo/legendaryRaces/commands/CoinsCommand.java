package swmg.murilo.legendaryRaces.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import swmg.murilo.legendaryRaces.storage.PlayerStorage;

public class CoinsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Component.text("Esse comando só pode ser utilizado por players!")
                    .color(NamedTextColor.RED));
            return true;
        }

        Player player = (Player) commandSender;
        int coins = PlayerStorage.getInstance().getPlayerCoins(player);

        player.sendMessage(Component.text("Você tem ")
                .color(NamedTextColor.YELLOW)
                .append(Component.text(coins + " coins", NamedTextColor.GOLD))
                .append(Component.text(".", NamedTextColor.YELLOW)));

        return true;
    }
}
