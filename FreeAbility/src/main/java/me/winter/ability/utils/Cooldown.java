package me.winter.ability.utils;

import lombok.Getter;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Winter (winter@frozen.gg)
 * AbilityPlugin / me.winter.ability.utils
 */
public class Cooldown {


    @Getter
    private Map<Player, Long> cooldowns = new HashMap<>();


    public void apply(Player player, long cooldown) {
        cooldowns.put(player, System.currentTimeMillis() + (cooldown * 1000));
    }

    public boolean hasCooldown(Player player) {
        return cooldowns.containsKey(player) && cooldowns.get(player) >= System.currentTimeMillis();
    }

    public String get(Player player) {
        long cooldown = cooldowns.get(player) - System.currentTimeMillis();
        return DurationFormatUtils.formatDuration(cooldown, (cooldown > TimeUnit.SECONDS.toMillis(60) ? "m:ss" : "s.S"));
    }

    public void remove(Player player) {
        cooldowns.remove(player);
    }

}
