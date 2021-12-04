package fr.bafbi.qsky.advancements;

import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.advancement.AdvancementVisibility;
import org.bukkit.Material;

public enum TutoAdvancements {

    TESTROOT (Material.ARMOR_STAND, "My Custom Advancements", "With cool additions", AdvancementDisplay.AdvancementFrame.TASK, AdvancementVisibility.ALWAYS, "tuto", "root", null, 0, 0),
    TESTCHILD (Material.CYAN_BED, "My Custom child Advancements", "With cool nice additions", AdvancementDisplay.AdvancementFrame.CHALLENGE, AdvancementVisibility.PARENT_GRANTED, "tuto", "child", TutoAdvancements.TESTROOT, 1, 0);


    private Material icon;
    private String title;
    private String description;
    private AdvancementDisplay.AdvancementFrame frame;
    private AdvancementVisibility visibility;
    private String nameSpace;
    private String key;
    private TutoAdvancements parentAdvancement;
    private float x;
    private float y;

    TutoAdvancements(Material icon, String title, String description, AdvancementDisplay.AdvancementFrame frame, AdvancementVisibility visibility, String nameSpace, String key, TutoAdvancements parentAdvancement, float x, float y) {
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.frame = frame;
        this.visibility = visibility;
        this.nameSpace = nameSpace;
        this.key = key;
        this.parentAdvancement = parentAdvancement;
        this.x = x;
        this.y = y;
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

    public AdvancementVisibility getVisibility() {
        return visibility;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public String getKey() {
        return key;
    }

    public TutoAdvancements getParentAdvancement() {
        return parentAdvancement;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
