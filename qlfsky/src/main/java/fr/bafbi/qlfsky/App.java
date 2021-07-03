package fr.bafbi.qlfsky;

import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getLogger().info("banane");
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.getLogger().info("banane");
        super.onDisable();
    }
}
