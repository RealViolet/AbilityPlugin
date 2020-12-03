package me.winter.ability.abilities;

import me.winter.ability.AbilityPlugin;
import me.winter.ability.manager.AbilityProvider;
import me.winter.ability.utils.CC;
import me.winter.ability.utils.Cooldown;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Debuff extends AbilityProvider {

    @Override
    public String getName() {
        return "Debuff";
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
        return new ItemStack(Material.RAW_FISH, 1, (short) 3);
    }

    @Override
    public boolean isEnabled() {
        return AbilityPlugin.getInstance().getAbilitiesConfig().getBoolean(getName() + ".Enabled");
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();
        if (!isItem(damager.getItemInHand())) {
            return;
        }
        Cooldown cooldown = AbilityPlugin.getInstance().getCooldownManager().getDebuff();
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
        damaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON
                , AbilityPlugin.getInstance().getAbilitiesConfig().getInt(getName() + ".Time") * 20
                , 0));
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
        if (!isItem(event.getItem())) {
            return;
        }
        event.setCancelled(true);
    }

}
