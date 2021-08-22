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
import fr.bafbi.qlfsky.utils.PlayerProfilLocal;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

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
                if (args.length > 0 && args[0].equalsIgnoreCase("create")) {
                    String islandName;
                    if (args.length > 1) {
                        islandName = args[1];
                    } else {
                        islandName = player.getName() + "_Is"; 
                    }
                    IslandProfilDB.createNewIslandProfil(player, islandName);
                    player.sendTitle("Creating you a new island", ". . . " + islandName, 5, 20, 10);
                    Bukkit.dispatchCommand(player, "farm home");
                    return true;
                }
                player.sendMessage(Component.text("You need create an island [/farm create {name of the farm}]").color(NamedTextColor.YELLOW));
                return false;
            }

            IslandProfilDB islandProfilDB = new IslandProfilDB(playerProfilDB.getIslandUUID());
            PlayerProfilLocal playerProfilLocal = new PlayerProfilLocal(player);

            if (args.length < 1) return false;

            switch (args[0]) {
                case "create":

                    player.sendMessage(Component.text("You already have an island").color(NamedTextColor.YELLOW));
                    
                    break;
                case "home":

                    List<Double> position = islandProfilDB.getHomePosition();
                    Location location = new Location(Bukkit.getWorld("sky"), position.get(0), position.get(1), position.get(2));

                    playerProfilLocal.setLocationUUID(islandProfilDB.getUUID());

                    player.teleport(location);
                
                    break;
                case "delete":

                    islandProfilDB.deleteIslandProfil();
                
                    break;
                case "sethome":

                    if (!playerProfilLocal.getLocationUUID().equals(playerProfilDB.getIslandUUID())) {
                        player.sendMessage(Component.text("You need to be in your island").color(NamedTextColor.YELLOW));
                        return true;
                    }
                    Location playerLocation = player.getLocation();
                        islandProfilDB.setHomePosition(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());

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
