package fr.bafbi.qsky.advancements;

import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.advancement.AdvancementVisibility;
import org.bukkit.Material;

public class AdvancementsPages {

    private Material icon;
    private String title;
    private String description;
    private AdvancementDisplay.AdvancementFrame frame;
    private boolean showToast;
    private boolean announceChat;
    private AdvancementVisibility visibility;
    private String nameSpace;
    private String key;

    AdvancementsPages(Material icon, String title, String description, AdvancementDisplay.AdvancementFrame frame, boolean showToast, boolean announceChat, AdvancementVisibility visibility, String nameSpace, String key) {
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.frame = frame;
        this.showToast = showToast;
        this.announceChat = announceChat;
        this.visibility = visibility;
        this.nameSpace = nameSpace;
        this.key = key;
    }

    public Material getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public AdvancementDisplay.AdvancementFrame getFrame() {
        return frame;
    }

    public boolean isShowToast() {
        return showToast;
    }

    public boolean isAnnounceChat() {
        return announceChat;
    }

    public AdvancementVisibility getVisibility() {
        return visibility;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public String getKey() {
        return key;
    }

    enum Tuto {

        //TESTROOT (Material.ARMOR_STAND, "My Custom Advancements", "With cool additions", AdvancementDisplay.AdvancementFrame.TASK, false, false, AdvancementVisibility.HIDDEN, "tuto", "root");

    }

}
