package me.winter.ability.manager;

import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.Getter;
import me.winter.ability.AbilityPlugin;
import me.winter.ability.abilities.*;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Winter (winter@frozen.gg)
 * AbilityPlugin / me.winter.ability.manager
 */
public class AbilityManager {

    @Getter
    private final List<AbilityProvider> abilityProviders = new ArrayList<>();

    private final AbilityPlugin instance;

    public AbilityManager(AbilityPlugin instance) {
        this.instance = instance;
        abilityProviders.add(new Snowport());
        abilityProviders.add(new AntiBuildBone());
        abilityProviders.add(new LazyBrewer());
        abilityProviders.add(new SwitchStick());
        abilityProviders.add(new FreezeGun());
        abilityProviders.add(new Debuff());
        abilityProviders.add(new PowerUp(instance));
        abilityProviders.add(new Resistance());
        abilityProviders.add(new Backstab());
        abilityProviders.add(new Strength());
        abilityProviders.add(new SmelterShovel());
        for (AbilityProvider abilityProvider : abilityProviders) {
            Bukkit.getPluginManager().registerEvents(abilityProvider, instance);
        }

    }

    public AbilityProvider getByName(String name) {
        return this.abilityProviders.stream().filter(abilityProvider -> abilityProvider.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
