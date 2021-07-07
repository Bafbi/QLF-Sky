package fr.bafbi.qlfsky.configfile;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.bafbi.qlfsky.App;

public class GuiConfig {

    private App main;
    private FileConfiguration guiConfig = null;
    private File guiFile = null;

    public GuiConfig(App main) {
        this.main = main;

        saveDefaultConfig();
    }

    public Void reloadConfig() {
        
        if (this.guiFile == null) this.guiFile = new File(this.main.getDataFolder(), "gui.yml");

        this.guiConfig = YamlConfiguration.loadConfiguration(this.guiFile);

        InputStream defaultStream = this.main.getResource("gui.yml");
        if (defaultStream != null) {

            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.guiConfig.setDefaults(defaultConfig);
        }
        return null;
    }

    public FileConfiguration getConfig() {

        if (this.guiConfig == null) reloadConfig();
        return this.guiConfig;

    }

    /*public void saveConfig() {
        
        if (this.guiConfig == null || this.guiFile == null) return;

        try {
            this.getConfig().save(this.guiFile);
        } catch (IOException e) {
            this.main.getLogger().log(Level.SEVERE, "Could not save GuiConfig to " + this.guiFile, e);
        }
    }*/

    public void saveDefaultConfig() {
        
        if (this.guiFile == null) this.guiFile = new File(this.main.getDataFolder(), "gui.yml");

        if (this.guiFile.exists()) {
            this.main.saveResource("gui.yml", true);
        }
    }
}
