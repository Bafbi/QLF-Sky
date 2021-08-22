package fr.bafbi.qlfsky.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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

    

    public static void createNewIslandProfil(Player player, String islandName) {

        MongoCollection<Document> islandCol = App.getIslandCol();
        Long countDoc = islandCol.countDocuments() + 5;
        List<Double> positionXZ = List.of( 200 * countDoc * Math.sin(countDoc * 20), 200 * countDoc * Math.cos(countDoc * 20));
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
            .append("MembersName", Arrays.asList(player.getName()))
            .append("Deleted", false);

        islandCol.insertOne(profil);

        Location loc = new Location(Bukkit.getWorld("sky"), positionXZ.get(0), 80, positionXZ.get(2));
        loc.getBlock().setType(Material.GRASS_BLOCK);

    }

    public void deleteIslandProfil() {

        setDeleted(true);

        for (String memberUUID : MembersUUID) {

            PlayerProfilDB playerProfilDB = new PlayerProfilDB(Bukkit.getPlayer(UUID.fromString(memberUUID)));
            playerProfilDB.setIslandUUID(null);
            Bukkit.getPlayer(UUID.fromString(memberUUID)).sendMessage(Component.text("Your Island has just been deleted").color(NamedTextColor.RED));

        }
        

    }
    
}
