package me.winter.ability.utils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class Config extends YamlConfiguration {

    private final JavaPlugin plugin;
    private final String fileName;

    @SneakyThrows
    public Config(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName + ".yml";

        if (!plugin.getDataFolder().exists())
            if (plugin.getDataFolder().mkdir())
                System.out.println("The data folder didn't exist so we created it for you.");

        File file = new File(plugin.getDataFolder(), this.fileName);
        if (!file.exists()) {
            if (plugin.getResource(this.fileName) != null)
                plugin.saveResource(this.fileName, false);
            else save();

            load(file);
            save();
            return;
        }

        load(file);
    }

    @Override
    public String getString(String path) {
        try {
            if (super.getString(path).contains("&"))
                return ChatColor.translateAlternateColorCodes('&', super.getString(path));

            return super.getString(path);
        } catch (NullPointerException ex) {
            System.out.println("The path " + path + " does not exist.");
            return "Error Read Console.";
        }
    }


    @Override
    public List<String> getStringList(String path) {
        return CC.translate(super.getStringList(path));
    }


    public void save() {
        try {
            this.save(new File(plugin.getDataFolder(), fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}