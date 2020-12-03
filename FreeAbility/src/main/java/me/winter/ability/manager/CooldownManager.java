package me.winter.ability.manager;

import lombok.Getter;
import me.winter.ability.utils.Cooldown;

/**
 * @author Winter
 * AbilityPlugin / me.winter.ability.manager
 */
public class CooldownManager {

    @Getter
    private final Cooldown snowport = new Cooldown();

    @Getter
    private final Cooldown lazybrewer = new Cooldown();

    @Getter
    private final Cooldown antiBuildbone = new Cooldown();

    @Getter
    private final Cooldown antibuilddamaged = new Cooldown();

    @Getter
    private final Cooldown switchstick = new Cooldown();

    @Getter
    private final Cooldown freezeGun = new Cooldown();

    @Getter
    private final Cooldown powerup = new Cooldown();

    @Getter
    private final Cooldown strength = new Cooldown();

    @Getter
    private final Cooldown resistance = new Cooldown();

    @Getter
    private final Cooldown debuff = new Cooldown();

    @Getter
    private final Cooldown backstab = new Cooldown();

}
