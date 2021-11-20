package fr.bafbi.qsky.listeners;

import fr.bafbi.qsky.Qsky;
import fr.bafbi.qsky.utils.IslandProfilDB;
import fr.bafbi.qsky.utils.PlayerProfilLocal;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GuardEvent implements Listener{

    private final Qsky main;
    private ConfigurationSection textComponent;

    public GuardEvent(Qsky main) {
        this.main = main;
    }

    private void loadTextComponent() {
        this.textComponent = main.getConfig().getConfigurationSection("textComponent.event.guard");
    } 

    @EventHandler (priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {

        loadTextComponent();

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

    @EventHandler (priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {

        loadTextComponent();
        
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

    @EventHandler (priority = EventPriority.LOWEST)
    public void onBlockUse(PlayerInteractEvent event) {

        loadTextComponent();

        if (!(event.hasBlock())) return;

        //main.getLogger().info(Material.CHEST.toString());

        Player player = event.getPlayer();
        PlayerProfilLocal playerProfilLocal = new PlayerProfilLocal(player);

        IslandProfilDB currentIslandProfilDB = new IslandProfilDB(playerProfilLocal.getLocationUUID());

        Integer playerPermissionLevel = currentIslandProfilDB.getPermissionLevelOfPlayer(player);
        Integer islandPermissionLevel = currentIslandProfilDB.getLevelOfPermission("block.interract." + event.getClickedBlock().getType().toString());

        if (islandPermissionLevel == null) return;

        if (playerPermissionLevel < islandPermissionLevel) {
            event.setCancelled(true);
            player.sendActionBar(GsonComponentSerializer.gson().deserialize(textComponent.getString("notPermit")));
        }

    }
    
}
