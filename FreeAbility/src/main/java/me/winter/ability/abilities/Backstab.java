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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Backstab extends AbilityProvider {
    @Override
    public String getName() {
        return "Backstab";
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
        return new ItemStack(Material.WOOD_SWORD);
    }

    @Override
    public boolean isEnabled() {
        return AbilityPlugin.getInstance().getAbilitiesConfig().getBoolean(getName() + ".Enabled");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();
        if (!isItem(damager.getItemInHand())) {
            return;
        }
        Cooldown cooldown = AbilityPlugin.getInstance().getCooldownManager().getBackstab();
        if (!isEnabled()) {
            damager.sendMessage(CC.translate("&cThis item is currently disabled. If you think this is a bug, contact the server manager."));
            return;
        }
        if (cooldown.hasCooldown(damager)) {
            damager.sendMessage(CC.translate("&cYou are still on cooldown for " + cooldown.get(damager) + " seconds."));
            event.setCancelled(true);
            return;
        }
        cooldown.apply(damager, getCooldown());
        for (String s : AbilityPlugin.getInstance().getAbilitiesConfig().getStringList(getName() + ".Messages.Damager")) {
            damager.sendMessage(s.replace("%player%", damaged.getName()));
        }
        for (String s : AbilityPlugin.getInstance().getAbilitiesConfig().getStringList(getName() + ".Messages.Damaged")) {
            damaged.sendMessage(s);
        }
        if (damager.getLocation().getDirection().dot(damaged.getLocation().getDirection()) > 0) {
            damaged.damage(4);
        }
        cooldown.apply(damager, getCooldown());
    }
}
