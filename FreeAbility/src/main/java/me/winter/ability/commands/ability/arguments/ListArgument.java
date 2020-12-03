package me.winter.ability.commands.ability.arguments;

import me.winter.ability.AbilityPlugin;
import me.winter.ability.manager.AbilityProvider;
import me.winter.ability.utils.CC;
import me.winter.ability.utils.command.CommandInfo;
import me.winter.ability.utils.command.argument.CommandArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
/**
 * @author Winter (winter@frozen.gg)
 * AbilityPlugin / me.winter.ability.commands.ability.arguments
 */
@CommandInfo(names = "list" , description = "List of all abilities", permission = "ability.command.list", usage = "list")
public class ListArgument extends CommandArgument {

    private AbilityPlugin instance;

    public ListArgument(AbilityPlugin instance) {
        this.instance = instance;
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return;
        }
        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.translate(""));
        sender.sendMessage(CC.translate("&b&lAbilities"));
        sender.sendMessage(CC.translate(""));
        sender.sendMessage(CC.translate("&b&lItems&f:"));
        for (AbilityProvider abilityProvider : instance.getAbilityManager().getAbilityProviders()) {
            if (abilityProvider.isEnabled()) {
                sender.sendMessage(CC.translate("  &7(&aEnabled&7) &f" + abilityProvider.getName()));
            } else {
                sender.sendMessage(CC.translate("  &7(&cDisabled&7) &f" + abilityProvider.getName()));
            }
        }
        sender.sendMessage(CC.translate(""));
        sender.sendMessage(CC.CHAT_BAR);

    }
}
