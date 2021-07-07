package fr.bafbi.qlfsky.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;

import fr.bafbi.qlfsky.App;

public class Menu implements TabExecutor {

    private App main;

    public Menu(App main) {

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
        
        if (sender instanceof Player) {

            Player player = (Player) sender;

            /*JSONObject customItemPart = (JSONObject) main.getCustomItemJSON().get("GUI");
            main.getLogger().info(customItemPart.toJSONString());
            JSONObject customItem = (JSONObject) customItemPart.get("is_create");
            Inventory menuGui = Bukkit.createInventory(player, 9);
            main.getLogger().info((String) main.getCustomItemJSON().get("item"));
            menuGui.setItem(4, new ItemStack(Material.getMaterial((String) customItem.get("material"))));

            customItemPart.put("aa", "bb");*/

            //player.openInventory(menuGui);

        }      

        


        return false;
    }
    
}
