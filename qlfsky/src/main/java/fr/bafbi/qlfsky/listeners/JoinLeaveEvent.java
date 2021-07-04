package fr.bafbi.qlfsky.listeners;

import java.util.Date;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.bafbi.qlfsky.App;
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
        MongoCollection<Document> playerCol = App.getPlayerCol();
        Document playerDoc = playerCol.find(Filters.eq("UUID", player.getUniqueId().toString())).first();

        if (playerDoc != null) {

            main.getLogger().info("Updating profil for " + player.getName());

            playerCol.updateOne(Filters.eq("UUID", player.getUniqueId().toString()), Updates.currentDate("Connection_Time"));
        } 
        else {

            Bukkit.broadcast(GsonComponentSerializer.gson().deserialize(textComponent.getString("firstConnection").replace("<player.name>", player.getName()).replace("<player.connection.rank>", Long.toString(playerCol.countDocuments() + 1))));

            main.getLogger().info("Creating profil for " + player.getName());
            Document profil = new Document("UUID", player.getUniqueId().toString())
                .append("Name", player.getName())
                .append("Connection_Time", new Date());
            
            playerCol.insertOne(profil);

        }  
    }

}
