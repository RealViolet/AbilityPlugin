package me.winter.ability.manager;

import me.winter.ability.AbilityPlugin;
import me.winter.ability.commands.ability.AbilityCommand;
/**
 * @author Winter (winter@frozen.gg)
 * AbilityPlugin / me.winter.ability.commands
 */
public class CommandHandler {

    private final AbilityPlugin instance;

    public CommandHandler(AbilityPlugin instance) {
        this.instance = instance;
        new AbilityCommand(instance);
    }

}
