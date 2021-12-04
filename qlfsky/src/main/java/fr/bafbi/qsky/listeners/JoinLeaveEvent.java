package fr.bafbi.qsky.listeners;

import java.util.Date;

import eu.endercentral.crazy_advancements.JSONMessage;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.advancement.ToastNotification;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.KeybindComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

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
            new ToastNotification(Material.PUFFERFISH_BUCKET, "Bienvenue " + player.getName() + ", J'suis Bob le PufferFish", AdvancementDisplay.AdvancementFrame.CHALLENGE).send(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    BaseComponent component = new TextComponent("Je t'ai fait un tutoriel, appui sur [");
                    component.addExtra(new KeybindComponent("key.advancements"));
                    component.addExtra("]");
                    new ToastNotification(Material.PUFFERFISH_BUCKET, new JSONMessage(component), AdvancementDisplay.AdvancementFrame.CHALLENGE).send(player);
                }
            }.runTaskLater(main, 40);
        }
        else {
            event.joinMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("connection").replace("<player.name>", player.getName())));
            new ToastNotification(Material.AXOLOTL_BUCKET, "Welcome back " + player.getName(), AdvancementDisplay.AdvancementFrame.CHALLENGE).send(player);
        }

        playerProfilDB.setOnline(true);
        playerProfilDB.setConnectionTime(new Date());
        playerProfilLocal.setMoney(playerProfilDB.getMoney());
        playerProfilLocal.setIslandInviteUUID("null");

        if (!playerProfilDB.getPlayedServerVersion().contains(main.getServerVersion())) playerProfilDB.addPlayedServerVersion(main.getServerVersion());

        new BukkitRunnable() {
            @Override
            public void run() {
                main.advancementManager.addPlayer(player);
            }
        }.runTaskLater(main, 40);

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
        main.advancementManager.removePlayer(player);

    }

}
