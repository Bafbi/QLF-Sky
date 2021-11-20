package fr.bafbi.qsky.utils;

import com.mongodb.lang.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class PermGui {

    Map<String, Integer> permissions;

    public PermGui(String islandUUID) {

        IslandProfilDB islandProfilDB = new IslandProfilDB(islandUUID);

        permissions = islandProfilDB.getPermissions_Level();

    }

    public Inventory openGui(@Nullable String sub) {

        Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER);

        return inv;

    }

}
