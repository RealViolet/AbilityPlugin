package me.winter.ability.abilities;

import me.winter.ability.AbilityPlugin;
import me.winter.ability.manager.AbilityProvider;
import me.winter.ability.utils.CC;
import me.winter.ability.utils.Cooldown;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
/**
 * @author Winter (winter@frozen.gg)
 * AbilityPlugin / me.winter.ability.abilities
 */
public class Snowport extends AbilityProvider {
    @Override
    public String getName() {
        return "Snowport";
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
        return new ItemStack(Material.SNOW_BALL);
    }

    @Override
    public boolean isEnabled() {
        return AbilityPlugin.getInstance().getAbilitiesConfig().getBoolean(getName() + ".Enabled");
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Snowball)) {
            return;
        }
        Snowball snowball = (Snowball) event.getEntity();
        if (!(snowball.getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player) snowball.getShooter();
        Cooldown cooldown = AbilityPlugin.getInstance().getCooldownManager().getSnowport();
        if (!isItem(player.getItemInHand())) {
            return;
        }
        if (!isEnabled()) {
            player.sendMessage(CC.translate("&cThis item is currently disabled. If you think this is a bug, contact the server manager."));
            return;
        }
        if (cooldown.hasCooldown(player)) {
            player.sendMessage(CC.translate("&cYou are still on cooldown for " + cooldown.get(player) + " seconds."));
            event.setCancelled(true);
            return;
        }
        snowball.setMetadata("snowport", new FixedMetadataValue(AbilityPlugin.getInstance(), player.getUniqueId()));
        cooldown.apply(player, getCooldown());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.isCancelled())) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!(event.getDamager() instanceof Snowball)) {
            return;
        }
        Snowball snowball = (Snowball) event.getDamager();
        if (!snowball.hasMetadata("snowport")) {
            return;
        }
        Player damager = (Player) snowball.getShooter();
        Player damaged = (Player) event.getEntity();
        Location location = damager.getLocation();
        if (damaged.getLocation().distance(location) > AbilityPlugin.getInstance().getAbilitiesConfig().getInt(getName() + ".Range")) {
            for (String s : AbilityPlugin.getInstance().getAbilitiesConfig().getStringList(getName() + ".Messages.Range")) {
                damager.sendMessage(CC.translate(s));
            }
            return;
        }
        damager.teleport(damaged);
        damaged.teleport(location);
        for (String s : AbilityPlugin.getInstance().getAbilitiesConfig().getStringList(getName() + ".Messages.Damager")) {
            damager.sendMessage(CC.translate(s).replace("%player%", damaged.getName()));
        }
        for (String s : AbilityPlugin.getInstance().getAbilitiesConfig().getStringList(getName() + ".Messages.Damaged")) {
            damager.sendMessage(CC.translate(s).replace("%player%", damager.getName()));
        }
    }
}
