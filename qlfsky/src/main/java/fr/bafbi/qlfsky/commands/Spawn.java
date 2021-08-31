package fr.bafbi.qlfsky.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import fr.bafbi.qlfsky.App;
import fr.bafbi.qlfsky.utils.PlayerProfilLocal;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class Spawn implements CommandExecutor {

    private App main;
    private FileConfiguration config;
    private final ConfigurationSection textComponent;

    public Spawn (App main) {

        this.main = main;
        this.config = main.getConfig();
        this.textComponent = config.getConfigurationSection("textComponent.command.spawn");

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        PlayerProfilLocal playerProfilLocal = new PlayerProfilLocal(player);
        List<Double> position = config.getDoubleList("spawn");
        Location spawn = new Location(Bukkit.getWorld(config.getString("skyWorldName")), position.get(0), position.get(1), position.get(2));

        playerProfilLocal.setLocationUUID("spawn");
        player.teleport(spawn);
        player.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("teleported")));

        return true;
    }
    
}
