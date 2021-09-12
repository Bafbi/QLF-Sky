package fr.bafbi.qsky.listeners;

import java.util.NoSuchElementException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.bafbi.qsky.Qsky;
import fr.bafbi.qsky.utils.IslandProfilDB;
import fr.bafbi.qsky.utils.PlayerProfilLocal;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class TpEvent implements Listener {
    
    private final Qsky main;
    private ConfigurationSection textComponent;
    
    public TpEvent(Qsky main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerTp(PlayerTeleportEvent event) {

        if (!(event.getCause().equals(TeleportCause.COMMAND) || event.getCause().equals(TeleportCause.PLUGIN))) return;

        this.textComponent = main.getConfig().getConfigurationSection("textComponent.event.tp");

        Player player = event.getPlayer();
        Player playerTpTo;
        try {
            playerTpTo = event.getTo().getNearbyPlayers(2).iterator().next();
        } catch (NoSuchElementException e) {
            playerTpTo = null;
        }

        PlayerProfilLocal playerProfilLocal = new PlayerProfilLocal(player);

        if (playerTpTo != null) {
            PlayerProfilLocal playerTpToProfilLocal = new PlayerProfilLocal(playerTpTo);
            playerProfilLocal.setLocationUUID(playerTpToProfilLocal.getLocationUUID());
        }
        
        if (!(player.getAllowFlight() || player.isFlying())) {
            player.sendActionBar(GsonComponentSerializer.gson().deserialize(textComponent.getString("noFly")));
            return;
        }

        IslandProfilDB currentIslandProfilDB = new IslandProfilDB(playerProfilLocal.getLocationUUID());

        Integer playerPermissionLevel = currentIslandProfilDB.getPermissionLevelOfPlayer(player);
        Integer islandPermissionLevel = currentIslandProfilDB.getLevelOfPermission("util.fly");

        if (playerPermissionLevel < islandPermissionLevel) {
            player.setFlying(false);
            player.setAllowFlight(false);
            player.sendActionBar(GsonComponentSerializer.gson().deserialize(textComponent.getString("noFly")));
            return;
        }

    }

}
