package me.winter.ability.commands.ability.arguments;

import me.winter.ability.AbilityPlugin;
import me.winter.ability.manager.AbilityProvider;
import me.winter.ability.utils.CC;
import me.winter.ability.utils.command.CommandCompleter;
import me.winter.ability.utils.command.CommandInfo;
import me.winter.ability.utils.command.argument.CommandArgument;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@CommandInfo(
        names = {"give"},
        permission = "ability.command.give",
        description = "Gives an ability to a player",
        usage = "give <player> <ability> <amount>"
)
public class GiveArgument extends CommandArgument implements CommandCompleter {

    private AbilityPlugin instance;

    public GiveArgument(AbilityPlugin instance) {
        this.instance = instance;
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 4) {
            sender.sendMessage(CC.createUsage(label, "give <player> [ability] [amount]"));
            return;
        }
        AbilityProvider abilityProvider = AbilityPlugin.getInstance().getAbilityManager().getByName(args[2]);
        if (abilityProvider == null) {
            sender.sendMessage(CC.translate("&cThat ability does not exist."));
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }
        try {
            ItemStack itemStack = new ItemStack(abilityProvider.getItemStack());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(CC.translate(abilityProvider.getLore()));
            itemStack.setAmount(Integer.parseInt(args[3]));
            itemMeta.setDisplayName(CC.translate(abilityProvider.getDisplayName()));
            itemStack.setItemMeta(itemMeta);
            for (ItemStack value : target.getInventory().addItem(itemStack).values()) {
                target.getWorld().dropItemNaturally(target.getLocation(), value);
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate("&cInvalid Amount"));
        }
        sender.sendMessage(CC.translate("&cGiven " + target.getName() + " a " + abilityProvider.getName()));
    }


    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> abilities = new ArrayList<>();
        if (args.length == 3) {
            for (AbilityProvider ability : AbilityPlugin.getInstance().getAbilityManager().getAbilityProviders()) {
                abilities.add(ability.getName());
            }
        }
        return abilities;
    }
}
