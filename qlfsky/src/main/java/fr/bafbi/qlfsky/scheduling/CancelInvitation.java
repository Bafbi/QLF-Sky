package fr.bafbi.qlfsky.scheduling;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.bafbi.qlfsky.Qsky;
import fr.bafbi.qlfsky.utils.PlayerProfilLocal;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class CancelInvitation extends BukkitRunnable {

    private Player player;
    private Player playerInvited;
    private final ConfigurationSection textComponent;


    public CancelInvitation(Player player, Player playerInvited, Qsky main) {
        this.player = player;
        this.playerInvited = playerInvited;
        this.textComponent = main.getConfig().getConfigurationSection("textComponent.command.farm");

    }

    @Override
    public void run() {

        PlayerProfilLocal playerInvitedProfilLocal = new PlayerProfilLocal(playerInvited);

        if (playerInvitedProfilLocal.getIslandInviteUUID() == "null") return;

        playerInvitedProfilLocal.setIslandInviteUUID("null");
        playerInvited.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("invite.exiredInvitation").replace("<player.name>", player.getName())));
        player.sendMessage(GsonComponentSerializer.gson().deserialize(textComponent.getString("invite.selfExiredInvitation").replace("<player.name>", playerInvited.getName())));

    }
    
}
