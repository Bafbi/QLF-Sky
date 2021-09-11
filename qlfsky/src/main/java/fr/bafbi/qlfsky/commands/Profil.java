package fr.bafbi.qlfsky.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.bafbi.qlfsky.Qsky;
import fr.bafbi.qlfsky.utils.PlayerProfilDB;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Profil implements TabExecutor {
    
    private Qsky main;

    public Profil(Qsky main) {

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
            
            Book profilBook = PlayerProfilDB.getProfilBook(target);

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
