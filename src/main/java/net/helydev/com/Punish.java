package net.helydev.com;

import net.helydev.com.command.EditCommand;
import net.helydev.com.command.PunishCommand;
import net.helydev.com.command.ReloadComand;
import net.helydev.com.listener.MenuListener;
import net.helydev.com.listener.edit.*;
import net.helydev.com.utils.Config;
import org.bukkit.plugin.java.JavaPlugin;

public class Punish extends JavaPlugin {

    private static Punish instance;
    public Config data;

    @Override
    public void onEnable() {
        instance=this;
        saveDefaultConfig();
        reloadConfig();
        this.data = new Config(Punish.getInstance(), "data", Punish.getInstance().getDataFolder().getAbsolutePath());
        this.getCommand("punish").setExecutor(new PunishCommand());
        this.getCommand("punishreload").setExecutor(new ReloadComand());
        this.getCommand("punishedit").setExecutor(new EditCommand());
        this.getServer().getPluginManager().registerEvents(new MenuListener(), this);
        this.getServer().getPluginManager().registerEvents(new CreateListener(), this);
        this.getServer().getPluginManager().registerEvents(new EditCategoryListener(), this);
        this.getServer().getPluginManager().registerEvents(new EditItemListener(), this);
        this.getServer().getPluginManager().registerEvents(new EditListener(), this);
        this.getServer().getPluginManager().registerEvents(new OnChat(), this);
    }

    @Override
    public void onDisable() {
        instance=null;
    }

    public static Punish getInstance() {
        return Punish.instance;
    }

    public Config getDataConfig() {
        return data;
    }

    public void getReload(){
        this.reloadConfig();
        this.getDataConfig().reload();
    }

}
