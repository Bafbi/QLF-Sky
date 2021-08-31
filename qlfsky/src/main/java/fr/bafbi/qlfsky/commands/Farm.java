package fr.bafbi.qlfsky.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;

import fr.bafbi.qlfsky.App;
import fr.bafbi.qlfsky.scheduling.CancelInvitation;
import fr.bafbi.qlfsky.utils.IslandProfilDB;
import fr.bafbi.qlfsky.utils.PlayerProfilDB;
import fr.bafbi.qlfsky.utils.PlayerProfilLocal;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class Farm implements TabExecutor {

    private App main;
    private final ConfigurationSection textComponent;

    public Farm(App main) {

        this.main = main;
        this.textComponent = main.getConfig().getConfigurationSection("textComponent.command.farm");

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
            PlayerProfilLocal playerProfilLocal = new PlayerProfilLocal(player);

            if (playerProfilDB.getIslandUUID() == null) {
                if (args.length > 0) {
                    switch (args[0]) {
                    case "create":

                        String islandName;
                        if (args.length > 1) {
                            islandName = args[1];
                        } else {
                            islandName = player.getName() + "_Is"; 
                        }
                        IslandProfilDB.createNewIslandProfil(player, islandName);
                        player.sendTitle("Creating you a new island", ". . . " + islandName, 5, 20, 10);
                        Bukkit.dispatchCommand(player, "farm home");
                        
                        break;
                    case "join":
                
                        if (playerProfilLocal.getIslandInviteUUID() == "null") {
                            player.sendMessage(Component.text("You don't have any invitation").color(NamedTextColor.YELLOW));
                            return true;
                        }

                        String islandInviteUUID = playerProfilLocal.getIslandInviteUUID();
                        IslandProfilDB islandProfilDBInvited = new IslandProfilDB(islandInviteUUID);

                        islandProfilDBInvited.addOneMember(player);
                        islandProfilDBInvited.addOnePlayerUUID_PermissionLevel(player, 5);
                        playerProfilDB.setIslandUUID(islandInviteUUID);
                        playerProfilLocal.setIslandInviteUUID("null");

                        Bukkit.dispatchCommand(player, "farm home");

                        break;
                    case "reject":

                        if (playerProfilLocal.getIslandInviteUUID() == "null") {
                            player.sendMessage(Component.text("You don't have any invitation").color(NamedTextColor.YELLOW));
                            return true;
                        }  

                        playerProfilLocal.setIslandInviteUUID("null");
                        player.sendMessage(Component.text("You reject the offer").color(NamedTextColor.YELLOW));

                        break;
                    default:
                        player.sendMessage(Component.text("You need create an island [/farm create {name of the farm}] or join one").color(NamedTextColor.YELLOW));
                        break;
                    }
                } else {
                    player.sendMessage(Component.text("You need create an island [/farm create {name of the farm}] or join one").color(NamedTextColor.YELLOW));
                }
                
                return true;
            }

            if (args.length < 1) return false;
            
            IslandProfilDB islandProfilDB = new IslandProfilDB(playerProfilDB.getIslandUUID());

            

            switch (args[0]) {
                case "create":

                    if (playerProfilDB.getIslandUUID() != null) {
                        player.sendMessage(Component.text("You already have an island").color(NamedTextColor.YELLOW));
                        return true;
                    }

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
                case "invite":
                    {
                        if (args.length < 2) {
                            player.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("invite.error.missingSecondArg")));
                            return true;
                        }

                        Player playerInvited = Bukkit.getPlayer(args[1]);
                        if (playerInvited == null) {
                            player.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("invite.error.missingPlayer")));
                            return true;
                        }

                        if (playerInvited.equals(player)) {
                            player.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("invite.error.selfInvite")));
                            return true;
                        }

                        PlayerProfilLocal playerInvitedProfilLocal = new PlayerProfilLocal(playerInvited);
                        PlayerProfilDB playerInvitedProfilDB = new PlayerProfilDB(playerInvited);

                        if (playerInvitedProfilDB.getIslandUUID() != null) {
                            player.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("invite.error.haveIsland")));
                            return true;
                        }

                        if (playerInvitedProfilLocal.getIslandInviteUUID() != "null") {
                            player.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("invite.error.haveInvite")));
                            return true;
                        }

                        islandProfilDB.broadcast(GsonComponentSerializer.gson().deserialize(textComponent.getString("invite.broadcastInvite").replace("<player.name>", player.getName()).replace("<invitedPlayer.name>", playerInvited.getName())));
                        playerInvitedProfilLocal.setIslandInviteUUID(playerProfilDB.getIslandUUID());
                        playerInvited.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("invite.invitation").replace("<player.name>", player.getName()).replace("<island.name>", islandProfilDB.getName())));

                        new CancelInvitation(player, playerInvited, this.main).runTaskLater(this.main, 600);
                    }
                    break;
                case "cancelInvite":
                    {
                        if (args.length < 2) {
                            player.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("cancelInvite.error.missingSecondArg")));
                            return true;
                        }

                        Player playerSelected = Bukkit.getPlayer(args[1]);
                        if (playerSelected == null) {
                            player.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("cancelInvite.error.missingPlayer")));
                            return true;
                        }

                        PlayerProfilLocal playerSelectedProfilLocal = new PlayerProfilLocal(playerSelected);

                        if (!playerSelectedProfilLocal.getIslandInviteUUID().equals(playerProfilDB.getIslandUUID())) {
                            player.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("cancelInvite.error.notInvited").replace("<selectedPlayer.name>", playerSelected.getName())));
                            return true;
                        }

                        playerSelectedProfilLocal.setIslandInviteUUID("null");
                        playerSelected.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("cancelInvite.invitationCancel").replace("<player.name>", player.getName())));
                        islandProfilDB.broadcast(GsonComponentSerializer.gson().deserialize(textComponent.getString("cancelInvite.invitationCancelBroadcast").replace("<player.name>", player.getName()).replace("<selectedPlayer.name>", playerSelected.getName())));
                    }
                    break;
                case "join":

                if (playerProfilDB.getIslandUUID() != null) {
                    player.sendMessage(Component.text("You already have an island").color(NamedTextColor.YELLOW));
                    return true;
                }
                
                    break;
                case "perm":
                    {
                        if (args.length < 3) {
                            return false;
                        }

                        Player playerSelected = Bukkit.getPlayer(args[1]);
                        if (playerSelected == null) {
                            player.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("cancelInvite.error.missingPlayer")));
                            return true;
                        }

                        Integer permLevel;
                        try {
                            permLevel = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            player.sendMessage(Component.text("erreur sur le nombre donné").color(NamedTextColor.YELLOW));
                            return false;
                        }
                        
                        islandProfilDB.setPermissionLevelOfPlayer(playerSelected, permLevel);
                    }
                    break;
                case "leave":
                    {
                        if (islandProfilDB.getLeaderUUID().equals(player.getUniqueId().toString())) {
                            player.sendMessage(Component.text("vous ne pouvez quitter l'ile dont vous etes le leader").color(NamedTextColor.YELLOW));
                            return true;
                        }
                        
                        playerProfilDB.setIslandUUID(null);
                        islandProfilDB.removeOneMember(player);
                        islandProfilDB.removeOnePlayerUUID_PermissionLevel(player);
                        Bukkit.dispatchCommand(player, "spawn");
                    }
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

        


        return true;
    }
    
}
