package me.winter.ability.abilities;

import me.winter.ability.AbilityPlugin;
import me.winter.ability.manager.AbilityProvider;
import me.winter.ability.utils.CC;
import me.winter.ability.utils.Cooldown;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
/**
 * @author Winter (winter@frozen.gg)
 * AbilityPlugin / me.winter.ability.abilities
 */
public class SwitchStick extends AbilityProvider {
    @Override
    public String getName() {
        return "SwitchStick";
    }

    @Override
    public String getDisplayName() {
        return AbilityPlugin.getInstance().getAbilitiesConfig().getString(getName() + ".Item.Name");
    }

    @Override
    public List<String> getLore() {
        return AbilityPlugin.getInstance().getAbilitiesConfig().getStringList(getName() + ".Item.Lore");
    }

    @Override
    public long getCooldown() {
        return AbilityPlugin.getInstance().getAbilitiesConfig().getInt(getName() + ".Cooldown");
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(Material.STICK);
    }

    @Override
    public boolean isEnabled() {
        return AbilityPlugin.getInstance().getAbilitiesConfig().getBoolean(getName() + ".Enabled");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();
        Cooldown cooldown = AbilityPlugin.getInstance().getCooldownManager().getSwitchstick();
        if (!isItem(damager.getItemInHand())) {
            return;
        }
        if (!isEnabled()) {
            damager.sendMessage(CC.translate("&cThis item is currently disabled. If you think this is a bug, contact the server manager."));
            return;
        }
        if (cooldown.hasCooldown(damager)) {
            damager.sendMessage(CC.translate("&cYou are still on cooldown for " + cooldown.get(damager) + " seconds."));
            return;
        }
        cooldown.apply(damager, getCooldown());
        damaged.getLocation().setYaw(damaged.getLocation().getYaw() + 180);
        for (String s : AbilityPlugin.getInstance().getAbilitiesConfig().getStringList(getName() + ".Messages.Damager")) {
            damager.sendMessage(CC.translate(s).replace("%player%", damaged.getName()));
        }
        for (String s : AbilityPlugin.getInstance().getAbilitiesConfig().getStringList(getName() + ".Messages.Damaged")) {
            damaged.sendMessage(CC.translate(s).replace("%player%", damager.getName()));
        }
        if (damager.getItemInHand().getAmount() == 1) {
            damager.setItemInHand(null);
        } else {
            damager.getItemInHand().setAmount(damager.getItemInHand().getAmount() - 1);
        }
    }

}
