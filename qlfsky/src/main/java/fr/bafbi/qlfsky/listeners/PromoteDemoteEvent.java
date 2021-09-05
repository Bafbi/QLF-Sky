package fr.bafbi.qlfsky.listeners;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.bafbi.qlfsky.App;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.track.UserTrackEvent;
import net.luckperms.api.model.data.DataType;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.ChatMetaNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.track.Track;

public class PromoteDemoteEvent {

    private final App main;
    private final LuckPerms luckPerms;

    public PromoteDemoteEvent(App main, LuckPerms luckPerms) {
        this.main = main;
        this.luckPerms = luckPerms;
    }

    public void register() {
        EventBus eventBus = this.luckPerms.getEventBus();
        eventBus.subscribe(this.main, UserTrackEvent.class, this::onPromoteDemote);
    }

    private void onPromoteDemote(UserTrackEvent event) {
        // main.getLogger().info(event.getTrack().getName());
        // main.getLogger().info(event.getGroupTo().get());
        Track track = event.getTrack();
        if (!(event.getTrack().getName().equalsIgnoreCase("staff"))) return;

        String groupToName;
        try {
            groupToName = event.getGroupTo().orElseThrow();
        } catch (NoSuchElementException e) {
            //TODO: handle exception
            return;
        }
        

        
        Scoreboard mainScorboard = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
        String prefix = this.luckPerms.getGroupManager().getGroup(groupToName).getNodes(NodeType.PREFIX).stream().max(Comparator.comparing(ChatMetaNode::getPriority)).get().getMetaValue();
        // main.getLogger().info(prefix);
        Team team = mainScorboard.getTeam(groupToName);
        if (team == null) {
            team = mainScorboard.registerNewTeam(groupToName);
            team.prefix(LegacyComponentSerializer.legacyAmpersand().deserialize(prefix + "&r&7・"));
            team.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize("&" + prefix.split("&")[1]));
            // main.getLogger().info("&" + prefix.split("&")[1]);
            // main.getLogger().info(team.displayName().toString());
        }
        team.addEntry(Bukkit.getPlayer(event.getUser().getUniqueId()).getName());
    }
    
}
