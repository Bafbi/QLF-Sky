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

import fr.bafbi.qlfsky.Qsky;
import fr.bafbi.qlfsky.utils.PlayerProfilLocal;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class Spawn implements CommandExecutor {

    private Qsky main;
    private FileConfiguration config;
    private ConfigurationSection textComponent;

    public Spawn (Qsky main) {

        this.main = main;

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        
        if (!(sender instanceof Player)) return false;

        this.config = main.getConfig();
        this.textComponent = this.config.getConfigurationSection("textComponent.command.spawn");

        Player player = (Player) sender;
        PlayerProfilLocal playerProfilLocal = new PlayerProfilLocal(player);
        List<Double> position = this.config.getDoubleList("spawn");
        Location spawn = new Location(Bukkit.getWorld(this.config.getString("skyWorldName")), position.get(0), position.get(1), position.get(2));

        playerProfilLocal.setLocationUUID("spawn");
        player.teleport(spawn);
        player.sendMessage(GsonComponentSerializer.gson().deserialize(this.textComponent.getString("teleported")));

        return true;
    }
    
}
