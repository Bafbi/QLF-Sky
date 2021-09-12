package fr.bafbi.qsky.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.bafbi.qsky.Qsky;

public class Baltop {

    private static List<String> baltopList = new ArrayList<>();

    private static final MongoCollection<Document> playerCol = Qsky.getPlayerCol();
    
    public Baltop() {

    }

    public static List<String> getBaltop() {
        return baltopList;
    }

    public static void setBaltop(List<String> baltop) {
        Baltop.baltopList = baltop;
    }

    public static List<String> reCalc(Qsky main) {

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerProfilDB playerProfilDB = new PlayerProfilDB(player);
            PlayerProfilLocal playerProfilLocal = new PlayerProfilLocal(player);
            playerProfilDB.update(playerProfilLocal.getMoney());
        }

        if (!baltopList.isEmpty()) baltopList.clear();
        for (Document doc : playerCol.find().sort(Sorts.descending("Money"))) {
            //main.getLogger().info(doc.getString("UUID"));
            baltopList.add(doc.getString("UUID"));
        }
        //main.getLogger().info(baltopList.toString());

        return baltopList;
    }

}
