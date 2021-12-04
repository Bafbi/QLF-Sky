package fr.bafbi.qsky.advancements;

import eu.endercentral.crazy_advancements.NameKey;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import fr.bafbi.qsky.Qsky;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class QskyAdvancementsManager {

    private AdvancementManager manager;

    public QskyAdvancementsManager(AdvancementManager manager) {
        this.manager = manager;
    }

    public void initPage() {}

    public void registerAdvancement(TutoAdvancements pattern) {

        Advancement parent = null;

        if (pattern.getParentAdvancement() != null) {
            Bukkit.getLogger().info(pattern.getKey());
            parent = manager.getAdvancement(new NameKey(pattern.getParentAdvancement().getNameSpace(), pattern.getParentAdvancement().getKey()));
        }

        AdvancementDisplay display = new AdvancementDisplay(pattern.getIcon(), pattern.getTitle(), pattern.getDescription(), pattern.getFrame(), pattern.getVisibility());
        if (parent != null) {
            display.setPositionOrigin(parent);
            display.setCoordinates(pattern.getX(), pattern.getY());
        }
        else {
            display.setBackgroundTexture("textures/block/yellow_concrete.png");
        }
        Advancement advancement = new Advancement(parent, new NameKey(pattern.getNameSpace(), pattern.getKey()), display);
        manager.addAdvancement(advancement);

    }

}
