package fr.bafbi.qlfsky.commands;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.bafbi.qlfsky.App;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Profil implements TabExecutor {
    
    private App main;

    public Profil(App main) {

        this.main = main;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {
        
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        
        if (args.length < 1) return false;

        if (sender instanceof Player) {

            Player player = (Player) sender;

            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null) return false;
            
            Book profilBook = getProfilBook(target);

            if (profilBook == null) {

                player.sendActionBar(Component.text("Error, plz retry", NamedTextColor.RED));
                return false;

            } 
            
            player.openBook(profilBook);

            return true;

        }
        return false;

    }

    public Book getProfilBook(final Player target){

        String targetUUIDString = target.getUniqueId().toString();
        MongoCollection<Document> playerCol = App.getPlayerCol();
        Document playerDoc = playerCol.find(Filters.eq("UUID", targetUUIDString)).first();

        Component bookTitle = Component.text("???").decorate(TextDecoration.OBFUSCATED);
        Component bookAuthor = Component.text("Server");
        Collection<Component> bookPages = new ArrayList<>();
        TextComponent firstPage = Component.text("           ").append(target.displayName()).append(Component.newline()).append(Component.newline());

        BufferedImage skinHeadTexture = null;
        try {
            URL url = new URL("https://minotar.net/avatar/" + targetUUIDString + "/8.png");
            skinHeadTexture = ImageIO.read(url);
        } catch (Exception e) {
            System.err.println("Could not get skin data from session servers!");
            e.printStackTrace();
            main.getLogger().warning("Could not get skin data from session servers!");
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

            main.getLogger().info(key + " : " + value.toString());

            bookPages.add(Component.text(key + " :\n" + value.toString()));

        });

        Book myBook = Book.book(bookTitle, bookAuthor, bookPages);
        
        return myBook;
    }
    
}
