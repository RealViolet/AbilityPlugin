package me.winter.ability.abilities;

import me.winter.ability.AbilityPlugin;
import me.winter.ability.manager.AbilityProvider;
import me.winter.ability.utils.CC;
import me.winter.ability.utils.Cooldown;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class PowerUp extends AbilityProvider {

    private final AbilityPlugin instance;

    public PowerUp(AbilityPlugin instance) {
        this.instance = instance;
    }

    @Override
    public String getName() {
        return "PowerUp";
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
        return new ItemStack(Material.REDSTONE);
    }

    @Override
    public boolean isEnabled() {
        return AbilityPlugin.getInstance().getAbilitiesConfig().getBoolean(getName() + ".Enabled");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.getAction().name().contains("RIGHT")) {
            return;
        }
        if (!isItem(player.getItemInHand())) {
            return;
        }
        Cooldown cooldown = instance.getCooldownManager().getPowerup();
        if (!isEnabled()) {
            player.sendMessage(CC.translate("&cThis item is currently disabled. If you think this is a bug, contact the server manager."));
            return;
        }
        if (cooldown.hasCooldown(player)) {
            player.sendMessage(CC.translate("&cYou are still on cooldown for " + cooldown.get(player) + " seconds."));
            event.setCancelled(true);
            return;
        }
        cooldown.apply(player, getCooldown());
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, AbilityPlugin.getInstance().getAbilitiesConfig().getInt(getName() + ".Time") * 20, 1));
        for (String s : AbilityPlugin.getInstance().getAbilitiesConfig().getStringList(getName() + ".Messages.Used")) {
            player.sendMessage(s);
        }
    }

}
