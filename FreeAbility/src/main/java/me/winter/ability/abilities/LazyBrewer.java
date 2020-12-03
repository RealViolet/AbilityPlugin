package me.winter.ability.abilities;

import me.winter.ability.AbilityPlugin;
import me.winter.ability.manager.AbilityProvider;
import me.winter.ability.utils.CC;
import me.winter.ability.utils.Cooldown;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.List;
/**
 * @author Winter (winter@frozen.gg)
 * AbilityPlugin / me.winter.ability.abilities
 *
 * I'm aware this is not good code, Please do not hate, you are free to change it as you please
 *
 */
public class LazyBrewer extends AbilityProvider {

    @Override
    public String getName() {
        return "LazyBrewer";
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
        return new ItemStack(Material.BREWING_STAND_ITEM);
    }

    @Override
    public boolean isEnabled() {
        return AbilityPlugin.getInstance().getAbilitiesConfig().getBoolean(getName() + ".Enabled");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!isItem(player.getItemInHand())) {
            return;
        }
        if (!isEnabled()) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&cThis item is currently disabled. If you think this is a bug, contact the server manager."));
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        Cooldown cooldown = AbilityPlugin.getInstance().getCooldownManager().getLazybrewer();
        if (cooldown.hasCooldown(player)) {
            player.sendMessage(CC.translate("&cYou are still on cooldown for " + cooldown.get(player) + " seconds."));
            event.setCancelled(true);
            return;
        }
        ItemStack potion = new ItemStack(Material.POTION);
        Potion splash = new Potion(1);
        splash.setType(PotionType.INSTANT_HEAL);
        splash.setLevel(2);
        splash.setSplash(true);
        splash.apply(potion);


        Block block = event.getBlock();
        Block block2 = event.getBlock().getLocation().add(0, 1, 0).getBlock();
        Block block3 = event.getBlock().getLocation().add(0, 2, 0).getBlock();

        Block block4 = event.getBlock().getLocation().subtract(-1, 0, 0).getBlock();
        Block block5 = event.getBlock().getLocation().subtract(-1, 0, 0).add(0, 1, 0).getBlock();
        Block block6 = event.getBlock().getLocation().subtract(-1, 0, 0).add(0, 2, 0).getBlock();
        for (int x = -(1); x <= 2; x++) {
            for (int y = -(0); y <= 2; y++) {
                for (int z = -(1); z <= 1; z++) {
                    Location loc = event.getBlock().getRelative(x, y, z).getLocation();
                    if (loc.getBlock().getType() != Material.AIR) {
                        for (String s : AbilityPlugin.getInstance().getAbilitiesConfig().getStringList(getName() + ".Messages.Error")) {
                            player.sendMessage(CC.translate(s));
                        }
                        return;
                    }
                }
            }
        }
        block.setType(Material.CHEST);
        block2.setType(Material.CHEST);
        block3.setType(Material.CHEST);
        block4.setType(Material.CHEST);
        block5.setType(Material.CHEST);
        block6.setType(Material.CHEST);

        Chest chest = (Chest) block.getState();
        Chest chest1 = (Chest) block2.getState();
        Chest chest2 = (Chest) block3.getState();
        Chest chest3 = (Chest) block4.getState();
        Chest chest4 = (Chest) block5.getState();
        Chest chest5 = (Chest) block6.getState();
        for (int i = 0; i < 54; i++) {
            chest.getInventory().addItem(potion);
            chest1.getInventory().addItem(potion);
            chest2.getInventory().addItem(potion);
            chest3.getInventory().addItem(potion);
            chest4.getInventory().addItem(potion);
            chest5.getInventory().addItem(potion);
        }
        for (String s : AbilityPlugin.getInstance().getAbilitiesConfig().getStringList(getName() + ".Messages.Placed")) {
            player.sendMessage(CC.translate(s));
        }
    }
}
