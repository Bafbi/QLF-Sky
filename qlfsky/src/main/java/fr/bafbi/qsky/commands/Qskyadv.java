package fr.bafbi.qsky.commands;

import eu.endercentral.crazy_advancements.NameKey;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.advancement.AdvancementVisibility;
import fr.bafbi.qsky.Qsky;
import fr.bafbi.qsky.advancements.QskyAdvancementsManager;
import fr.bafbi.qsky.advancements.TutoAdvancements;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Qskyadv implements TabExecutor {

    private final Qsky main;

    public Qskyadv(Qsky qsky) {
        this.main = qsky;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) return true;

        player.sendMessage(main.advancementManager.getName().toString());
        for (Player playerInManager : main.advancementManager.getPlayers()) {
            player.sendMessage(playerInManager.getName());
        }

        main.advancementManager.getAdvancements().forEach(adv -> {
            main.advancementManager.grantAdvancement(player, adv);
        });

        main.advancementManager.saveProgress(player);


        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
