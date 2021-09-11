package fr.bafbi.qlfsky.commands;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;

import org.bson.Document;
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

public class Bypass implements TabExecutor {

    private Qsky main;
    private FindIterable<Document> allIslands = Qsky.getIslandCol().find(Filters.eq("Deleted", false));
    private ConfigurationSection textComponent;

    public Bypass(Qsky main) {
        this.main = main;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {
        
        List<String> tempCompletor = new ArrayList<>();
        List<String> completor = new ArrayList<>();

        if (args.length != 1) return completor;

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            tempCompletor.add(onlinePlayer.getName());
        }
        for (Document islandDoc : this.allIslands) {
            tempCompletor.add(islandDoc.getString("Name"));
        }

        for (String string : tempCompletor) {
            if (string.contains(args[1])) completor.add(string);
        }

        return completor;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        this.textComponent = main.getConfig().getConfigurationSection("textComponent.command.bypass");
        IslandProfilDB islandProfilDB;

        if (args.length < 1) {

            PlayerProfilLocal playerProfilLocal = new PlayerProfilLocal(player);
            islandProfilDB = new IslandProfilDB(playerProfilLocal.getLocationUUID());

            islandProfilDB.setPermissionLevelOfPlayer(player, 8);

        } else {

            Document islandSelected = this.allIslands.filter(Filters.eq("Name", args[1])).first();
            islandProfilDB = new IslandProfilDB(islandSelected.getString("UUID"));

            islandProfilDB.setPermissionLevelOfPlayer(player, 8);

        }

        player.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("bypassed").replace("<island.name>", islandProfilDB.getName())));

        return true;
    }
    
}
