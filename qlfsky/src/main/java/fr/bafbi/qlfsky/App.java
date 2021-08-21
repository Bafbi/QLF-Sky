package fr.bafbi.qlfsky;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.bafbi.qlfsky.commands.Farm;
import fr.bafbi.qlfsky.commands.Money;
import fr.bafbi.qlfsky.commands.Profil;
import fr.bafbi.qlfsky.configfile.GuiConfig;
import fr.bafbi.qlfsky.listeners.JoinLeaveEvent;
import fr.bafbi.qlfsky.utils.VoidChunkGenerator;

public class App extends JavaPlugin {

    private static MongoCollection<Document> islandCol;
    private static MongoCollection<Document> playerCol;
    private static App plugin;
    public GuiConfig guiConfig;

    @Override
    public void onEnable() {

        plugin = this;
        this.guiConfig = new GuiConfig(this);

        //region Save Files
        this.saveDefaultConfig();

        /*File file = new File(this.getDataFolder(), "customItem.json");
        try {
            customItem = (JSONObject) new JSONParser().parse(new FileReader(file));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //endregion

        //region MongoDB
        ConnectionString connectionString = new ConnectionString(this.getConfig().getString("mongoDB.connectionString"));
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("plugintest");
        App.islandCol = database.getCollection("island");
        App.playerCol = database.getCollection("player");
        //endregion

        //region Listeners
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new JoinLeaveEvent(this), this);
        //endregion

        //region Tab Commands

            //region /profil <pseudo>

            PluginCommand profilCommand = this.getCommand("profil");
            profilCommand.setExecutor(new Profil(this));
            profilCommand.setTabCompleter(new Profil(this));

            //endregion
            //region /menu

            PluginCommand farmCommand = this.getCommand("farm");
            farmCommand.setExecutor(new Farm(this));
            farmCommand.setTabCompleter(new Farm(this));

            //endregion

            //region /money

            PluginCommand moneyCommand = this.getCommand("money");
            moneyCommand.setExecutor(new Money(this));
            moneyCommand.setTabCompleter(new Money(this));

            //endregion

        //endregion

        //region Generate void World

        if (Bukkit.getWorld("sky") == null) {

            WorldCreator wc = new WorldCreator("sky");
            wc.generator(new VoidChunkGenerator());
            wc.createWorld();

        }

        //#endregion


        this.getLogger().info("All clear for " + this.getName());
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Not cool");
        super.onDisable();
    }

    public static MongoCollection<Document> getPlayerCol() {
        return playerCol;
    }

    public static MongoCollection<Document> getIslandCol() {
        return islandCol;
    }

    public double getServerVersion() {
        return this.getConfig().getDouble("version", -1);
    }

    public static App getPlugin() {
        return plugin;
    }

}
