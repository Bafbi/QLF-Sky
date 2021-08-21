package fr.bafbi.qlfsky.listeners;

import java.util.Date;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.bafbi.qlfsky.App;
import fr.bafbi.qlfsky.utils.PlayerProfilDB;
import fr.bafbi.qlfsky.utils.PlayerProfilLocal;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class JoinLeaveEvent implements Listener {
    
    private final App main;
    private final ConfigurationSection textComponent;

    public JoinLeaveEvent(App main) {

        this.main = main;
        this.textComponent = main.getConfig().getConfigurationSection("textComponent.event.joinLeave");

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        PlayerProfilDB playerProfilDB = new PlayerProfilDB(player);
        PlayerProfilLocal playerProfilLocal= new PlayerProfilLocal(player);

        if (playerProfilDB.getPlaytime() == 0.0) event.joinMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("firstConnection").replace("<player.name>", player.getName()).replace("<player.connection.rank>", Long.toString(playerProfilDB.countProfils() + 1))));
        else event.joinMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("connection").replace("<player.name>", player.getName())));

        playerProfilDB.setOnline(true);
        playerProfilDB.setConnectionTime(new Date());
        playerProfilLocal.setMoney(playerProfilDB.getMoney());

        if (!playerProfilDB.getPlayedServerVersion().contains(main.getServerVersion())) playerProfilDB.addPlayedServerVersion(main.getServerVersion());  

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        PlayerProfilDB playerProfilDB = new PlayerProfilDB(player);
        PlayerProfilLocal playerProfilLocal= new PlayerProfilLocal(player);

        event.quitMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("disconnect").replace("<player.name>", player.getName())));

        playerProfilDB.update();
        playerProfilDB.setOnline(false);
        playerProfilDB.setMoney(playerProfilLocal.getMoney());

    }

}
