package fr.bafbi.qlfsky.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import fr.bafbi.qlfsky.utils.IslandProfilDB;
import fr.bafbi.qlfsky.utils.PlayerProfilDB;

public class Farm implements TabExecutor {

    private App main;

    public Farm(App main) {

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
            PlayerProfilDB playerProfilDB = new PlayerProfilDB(player);

            if (playerProfilDB.getIslandUUID() == null) {
               IslandProfilDB.createNewIslandProfil(player, player.getName() + "_Is"); 
               player.sendTitle("Creating you a new island", "", 15, 30, 15);
               return true;
            } 

            IslandProfilDB islandProfilDB = new IslandProfilDB(playerProfilDB.getIslandUUID());

            if (args.length < 1) return false;

            switch (args[0]) {
                case "create":

                    if (playerProfilDB.getIslandUUID() == null) IslandProfilDB.createNewIslandProfil(player, args[1]);
                    else player.sendTitle("You already have an island", "", 15, 30, 15);
                    
                    break;
                case "home":

                    List<Double> position = islandProfilDB.getHomePosition();
                    Location location = new Location(Bukkit.getWorld("sky"), position.get(0), position.get(1), position.get(2));
                    player.teleport(location);
                
                    break;
                case "sah":

                
                
                    break;
                default:
                    break;
            }

            

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
