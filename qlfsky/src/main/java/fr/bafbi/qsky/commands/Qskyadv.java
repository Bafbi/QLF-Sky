package fr.bafbi.qsky.commands;

import eu.endercentral.crazy_advancements.Advancement;
import eu.endercentral.crazy_advancements.AdvancementDisplay;
import eu.endercentral.crazy_advancements.AdvancementVisibility;
import eu.endercentral.crazy_advancements.NameKey;
import fr.bafbi.qsky.Qsky;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
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

        AdvancementDisplay rootDisplay = new AdvancementDisplay(Material.ARMOR_STAND, "My Custom Advancements", "With cool additions", AdvancementDisplay.AdvancementFrame.TASK, false, false, AdvancementVisibility.ALWAYS);
        rootDisplay.setBackgroundTexture("textures/block/yellow_concrete.png");
        Advancement root = new Advancement(null, new NameKey("custom", "root"), rootDisplay);

        AdvancementDisplay childrenDisplay = new AdvancementDisplay(Material.ENDER_EYE, "To the right", "Your Goal", AdvancementDisplay.AdvancementFrame.GOAL, true, true, AdvancementVisibility.VANILLA);
        childrenDisplay.setCoordinates(1, 0);//x, y
        Advancement children = new Advancement(root, new NameKey("custom", "right"), childrenDisplay);

        main.advancementManager.addAdvancement(root, children);
        main.advancementManager.displayMessage(player, children);

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
