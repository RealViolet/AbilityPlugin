package me.winter.ability.abilities;

import me.winter.ability.AbilityPlugin;
import me.winter.ability.manager.AbilityProvider;
import me.winter.ability.utils.CC;
import me.winter.ability.utils.Cooldown;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * @author Winter (winter@frozen.gg)
 * AbilityPlugin / me.winter.ability.abilities
 */
public class FreezeGun extends AbilityProvider {
    @Override
    public String getName() {
        return "FreezeGun";
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
        return new ItemStack(Material.DIAMOND_HOE);
    }

    @Override
    public boolean isEnabled() {
        return AbilityPlugin.getInstance().getAbilitiesConfig().getBoolean(getName() + ".Enabled");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_AIR) {
            return;
        }
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }
        if (!isItem(player.getItemInHand())) {
            return;
        }
        if (!isEnabled()) {
            player.sendMessage(CC.translate("&cThis item is currently disabled. If you think this is a bug, contact the server manager."));
            return;
        }
        Cooldown cooldown = AbilityPlugin.getInstance().getCooldownManager().getFreezeGun();
        if (cooldown.hasCooldown(player)) {
            player.sendMessage(CC.translate("&cYou are still on cooldown for " + cooldown.get(player) + " seconds."));
            event.setCancelled(true);
            return;
        }
        cooldown.apply(player, getCooldown());
        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setMetadata("freezegun", new FixedMetadataValue(AbilityPlugin.getInstance(), player.getUniqueId()));
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
        if (!snowball.hasMetadata("freezegun")) {
            return;
        }
        Player damager = (Player) snowball.getShooter();
        Player damaged = (Player) event.getEntity();

        damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
        for (String s : AbilityPlugin.getInstance().getAbilitiesConfig().getStringList(getName() + ".Messages.Damager")) {
            damager.sendMessage(s.replace("%player%", damaged.getName()));
        }
        for (String s : AbilityPlugin.getInstance().getAbilitiesConfig().getStringList(getName() + ".Messages.Damaged")) {
            damaged.sendMessage(s.replace("%player%", damager.getName()));
        }
    }


}
