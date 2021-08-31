package fr.bafbi.qlfsky.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.bson.codecs.MapCodec;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import fr.bafbi.qlfsky.App;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class IslandProfilDB {

    private final static App main = App.getPlugin();
    private final MongoCollection<Document> islandCol = App.getIslandCol();
    private Document islandDoc;

    private final String Uuid;
    private String Name;
    private String LeaderUUID;
    private String LeaderPseudo;
    private final List<Double> PositionXZ;
    private List<Double> HomePosition;
    private List<String> MembersUUID;
    private List<String> MembersPseudo;
    private Map<String, Integer> PlayersUUID_PermissionLevel;
    private Map<String, Integer> Permissions_Level;
    private Boolean Deleted;

    public IslandProfilDB(String islandUuid) {

        this.Uuid = islandUuid;

        this.islandDoc = islandCol.find(Filters.eq("UUID", this.Uuid)).first();

        this.Name = islandDoc.getString("Name");
        this.LeaderUUID = islandDoc.getString("LeaderUUID");
        this.LeaderPseudo = islandDoc.getString("LeaderPseudo");
        this.PositionXZ = islandDoc.getList("Position", Double.class);
        this.HomePosition = islandDoc.getList("HomePosition", Double.class);
        this.MembersUUID = islandDoc.getList("MembersUUID", String.class);
        this.MembersPseudo = islandDoc.getList("MembersPseudo", String.class);
        try {
            this.PlayersUUID_PermissionLevel = (Map<String, Integer>) islandDoc.get("PlayersUUID_PermissionLevel", Map.class);
            this.Permissions_Level = (Map<String, Integer>) islandDoc.get("Permission_Level", Map.class);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        

        this.Deleted = islandDoc.getBoolean("Deleted");
        
    }
    public String getUUID() {
        return Uuid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.set("Name", name));
        this.Name = name;
    }

    public String getLeaderUUID() {
        return LeaderUUID;
    }

    public void setLeader(String leaderUUID) {
        String leaderPseudo = Bukkit.getPlayer(leaderUUID).getName();
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.set("LeaderUUID", leaderUUID));
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.set("LeaderPseudo", leaderPseudo));
        this.LeaderUUID = leaderUUID;
        this.LeaderPseudo = leaderPseudo;
    }

    public String getLeaderPseudo() {
        return LeaderPseudo;
    }

    public List<Double> getPositionXZ() {
        return PositionXZ;
    }

    public void setHomePosition(Double x, Double y, Double z) {
        this.HomePosition.set(0, x);
        this.HomePosition.set(1, y);
        this.HomePosition.set(2, z);
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.set("HomePosition", this.HomePosition));
    }

    public List<Double> getHomePosition() {
        return HomePosition;
    }

    public List<String> getMembersUUID() {
        return MembersUUID;
    }

    public void setMembers(List<String> membersUUID) {
        List<String> membersPseudo = new ArrayList<String>();
        for (String memberUUID : membersUUID) {
            membersPseudo.add(Bukkit.getPlayer(memberUUID).getName());
        }

        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.set("MembersUUID", membersUUID));
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.set("MembersPseudo", membersPseudo));
        this.MembersUUID = membersUUID;
        this.MembersPseudo = membersPseudo;
    }

    public List<String> getMembersPseudo() {
        return MembersPseudo;
    }

    public void setDeleted(Boolean deleted) {
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.set("Deleted", deleted));
        this.Deleted = deleted;
    }

    public Boolean getDeleted() {
        return Deleted;
    }

    public void addOneMember(Player player) {
        String playerUUID = player.getUniqueId().toString();
        String playerPseudo = player.getName();
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.addToSet("MembersUUID", playerUUID));
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.addToSet("MembersPseudo", playerPseudo));
        this.MembersUUID.add(playerUUID);
        this.MembersPseudo.add(playerPseudo);
    }

    public void removeOneMember(Player player) {
        String playerUUID = player.getUniqueId().toString();
        String playerPseudo = player.getName();
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.pull("MembersUUID", playerUUID));
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.pull("MembersPseudo", playerPseudo));
        this.MembersUUID.remove(playerUUID);
        this.MembersPseudo.remove(playerPseudo);
    }

    public void setPlayersUUID_PermissionLevel(Map<String, Integer> playersUUID_PermissionLevel) {
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.set("PlayersUUID_PermissionLevel", playersUUID_PermissionLevel));
        this.PlayersUUID_PermissionLevel = playersUUID_PermissionLevel;
    }

    public Map<String, Integer> getPlayersUUID_PermissionLevel() {
        return this.PlayersUUID_PermissionLevel;
    }

    public Integer getPermissionLevelOfPlayer(Player player) {
        Integer permLevel = this.PlayersUUID_PermissionLevel.get(player.getUniqueId().toString());
        if (permLevel == null) permLevel = 1;
        return permLevel;
    }
    public Integer getPermissionLevelOfPlayer(String playerUUID) {
        Integer permLevel = this.PlayersUUID_PermissionLevel.get(playerUUID);
        if (permLevel == null) permLevel = 1;
        return permLevel;
    }

    public void addOnePlayerUUID_PermissionLevel(Player player, Integer permissionLevel) {
        String playerUUID = player.getUniqueId().toString();
        this.PlayersUUID_PermissionLevel.put(playerUUID, permissionLevel);
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.set("PlayersUUID_PermissionLevel", this.PlayersUUID_PermissionLevel));
        
    }
    public void addOnePlayerUUID_PermissionLevel(String playerUUID, Integer permissionLevel) {
        this.PlayersUUID_PermissionLevel.put(playerUUID, permissionLevel);
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.set("PlayersUUID_PermissionLevel", this.PlayersUUID_PermissionLevel));
    }

    public void removeOnePlayerUUID_PermissionLevel(Player player) {
        String playerUUID = player.getUniqueId().toString();
        this.PlayersUUID_PermissionLevel.remove(playerUUID);
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.set("PlayersUUID_PermissionLevel", this.PlayersUUID_PermissionLevel));
    }
    public void removeOnePlayerUUID_PermissionLevel(String playerUUID) {
        this.PlayersUUID_PermissionLevel.remove(playerUUID);
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.set("PlayersUUID_PermissionLevel", this.PlayersUUID_PermissionLevel));
    }

    public void setPermissionLevelOfPlayer(Player player, Integer permissionLevel) {
        String playerUUID = player.getUniqueId().toString();
        this.PlayersUUID_PermissionLevel.replace(playerUUID, permissionLevel);
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.set("PlayersUUID_PermissionLevel", this.PlayersUUID_PermissionLevel));
    }
    public void setPermissionLevelOfPlayer(String playerUUID, Integer permissionLevel) {
        this.PlayersUUID_PermissionLevel.replace(playerUUID, permissionLevel);
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.set("PlayersUUID_PermissionLevel", this.PlayersUUID_PermissionLevel));
    }

    public void setPermissions_Level(Map<String, Integer> permissions_Level) {
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.set("Permissions_Level", permissions_Level));
        this.Permissions_Level = permissions_Level;
    }

    public Map<String, Integer> getPermissions_Level() {
        return this.Permissions_Level;
    }

    public void setLevelOfPermission(String permission, Integer level) {
        this.Permissions_Level.put(permission, level);
        this.islandCol.updateOne(Filters.eq("UUID", this.Uuid), Updates.set("Permissions_Level", this.Permissions_Level));
    }

    public Integer getLevelOfPermission(String permission) {
        return this.Permissions_Level.get(permission);
    }

    public static void createNewIslandProfil(Player player, String islandName) {

        MongoCollection<Document> islandCol = App.getIslandCol();
        Long countDoc = islandCol.countDocuments() + 5;
        List<Double> positionXZ = List.of( 200 * countDoc * Math.sin(countDoc * 20), 81.0, 200 * countDoc * Math.cos(countDoc * 20));
        String leaderUuid = player.getUniqueId().toString();
        UUID newUuid = null;
        boolean duplicate = true;

        while (duplicate) {
            newUuid = UUID.randomUUID();
            if (islandCol.find(Filters.eq("UUID", newUuid.toString())).first() == null) duplicate = false;
        }

        PlayerProfilDB playerProfilDB = new PlayerProfilDB(player);
        playerProfilDB.setIslandUUID(newUuid.toString());
        
        main.getLogger().info("Creating new Island for " + player.getName());
        Document profil = new Document("UUID", newUuid.toString())
            .append("Name", islandName)
            .append("LeaderUUID", leaderUuid)
            .append("LeaderPseudo", player.getName())
            .append("Position", positionXZ)
            .append("HomePosition", positionXZ)
            .append("MembersUUID", Arrays.asList(leaderUuid))
            .append("MembersPseudo", Arrays.asList(player.getName()))
            .append("PlayersUUID_PermissionLevel", getDefaultGuard())
            .append("Permissions_Level", Map.of("thing", 8))
            .append("Deleted", false);

        islandCol.insertOne(profil);

        Location loc = new Location(Bukkit.getWorld("sky"), positionXZ.get(0), 80, positionXZ.get(2));
        loc.getBlock().setType(Material.GRASS_BLOCK);

    }

    private static Map<String, Integer> getDefaultGuard() {
        ConfigurationSection config = main.getConfig().getConfigurationSection("defaultGuard");
        Map<String, Integer> defaultGuard = new HashMap<>();
        String guardKey;
        for (String section1 : config.getKeys(false)) {
            guardKey = section1;
            ConfigurationSection config2 = config.getConfigurationSection(section1);
            for (String section2 : config2.getKeys(false)) {
                guardKey = guardKey + "." + section2;
                defaultGuard.put(guardKey, config.getInt(guardKey));
                main.getLogger().info(guardKey);
            }
        }

        return defaultGuard;
    }

    public void deleteIslandProfil() {

        setDeleted(true);

        for (String memberUUID : this.MembersUUID) {

            Player member = Bukkit.getPlayer(UUID.fromString(memberUUID));
            PlayerProfilDB playerProfilDB = new PlayerProfilDB(member);
            playerProfilDB.setIslandUUID(null);
            member.sendMessage(Component.text("Your Island has just been deleted").color(NamedTextColor.RED));

        }
        
    }

    public void broadcast(Component message) {
        
        for (String memberUUID : MembersUUID) {

            Bukkit.getPlayer(UUID.fromString(memberUUID)).sendMessage(message);

        }

    }
    
}
