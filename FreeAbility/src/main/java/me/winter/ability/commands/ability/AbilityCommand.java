package me.winter.ability.commands.ability;

import me.winter.ability.AbilityPlugin;
import me.winter.ability.commands.ability.arguments.GiveArgument;
import me.winter.ability.commands.ability.arguments.ListArgument;
import me.winter.ability.utils.command.CommandInfo;
import me.winter.ability.utils.command.argument.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Winter (winter@frozen.gg)
 * AbilityPlugin / me.winter.ability.commands.ability
 */
@CommandInfo(names = {"ability", "abilities"}, permission = "ability.command", helpTitle = "Ability Help")
public class AbilityCommand extends CommandExecutor {



    public AbilityCommand(AbilityPlugin instance) {
        super(instance);
        this.addArgument(new ListArgument(instance));
        this.addArgument(new GiveArgument(instance));
    }

    @Override
    public boolean executeOther(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}
