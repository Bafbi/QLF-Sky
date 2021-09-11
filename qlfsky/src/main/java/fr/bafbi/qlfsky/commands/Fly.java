package fr.bafbi.qlfsky.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.bafbi.qlfsky.Qsky;
import fr.bafbi.qlfsky.utils.IslandProfilDB;
import fr.bafbi.qlfsky.utils.PlayerProfilLocal;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class Fly implements TabExecutor {

    private Qsky main;
    ConfigurationSection textComponent;

    public Fly(Qsky main) {
        this.main = main;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        if (!(sender instanceof Player)) return true;

        textComponent = main.getConfig().getConfigurationSection("textComponent.command." + command.getName());

        Player player = (Player) sender;
        PlayerProfilLocal playerProfilLocal = new PlayerProfilLocal(player);
        IslandProfilDB currentIslandProfilDB = new IslandProfilDB(playerProfilLocal.getLocationUUID());

        Integer playerPermissionLevel = currentIslandProfilDB.getPermissionLevelOfPlayer(player);
        Integer islandPermissionLevel = currentIslandProfilDB.getLevelOfPermission("util.fly");

        if (playerPermissionLevel < islandPermissionLevel) {
            player.sendActionBar(GsonComponentSerializer.gson().deserialize(textComponent.getString("notPermit")));
            return true;
        }

        if (player.getAllowFlight()) {

            player.setAllowFlight(false);
            player.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("desactivated")));

            return true;
        }

        player.setAllowFlight(true);
        player.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("activated")));

        return true;
    }
    
}
