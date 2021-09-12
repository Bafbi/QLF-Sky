package fr.bafbi.qsky.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import fr.bafbi.qsky.Qsky;

public class PlayerProfilLocal {

    private final Qsky main = Qsky.getPlugin();
    private final Player player;
    private final PersistentDataContainer data;

    private final NamespacedKey moneyKey = new NamespacedKey(main, "Money");
    //private final NamespacedKey modificationPermissionKey = new NamespacedKey(main, "ModificationPermission");
    private final NamespacedKey locationUUIDKey = new NamespacedKey(main, "LocationUUID");
    private final NamespacedKey islandInviteUUIDKey = new NamespacedKey(main, "IslandInviteUUID");
    //private final NamespacedKey tpaPlayerUUIDKey = new NamespacedKey(main, "TpaPlayersUUID");

    private Long money;
    //private Integer modificationPermission;
    private String locationUUID;
    private String islandInviteUUID;
    //private String[] tpaPlayersUUID;
    
    public PlayerProfilLocal(Player player) {

        this.player = player;

        this.data = this.player.getPersistentDataContainer();

        this.money = this.data.get(this.moneyKey, PersistentDataType.LONG);
        if (this.money == null) this.setMoney(0);

        // this.modificationPermission = this.data.get(this.modificationPermissionKey, PersistentDataType.INTEGER);
        // if (this.modificationPermission == null) this.setModificationPermission(0);

        this.locationUUID = this.data.get(locationUUIDKey, PersistentDataType.STRING);
        if (this.locationUUID == null) this.setLocationUUID("spawn");

        this.islandInviteUUID = this.data.get(islandInviteUUIDKey, PersistentDataType.STRING);
        if (this.islandInviteUUID == null) this.setIslandInviteUUID("null");

        //this.tpaPlayersUUID = this.data.get(tpaPlayerUUIDKey, new ListStringDataType());

    }


    public long getMoney() {
        return this.money;
    }
    public void setMoney(long money) {
        this.data.set(moneyKey, PersistentDataType.LONG, money);
        this.money = money;
    }

    // public int getModificationPermission() {
    //     return this.modificationPermission;
    // }
    // public void setModificationPermission(int modificationPermission) {
    //     this.data.set(modificationPermissionKey, PersistentDataType.INTEGER, modificationPermission);
    //     this.modificationPermission = modificationPermission;
    // }

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

    /*public String[] getTpaPlayersUUID() {
        return this.tpaPlayersUUID;
    }

    public void setTpaPlayersUUID(@NotNull String[] tpaPlayersUUID) {
        this.data.set(this.tpaPlayerUUIDKey, new ListStringDataType(), tpaPlayersUUID);
        this.tpaPlayersUUID = tpaPlayersUUID;
    }

    public void addTpaPlayerUUID(@NotNull String tpaPlayerUUID) {
        List<String> listTpaPlayersUUID = new ArrayList<>();
        if (this.tpaPlayersUUID != null) {
            for (String playerUUID : this.tpaPlayersUUID) {
            listTpaPlayersUUID.add(playerUUID);
            }
        }
        listTpaPlayersUUID.add(tpaPlayerUUID);
        String[] fee = {"fee"};
        this.tpaPlayersUUID = fee;
        this.tpaPlayersUUID = listTpaPlayersUUID.toArray(this.tpaPlayersUUID);
        this.data.set(this.tpaPlayerUUIDKey, new ListStringDataType(), this.tpaPlayersUUID);
    }

    public void removeLastTpaPlayerUUID() {
        List<String> listTpaPlayersUUID = new ArrayList<>();
        for (String playerUUID : this.tpaPlayersUUID) {
            listTpaPlayersUUID.add(playerUUID);
        }
        listTpaPlayersUUID.remove(listTpaPlayersUUID.size() - 1);
        main.getLogger().info(listTpaPlayersUUID.toString());
        if (listTpaPlayersUUID.isEmpty()) {
            this.removeTpaPlayerUUID();
            return;
        }
        this.tpaPlayersUUID = listTpaPlayersUUID.toArray(this.tpaPlayersUUID);
        this.data.set(this.tpaPlayerUUIDKey, new ListStringDataType(), this.tpaPlayersUUID);
    }
    public void removeFirstTpaPlayerUUID() {
        List<String> listTpaPlayersUUID = new ArrayList<>();
        for (String playerUUID : this.tpaPlayersUUID) {
            listTpaPlayersUUID.add(playerUUID);
        }
        listTpaPlayersUUID.remove(0);
        main.getLogger().info(listTpaPlayersUUID.toString());
        if (listTpaPlayersUUID.isEmpty()) {
            this.removeTpaPlayerUUID();
            return;
        }
        this.tpaPlayersUUID = listTpaPlayersUUID.toArray(this.tpaPlayersUUID);
        this.data.set(this.tpaPlayerUUIDKey, new ListStringDataType(), this.tpaPlayersUUID);
    }
    public void removeOneTpaPlayerUUID(Integer index) {
        List<String> listTpaPlayersUUID = new ArrayList<>();
        for (String playerUUID : this.tpaPlayersUUID) {
            listTpaPlayersUUID.add(playerUUID);
        }
        listTpaPlayersUUID.remove(index);
        main.getLogger().info(listTpaPlayersUUID.toString());
        if (listTpaPlayersUUID.isEmpty()) {
            this.removeTpaPlayerUUID();
            return;
        }
        this.tpaPlayersUUID = listTpaPlayersUUID.toArray(this.tpaPlayersUUID);
        this.data.set(this.tpaPlayerUUIDKey, new ListStringDataType(), this.tpaPlayersUUID);
    }
    public void removeTpaPlayerUUID() {
        this.data.remove(tpaPlayerUUIDKey);
    }*/


    
}
