package net.nextbattle.quarry.support;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.RegionGroupFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WorldGuard {

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }

    public static <ProtectedRegion> List<ProtectedRegion> copyIterator(Iterator<ProtectedRegion> iter) {
        List<ProtectedRegion> copy = new ArrayList<ProtectedRegion>();
        while (iter.hasNext()) {
            copy.add(iter.next());
        }
        return copy;
    }

    public boolean mayEditBlock(Block b, String player) {
        Player p = Bukkit.getServer().getPlayer(player);
        boolean mayedit = true;
        if (p != null) {
            mayedit = getWorldGuard().canBuild(p, b.getLocation());
        } else {
            if (!getWorldGuard().getGlobalStateManager().get(b.getWorld()).useRegions) {
                return true;
            }
            mayedit = internalGetState(DefaultFlag.BUILD, player, null, getWorldGuard().getRegionManager(b.getWorld()).getRegion("__global__"), copyIterator(getWorldGuard().getRegionManager(b.getWorld()).getApplicableRegions(b.getLocation()).iterator()));
        }
        return mayedit;
    }

    private boolean internalGetState(StateFlag flag, String player,
            LocalPlayer groupPlayer, ProtectedRegion globalRegion, List<ProtectedRegion> applicable) {
        boolean found = false;
        boolean hasFlagDefined = false;
        boolean allowed = false;
        boolean def = flag.getDefault();
        if (globalRegion != null) {
            State globalState = globalRegion.getFlag(flag);
            if (globalState != null) {
                if (player != null && globalRegion.hasMembersOrOwners()) {
                    def = globalRegion.isMember(player) && (globalState == State.ALLOW);
                } else {
                    def = (globalState == State.ALLOW);
                }
            } else {
                if (player != null && globalRegion.hasMembersOrOwners()) {
                    def = globalRegion.isMember(player);
                }
            }
        }
        if (player == null) {
            allowed = def;
        }

        int lastPriority = Integer.MIN_VALUE;
        Set<ProtectedRegion> needsClear = new HashSet<ProtectedRegion>();
        Set<ProtectedRegion> hasCleared = new HashSet<ProtectedRegion>();

        for (ProtectedRegion region : applicable) {
            if (hasFlagDefined && region.getPriority() < lastPriority) {
                break;
            }

            lastPriority = region.getPriority();
            if (player != null
                    && region.getFlag(DefaultFlag.PASSTHROUGH) == State.ALLOW) {
                continue;
            }
            if (groupPlayer != null && flag.getRegionGroupFlag() != null) {
                RegionGroup group = region.getFlag(flag.getRegionGroupFlag());
                if (group == null) {
                    group = flag.getRegionGroupFlag().getDefault();
                }
                if (!RegionGroupFlag.isMember(region, group, groupPlayer)) {
                    continue;
                }
            }

            State v = region.getFlag(flag);
            if (v == State.DENY) {
                return false;
            }
            if (v == State.ALLOW) {
                allowed = true;
                found = true;
                hasFlagDefined = true;
                continue;
            }
            if (player != null) {
                hasFlagDefined = true;

                if (hasCleared.contains(region)) {
                } else {
                    if (!region.isMember(player)) {
                        needsClear.add(region);
                    } else {
                        clearParents(needsClear, hasCleared, region);
                    }
                }
            }
            found = true;
        }

        return !found ? def
                : (allowed || (player != null && needsClear.size() == 0));
    }

    private void clearParents(Set<ProtectedRegion> needsClear,
            Set<ProtectedRegion> hasCleared, ProtectedRegion region) {
        ProtectedRegion parent = region.getParent();

        while (parent != null) {
            if (!needsClear.remove(parent)) {
                hasCleared.add(parent);
            }

            parent = parent.getParent();
        }
    }
}
