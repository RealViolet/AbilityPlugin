package me.winter.ability.abilities;

import me.winter.ability.AbilityPlugin;
import me.winter.ability.manager.AbilityProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SmelterShovel extends AbilityProvider {
    @Override
    public String getName() {
        return "SmelterShovel";
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
        return 0;
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(Material.DIAMOND_SPADE);
    }

    @Override
    public boolean isEnabled() {
        return AbilityPlugin.getInstance().getAbilitiesConfig().getBoolean(getName() + ".Enabled");
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getBlock().getType().equals(Material.SAND)) {
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.GLASS));

        }
    }

}
