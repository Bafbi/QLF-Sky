package fr.bafbi.qlfsky.utils;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Arrays;

import javax.imageio.ImageIO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.bukkit.entity.Player;

import fr.bafbi.qlfsky.App;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextColor;

public class PlayerProfil {

    private final App main = App.getPlugin();
    private final Player player;
    private final MongoCollection<Document> playerCol = App.getPlayerCol();
    private Document playerDoc;

    private final String UUID;
    private String Pseudo;
    private boolean Online;
    private List<Double> Played_Server_Version;
    private final Date First_Connection;
    private Date Connection_Time;
    private Date Last_Connection;
    private double Playtime;
    private long Money;

    public PlayerProfil(Player player) {

        this.player = player;

        this.UUID = player.getUniqueId().toString();        

        this.playerDoc = this.playerCol.find(Filters.eq("UUID", this.UUID)).first();

        if (playerDoc == null) {

            createPlayerProfil();
            this.playerDoc = this.playerCol.find(Filters.eq("UUID", this.UUID)).first();

        } 

        this.Pseudo = playerDoc.getString("Pseudo");
        this.Online = playerDoc.getBoolean("Online");
        this.Played_Server_Version = playerDoc.getList("Played_Server_Version", Double.class);
        this.First_Connection = playerDoc.getDate("First_Connection");
        this.Connection_Time = playerDoc.getDate("Connection_Time");
        this.Last_Connection = playerDoc.getDate("Last_Connection");
        this.Playtime = playerDoc.getDouble("Playtime");
        this.Money = playerDoc.getLong("Money");

    }


    public String getPseudo() {
        return this.Pseudo;
    }
    public void setPseudo(String pseudo) {
        this.playerCol.updateOne(Filters.eq("UUID", this.UUID), Updates.set("Pseudo", pseudo));
        this.Pseudo = pseudo;
    }

    public boolean getOnline() {
        return this.Online;
    }
    public void setOnline(boolean online) {
        this.playerCol.updateOne(Filters.eq("UUID", this.UUID), Updates.set("Online", online));
        this.Online = online;
    }

    public List<Double> getPlayedServerVersion() {
        return this.Played_Server_Version;
    }
    public void addPlayedServerVersion(double serverVersion) {
        this.playerCol.updateOne(Filters.eq("UUID", this.UUID), Updates.push("Played_Server_Version", serverVersion));
        this.Played_Server_Version.add(serverVersion);
    }

    public Date getFirstConnection() {
        return this.First_Connection;
    }

    public Date getConnectionTime() {
        return this.Connection_Time;
    }
    public void setConnectionTime(Date connectionTime) {
        this.playerCol.updateOne(Filters.eq("UUID", this.UUID), Updates.set("Connection_Time", connectionTime));
        this.Connection_Time = connectionTime;
    }

    public Date getLastConnection() {
        return this.Last_Connection;
    }
    public void setLastConnection(Date lastConnection) {
        this.playerCol.updateOne(Filters.eq("UUID", this.UUID), Updates.set("Last_Connection", lastConnection));
        this.Last_Connection = lastConnection;
    }

    public double getPlaytime() {
        return this.Playtime;
    }
    public void setPlaytime(double playtime) {
        this.playerCol.updateOne(Filters.eq("UUID", this.UUID), Updates.set("Playtime", playtime));
        this.Playtime = playtime;
    }

    public long getMoney() {
        return this.Money;
    }
    public void setMoney(long money) {
        this.playerCol.updateOne(Filters.eq("UUID", this.UUID), Updates.set("Money", money));
        this.Money = money;
    }

    public long countProfils() {
        return this.playerCol.countDocuments();
    }


    private void createPlayerProfil() {

        main.getLogger().info("Creating profil for " + player.getName());
        Document profil = new Document("UUID", player.getUniqueId().toString())
            .append("Pseudo", player.getName())
            .append("Online", true)
            .append("Played_Server_Version", Arrays.asList(main.getServerVersion()))
            .append("Connection_Time", new Date())
            .append("First_Connection", new Date())
            .append("Last_Connection", new Date())
            .append("Playtime", 0.0)
            .append("Money", (long) 100);

        
        this.playerCol.insertOne(profil);

    }


    public static Book getProfilBook(final Player target){

        String targetUUIDString = target.getUniqueId().toString();
        MongoCollection<Document> playerCol = App.getPlayerCol();
        Document playerDoc = playerCol.find(Filters.eq("UUID", targetUUIDString)).first();

        Component bookTitle = Component.text("???").decorate(TextDecoration.OBFUSCATED);
        Component bookAuthor = Component.text("Server");
        Collection<Component> bookPages = new ArrayList<>();
        TextComponent firstPage = Component.text("           ").append(target.displayName()).append(Component.newline()).append(Component.newline());

        BufferedImage skinHeadTexture = null;
        try {
            URL url = new URL("https://minotar.net/helm/" + targetUUIDString + "/8.png");
            skinHeadTexture = ImageIO.read(url);
        } catch (Exception e) {
            System.err.println("Could not get skin data from session servers!");
            e.printStackTrace();
            return null;
        }
        
        //main.getLogger().info(skinHeadTexture.getWidth() + " : " + skinHeadTexture.getHeight());

        firstPage = firstPage.append(Component.newline()).append(Component.text("      "));
        for (int y = 0; y < skinHeadTexture.getHeight() ; y++) {
            for (int x = 0; x < skinHeadTexture.getWidth() ; x++) {
                int color = skinHeadTexture.getRGB(x, y);
                //main.getLogger().info(color + " at " + x + " : " + y);
                firstPage = firstPage.append(Component.text("▉", TextColor.color(color)));
            }
            firstPage = firstPage.append(Component.newline()).append(Component.text("      "));
        }

        bookPages.add(firstPage);
        playerDoc.forEach((key, value) -> {

            bookPages.add(Component.text(key + " :\n" + value.toString()));

        });

        Book myBook = Book.book(bookTitle, bookAuthor, bookPages);
        
        return myBook;
    }
    
}
