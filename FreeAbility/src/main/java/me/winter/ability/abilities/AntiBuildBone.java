package me.winter.ability.abilities;

import jdk.nashorn.internal.ir.Block;
import me.winter.ability.AbilityPlugin;
import me.winter.ability.manager.AbilityProvider;
import me.winter.ability.utils.CC;
import me.winter.ability.utils.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author Winter (winter@frozen.gg)
 * AbilityPlugin / me.winter.ability.abilities
 *
 * Shit code
 */
public class AntiBuildBone extends AbilityProvider {

    HashMap<UUID, Integer> exoticbone = new HashMap<UUID, Integer>();
    HashMap<UUID, UUID> otherplayer = new HashMap<UUID, UUID>();
    int a;

    @Override
    public String getName() {
        return "AntiBuildBone";
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
        return new ItemStack(Material.BONE);
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
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();
        Cooldown cooldown = AbilityPlugin.getInstance().getCooldownManager().getAntiBuildbone();

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
        try {
            a = exoticbone.get(damager.getUniqueId());
            if (otherplayer.get(damager.getUniqueId()) != damaged.getUniqueId()) {
                exoticbone.remove(damager.getUniqueId());
                otherplayer.remove(damager.getUniqueId());
                for (String s : AbilityPlugin.getInstance().getAbilitiesConfig().getStringList(getName() + ".Messages.Damager.WrongPlayer")) {
                    damager.sendMessage(s);
                }
                return;
            }
        } catch (NullPointerException e) {
            exoticbone.put(damager.getUniqueId(), 0);
            otherplayer.put(damager.getUniqueId(), damaged.getUniqueId());
            Bukkit.getScheduler().scheduleSyncDelayedTask(AbilityPlugin.getPlugin(AbilityPlugin.class), () -> {
                if (!exoticbone.containsKey(damager.getUniqueId())) return;
                exoticbone.remove(damager.getUniqueId());
                for (String s : AbilityPlugin.getInstance().getAbilitiesConfig().getStringList(getName() + ".Messages.Damager.HitsReset")) {
                    damager.sendMessage(s);
                }
            }, 30 * 20);
            return;
        }
        if (a == 0) {
            exoticbone.put(damager.getUniqueId(), a + 1);
            return;
        }
        if (a == 1) {
            if (damager.getItemInHand().getAmount() == 1) {
                damager.setItemInHand(null);
            } else {
                damager.getItemInHand().setAmount(damager.getItemInHand().getAmount() - 1);
            }
        }
        cooldown.apply(damager, getCooldown());
        AbilityPlugin.getInstance().getCooldownManager().getAntibuilddamaged().apply(damaged, 15);
        exoticbone.remove(damager.getUniqueId());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            return;
        }
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }
        Player player = event.getPlayer();
        if (!AbilityPlugin.getInstance().getCooldownManager().getAntibuilddamaged().hasCooldown(player)) {
            return;
        }
        event.setCancelled(true);
        for (String s : AbilityPlugin.getInstance().getAbilitiesConfig().getStringList(getName() + ".Messages.Timer")) {
            player.sendMessage(CC.translate(s));
        }

    }

}
