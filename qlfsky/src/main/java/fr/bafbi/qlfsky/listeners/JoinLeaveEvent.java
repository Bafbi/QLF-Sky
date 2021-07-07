package fr.bafbi.qlfsky.listeners;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.bafbi.qlfsky.App;
import fr.bafbi.qlfsky.utils.PlayerProfil;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class JoinLeaveEvent implements Listener {
    
    private App main;
    private ConfigurationSection textComponent;

    public JoinLeaveEvent(App main) {

        this.main = main;
        this.textComponent = main.getConfig().getConfigurationSection("textComponent.event.joinLeave");

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        PlayerProfil playerProfil = new PlayerProfil(player);

        if (playerProfil.getPlaytime() == 0.0) event.joinMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("firstConnection").replace("<player.name>", player.getName()).replace("<player.connection.rank>", Long.toString(playerProfil.countProfils() + 1))));
        else event.joinMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("connection").replace("<player.name>", player.getName())));

        playerProfil.setOnline(true);
        playerProfil.setConnectionTime(new Date());

        if (!playerProfil.getPlayedServerVersion().contains(main.getServerVersion())) playerProfil.addPlayedServerVersion(main.getServerVersion());  

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        MongoCollection<Document> playerCol = App.getPlayerCol();
        Document playerDoc = playerCol.find(Filters.eq("UUID", player.getUniqueId().toString())).first();
        Date connectionTime = playerDoc.getDate("Connection_Time");

        event.quitMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("disconnect").replace("<player.name>", player.getName())));

        playerCol.updateOne(Filters.eq("UUID", player.getUniqueId().toString()), Updates.currentDate("Last_Connection"));
        playerCol.updateOne(Filters.eq("UUID", player.getUniqueId().toString()), Updates.set("Online", false));
        playerCol.updateOne(Filters.eq("UUID", player.getUniqueId().toString()), Updates.inc("Playtime", (Double) ((new Date().getTime() - connectionTime.getTime()) / 1000.0 / 60.0 / 60.0)));

    }

}
