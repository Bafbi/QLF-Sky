package fr.bafbi.qlfsky.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.bafbi.qlfsky.App;
import fr.bafbi.qlfsky.utils.PlayerProfil;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

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
            
            Book profilBook = PlayerProfil.getProfilBook(target);

            if (profilBook == null) {

                player.sendActionBar(Component.text("Error, plz retry", NamedTextColor.RED));
                return false;

            } 
            
            player.openBook(profilBook);

            return true;

        }
        return false;

    }

    
}
