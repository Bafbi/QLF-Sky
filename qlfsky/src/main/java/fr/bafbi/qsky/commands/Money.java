package fr.bafbi.qsky.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.bafbi.qsky.Qsky;
import fr.bafbi.qsky.utils.Baltop;
import fr.bafbi.qsky.utils.PlayerProfilDB;
import fr.bafbi.qsky.utils.PlayerProfilLocal;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class Money implements TabExecutor {

    private final Qsky main;
    private ConfigurationSection textComponent;

    public Money(Qsky app) {
        this.main = app;
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
        
        if (!(sender instanceof Player)) {

            main.getLogger().info("This command is not executable on console, see \"/adminmoney\"");
            return true;

        }

        this.textComponent = main.getConfig().getConfigurationSection("textComponent.command.money");

        Player player = (Player) sender;
        PlayerProfilLocal playerProfilLocal = new PlayerProfilLocal(player);

        if (args.length < 1) {
            player.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("getBalance").replace("<data.money>", Long.toString(playerProfilLocal.getMoney())).replace("<config.currency.displayName>", main.getConfig().getString("currency.displayName"))));
            return false;
        }

        switch (args[0]) {
            case "baltop":
                {
                    Baltop.reCalc(main);

                    for (int index = 0; index < Baltop.getBaltop().size() && index < 10; index++) {

                        PlayerProfilDB playerProfilDB = new PlayerProfilDB(Baltop.getBaltop().get(index));

                        player.sendMessage(Component.text(playerProfilDB.getMoney() + main.getConfig().getString("currency.displayName") + " : " + playerProfilDB.getPseudo()));
                    }
                    
                }
                return true;
            default:
                return false;
        }
        
    }
    
}
