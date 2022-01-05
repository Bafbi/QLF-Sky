package fr.bafbi.qsky.advancements;

import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.advancement.AdvancementVisibility;
import org.bukkit.Material;

public interface CustomAdvancements {

    public Material getIcon();

    public String getTitle();

    public String getDescription();

    public AdvancementDisplay.AdvancementFrame getFrame();

    public AdvancementVisibility getVisibility();

    public String getNameSpace();

    public String getKey();

    public TutoAdvancements getParentAdvancement();

    public float getX();

    public float getY();

}
