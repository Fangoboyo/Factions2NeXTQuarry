package net.nextbattle.quarry.main;

import net.nextbattle.quarry.entities.CustomItems;
import net.nextbattle.quarry.entities.Quarry;
import net.nextbattle.quarry.functions.WorldFunctions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class GeneralEventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt) {
        if (evt.getPlayer().getName().equals("bemacized") && MainClass.config.dev_join_message && Bukkit.getServer().getOnlineMode()) {
            evt.setJoinMessage(ChatColor.AQUA + "NeXTBattle Developer " + ChatColor.GOLD + "BeMacized" + ChatColor.AQUA + " has joined the game!");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent evt) {
        try {
            if (evt.getItemInHand().equals(MainClass.citems.fuel_finder_upgrade)) {
                evt.setCancelled(true);
                return;
            }
            if (MainClass.config.world_whitelist_enabled) {
                if (!MainClass.config.world_whitelist.contains(evt.getBlock().getWorld().getName()) && !evt.getPlayer().hasPermission("nextquarry.admin") && (CustomItems.customItemsMatch(evt.getItemInHand(), MainClass.citems.quarry_tier1) || CustomItems.customItemsMatch(evt.getItemInHand(), MainClass.citems.quarry_tier2) || CustomItems.customItemsMatch(evt.getItemInHand(), MainClass.citems.quarry_tier3))) {
                    evt.setCancelled(true);
                    evt.getPlayer().sendMessage(ChatColor.RED + "You are not allowed to place quarries in this world");
                    return;
                }
            }
            if (!evt.isCancelled()) {
                if (CustomItems.customItemsMatch(evt.getItemInHand(), MainClass.citems.quarry_tier1) && evt.getPlayer().hasPermission("nextquarry.user.tier1") && Quarry.userCanPlaceTier(0, evt.getPlayer().getName())) {
                    new Quarry(WorldFunctions.getCardinalDirection(evt.getPlayer()), 0, evt.getBlock(), evt.getPlayer());
                }
                if (CustomItems.customItemsMatch(evt.getItemInHand(), MainClass.citems.quarry_tier2) && evt.getPlayer().hasPermission("nextquarry.user.tier2") && Quarry.userCanPlaceTier(1, evt.getPlayer().getName())) {
                    new Quarry(WorldFunctions.getCardinalDirection(evt.getPlayer()), 1, evt.getBlock(), evt.getPlayer());
                }
                if (CustomItems.customItemsMatch(evt.getItemInHand(), MainClass.citems.quarry_tier3) && evt.getPlayer().hasPermission("nextquarry.user.tier3") && Quarry.userCanPlaceTier(2, evt.getPlayer().getName())) {
                    new Quarry(WorldFunctions.getCardinalDirection(evt.getPlayer()), 2, evt.getBlock(), evt.getPlayer());
                }
            }
        } catch (Exception e) {
        }
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent evt) {
        Player p = evt.getPlayer();
        Quarry.saveAll();
        if (p.getItemInHand() != null) {
            if (p.getItemInHand().equals(MainClass.citems.wrench_tool) && evt.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Quarry q = Quarry.isActualQuarry(evt.getClickedBlock());
                if (q != null) {
                    if (!p.hasPermission("nextquarry.user.remove") && !p.hasPermission("nextquarry.admin")) {
                        p.sendMessage(ChatColor.DARK_RED + "[!] You do not have the permission to edit this quarry!");
                        return;
                    }
                    if (MainClass.config.privatequarries) {
                        if (!p.getName().equals(q.getPlayerName()) && !p.hasPermission("nextquarry.admin")) {
                            p.sendMessage(ChatColor.DARK_RED + "[!] You do not own this quarry!");
                            return;
                        }
                    }
                    p.openInventory(q.upgr_inv);
                }
                return;
            }
            if (p.getItemInHand().equals(MainClass.citems.fuel_tool) && evt.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Quarry q = Quarry.isActualQuarry(evt.getClickedBlock());
                if (q != null) {
                    if (!p.hasPermission("nextquarry.user.edit") && !p.hasPermission("nextquarry.admin")) {
                        p.sendMessage(ChatColor.DARK_RED + "[!] You do not have the permission to edit this quarry!");
                        return;
                    }
                    if (MainClass.config.privatequarries) {
                        if (!p.getName().equals(q.getPlayerName()) && !p.hasPermission("nextquarry.admin")) {
                            p.sendMessage(ChatColor.DARK_RED + "[!] You do not own this quarry!");
                            return;
                        }
                    }
                    p.openInventory(q.fuel_inv);
                }
                return;
            }
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent evt) {
        try {
            if (Quarry.isUpgradeBlock(evt.getBlock())) {
                evt.setCancelled(true);
                return;
            }
            if (Quarry.isActualQuarry(evt.getBlock()) != null) {
                evt.setCancelled(true);
                Quarry q = Quarry.isActualQuarry(evt.getBlock());
                Player p = evt.getPlayer();
                if (!p.hasPermission("nextquarry.user.remove") && !p.hasPermission("nextquarry.admin")) {
                    p.sendMessage(ChatColor.DARK_RED + "[!] You do not have the permission to break this quarry!");
                    return;
                }
                if (MainClass.config.privatequarries) {
                    if (!p.getName().equals(q.getPlayerName()) && !p.hasPermission("nextquarry.admin")) {
                        p.sendMessage(ChatColor.DARK_RED + "[!] You do not own this quarry!");
                        return;
                    }
                }

                ItemStack is = null;
                if (q.getTier() == 0) {
                    is = MainClass.citems.quarry_tier1;
                }
                if (q.getTier() == 1) {
                    is = MainClass.citems.quarry_tier2;
                }
                if (q.getTier() == 2) {
                    is = MainClass.citems.quarry_tier3;
                }
                evt.getBlock().getWorld().dropItemNaturally(evt.getBlock().getLocation(), is);
                for (ItemStack is2 : q.upgr_inv.getContents()) {
                    if (is2 != null) {
                        evt.getBlock().getWorld().dropItemNaturally(evt.getBlock().getLocation(), is2);
                    }
                }
                for (ItemStack is2 : q.fuel_inv.getContents()) {
                    if (is2 != null) {
                        evt.getBlock().getWorld().dropItemNaturally(evt.getBlock().getLocation(), is2);
                    }
                }
                q.delete();
                evt.getBlock().setType(Material.AIR);
            }
            if (Quarry.isInQuarriesBlock(evt.getBlock())) {
                evt.setCancelled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
