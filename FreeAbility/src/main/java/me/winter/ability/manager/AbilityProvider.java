package me.winter.ability.manager;


import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;
/**
 * @author Winter (winter@frozen.gg)
 * AbilityPlugin / me.winter.ability`A
 */
public abstract class AbilityProvider implements Listener {

    public abstract String getName();

    public abstract String getDisplayName();

    public abstract List<String> getLore();

    public abstract long getCooldown();

    public abstract ItemStack getItemStack();

    public abstract boolean isEnabled();



    public boolean isItem(ItemStack itemStack) {
        return itemStack.getType().equals(getItemStack().getType()) &&
                itemStack.hasItemMeta() &&
                itemStack.getItemMeta().getLore().equals(getLore()) &&
                itemStack.getItemMeta().hasDisplayName() && itemStack.getData().equals(getItemStack().getData());
    }


}
