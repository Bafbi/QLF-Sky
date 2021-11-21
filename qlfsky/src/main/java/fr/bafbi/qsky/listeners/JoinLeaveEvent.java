package fr.bafbi.qsky.listeners;

import java.util.Date;

import eu.endercentral.crazy_advancements.Advancement;
import eu.endercentral.crazy_advancements.CrazyAdvancements;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.bafbi.qsky.Qsky;
import fr.bafbi.qsky.utils.PlayerProfilDB;
import fr.bafbi.qsky.utils.PlayerProfilLocal;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class JoinLeaveEvent implements Listener {
    
    private final Qsky main;
    private ConfigurationSection textComponent;

    public JoinLeaveEvent(Qsky main) {
        this.main = main;
    }

    private void loadTextComponent() {
        this.textComponent = main.getConfig().getConfigurationSection("textComponent.event.joinLeave");
    } 

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        loadTextComponent();

        Player player = event.getPlayer();
        PlayerProfilDB playerProfilDB = new PlayerProfilDB(player);
        PlayerProfilLocal playerProfilLocal= new PlayerProfilLocal(player);

        if (playerProfilDB.getPlaytime() == 0.0) {
            event.joinMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("firstConnection").replace("<player.name>", player.getName()).replace("<player.connection.rank>", Long.toString(playerProfilDB.countProfils() + 1))));

            Bukkit.dispatchCommand(player, "spawn");
        }
        else event.joinMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("connection").replace("<player.name>", player.getName())));

        playerProfilDB.setOnline(true);
        playerProfilDB.setConnectionTime(new Date());
        playerProfilLocal.setMoney(playerProfilDB.getMoney());
        playerProfilLocal.setIslandInviteUUID("null");
        main.advancementManager.addPlayer(player);
        AdvancementManager.getAccessibleManager("main").addPlayer(player);

        if (!playerProfilDB.getPlayedServerVersion().contains(main.getServerVersion())) playerProfilDB.addPlayedServerVersion(main.getServerVersion());  

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {

        loadTextComponent();

        Player player = event.getPlayer();
        PlayerProfilDB playerProfilDB = new PlayerProfilDB(player);
        PlayerProfilLocal playerProfilLocal= new PlayerProfilLocal(player);

        event.quitMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("disconnect").replace("<player.name>", player.getName())));

        playerProfilDB.update(playerProfilLocal.getMoney());
        playerProfilDB.setOnline(false);
        playerProfilLocal.setIslandInviteUUID("null");

    }

}
