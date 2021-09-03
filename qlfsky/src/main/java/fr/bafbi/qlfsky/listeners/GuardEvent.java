package fr.bafbi.qlfsky.listeners;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.bafbi.qlfsky.App;
import fr.bafbi.qlfsky.utils.IslandProfilDB;
import fr.bafbi.qlfsky.utils.PlayerProfilLocal;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class GuardEvent implements Listener{

    private final App main;
    private final ConfigurationSection textComponent;

    public GuardEvent(App main) {

        this.main = main;
        this.textComponent = main.getConfig().getConfigurationSection("textComponent.event.guard");

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        PlayerProfilLocal playerProfilLocal = new PlayerProfilLocal(player);

        IslandProfilDB currentIslandProfilDB = new IslandProfilDB(playerProfilLocal.getLocationUUID());

        Integer playerPermissionLevel = currentIslandProfilDB.getPermissionLevelOfPlayer(player);
        Integer islandPermissionLevel = currentIslandProfilDB.getLevelOfPermission("block.place");

        if (playerPermissionLevel < islandPermissionLevel) {
            event.setCancelled(true);
            player.sendActionBar(GsonComponentSerializer.gson().deserialize(textComponent.getString("notPermit")));
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        
        Player player = event.getPlayer();
        PlayerProfilLocal playerProfilLocal = new PlayerProfilLocal(player);

        IslandProfilDB currentIslandProfilDB = new IslandProfilDB(playerProfilLocal.getLocationUUID());

        Integer playerPermissionLevel = currentIslandProfilDB.getPermissionLevelOfPlayer(player);
        Integer islandPermissionLevel = currentIslandProfilDB.getLevelOfPermission("block.break");

        if (playerPermissionLevel < islandPermissionLevel) {
            event.setCancelled(true);
            player.sendActionBar(GsonComponentSerializer.gson().deserialize(textComponent.getString("notPermit")));
        }

    }
    
}
