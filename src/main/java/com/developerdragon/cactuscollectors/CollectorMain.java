package com.developerdragon.cactuscollectors;

import com.developerdragon.cactuscollectors.commands.GiveCollectorCommand;
import com.developerdragon.cactuscollectors.database.IDataHandler;
import com.developerdragon.cactuscollectors.database.SQLHandler;
import com.developerdragon.cactuscollectors.handler.CactusCollectorHandler;
import com.developerdragon.cactuscollectors.listeners.BlockGrowListener;
import com.developerdragon.cactuscollectors.listeners.InteractListener;
import com.developerdragon.cactuscollectors.listeners.InventoryListener;
import com.developerdragon.cactuscollectors.listeners.PlaceListener;
import com.developerdragon.cactuscollectors.sql.SQL;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class CollectorMain extends JavaPlugin {


    private static CollectorMain instance;
    private static Economy econ = null;
    public SQL sql;
    public IDataHandler databaseHandler;
    public CactusCollectorHandler collectorHandler;

    public static CollectorMain getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        loadConfig();
        if(getConfig().getBoolean("mysql.enabled")){
            databaseHandler = new SQLHandler();
        }else {
            // Create SQLite Handler
        }

        databaseHandler.load();
        collectorHandler = new CactusCollectorHandler();
        getServer().getPluginManager().registerEvents(new BlockGrowListener(), this);
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new PlaceListener(), this);
        getCommand("givecollector").setExecutor(new GiveCollectorCommand());

        if (!setupEconomy()) {
            getServer().getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

    }

    @Override
    public void onDisable() {
        instance = null;
        collectorHandler.stop();
    }


    public void loadConfig() {
        saveDefaultConfig();
        reloadConfig();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEcon() {
        return econ;
    }

}
