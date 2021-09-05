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

public class Fly implements TabExecutor {

    private App main;

    public Fly(App main) {
        this.main = main;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        Bukkit.getServer().getScoreboardManager().getMainScoreboard().getTeam("admin").addEntry(player.getName());
        return false;
    }
    
}
