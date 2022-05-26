package net.helydev.com.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Config
{
    private File file;
    private String name;
    private String directory;
    private YamlConfiguration configuration;

    public Config(JavaPlugin plugin, String name, String directory) {
        this.setName(name);
        this.setDirectory(directory);
        this.file = new File(directory, name + ".yml");
        if (!this.file.exists()) {
            plugin.saveResource(name + ".yml", false);
        }
        this.configuration = YamlConfiguration.loadConfiguration(this.getFile());
    }

    public void save() {
        try {
            this.configuration.save(this.file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        this.configuration = YamlConfiguration.loadConfiguration(this.getFile());
    }

    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }


    public YamlConfiguration getConfiguration() {
        return this.configuration;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void setConfiguration(YamlConfiguration configuration) {
        this.configuration = configuration;
    }
}
