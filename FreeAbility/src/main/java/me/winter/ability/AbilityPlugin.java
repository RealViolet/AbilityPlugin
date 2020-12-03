package me.winter.ability;

import lombok.Getter;
import lombok.SneakyThrows;
import me.winter.ability.manager.CommandHandler;
import me.winter.ability.manager.AbilityManager;
import me.winter.ability.utils.Config;
import me.winter.ability.manager.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Winter (winter@frozen.gg)
 * AbilityPlugin / me.winter.ability
 */
public class AbilityPlugin extends JavaPlugin {

    @Getter
    private static AbilityPlugin instance;

    @Getter
    private AbilityManager abilityManager;

    @Getter
    private CooldownManager cooldownManager;

    @Getter
    private Config abilitiesConfig;


    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;
        abilitiesConfig = new Config(this, "abilities");
        cooldownManager = new CooldownManager();
        abilityManager = new AbilityManager(this);
        new CommandHandler(this);
    }


    @Override
    public void onDisable() {
        instance = null;
    }

}
