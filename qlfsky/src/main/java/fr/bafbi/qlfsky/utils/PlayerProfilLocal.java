package fr.bafbi.qlfsky.utils;

import java.util.List;
import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import fr.bafbi.qlfsky.App;

public class PlayerProfilLocal {

    private final App main = App.getPlugin();
    private final Player player;
    private final PersistentDataContainer data;

    private final NamespacedKey moneyKey = new NamespacedKey(main, "Money");
    private final NamespacedKey modificationPermissionKey = new NamespacedKey(main, "ModificationPermission");
    private final NamespacedKey locationUUIDKey = new NamespacedKey(main, "LocationUUID");
    private final NamespacedKey islandInviteUUIDKey = new NamespacedKey(main, "IslandInviteUUID");

    private Long money;
    private Integer modificationPermission;
    private String locationUUID;
    private String islandInviteUUID;
    
    public PlayerProfilLocal(Player player) {

        this.player = player;

        this.data = this.player.getPersistentDataContainer();

        this.money = this.data.get(this.moneyKey, PersistentDataType.LONG);
        if (this.money == null) this.setMoney(0);

        this.modificationPermission = this.data.get(this.modificationPermissionKey, PersistentDataType.INTEGER);
        if (this.modificationPermission == null) this.setModificationPermission(0);

        this.locationUUID = this.data.get(locationUUIDKey, PersistentDataType.STRING);
        if (this.locationUUID == null) this.setLocationUUID("spawn");

        this.islandInviteUUID = this.data.get(islandInviteUUIDKey, PersistentDataType.STRING);
        if (this.islandInviteUUID == null) this.setIslandInviteUUID("null");

    }


    public long getMoney() {
        return this.money;
    }
    public void setMoney(long money) {
        this.data.set(moneyKey, PersistentDataType.LONG, money);
        this.money = money;
    }

    public int getModificationPermission() {
        return this.modificationPermission;
    }
    public void setModificationPermission(int modificationPermission) {
        this.data.set(modificationPermissionKey, PersistentDataType.INTEGER, modificationPermission);
        this.modificationPermission = modificationPermission;
    }

    public String getLocationUUID() {
        return this.locationUUID;
    }
    public void setLocationUUID(String locationUUID) {
        this.data.set(this.locationUUIDKey, PersistentDataType.STRING, locationUUID);
        this.locationUUID = locationUUID;
    }

    public String getIslandInviteUUID() {
        return this.islandInviteUUID;
    }
    public void setIslandInviteUUID(String islandInviteUUID) {
        this.data.set(this.islandInviteUUIDKey, PersistentDataType.STRING, islandInviteUUID);
        this.islandInviteUUID= islandInviteUUID;
    }
    
}
