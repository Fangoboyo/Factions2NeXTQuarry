package net.nextbattle.quarry.support;

import java.util.logging.Level;
import net.nextbattle.quarry.main.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class PluginSupport {

    public WorldGuard wg;
    public Factions fa;
    public CoreProtect cp;
    public LogBlock_ lb;

    public PluginSupport() {
        init();
    }

    public void logPlacement(String username, Location loc, int type, byte data) {
        if (cp != null) {
            try {
                cp.logPlacement(username, loc, type, data);
            } catch (Exception e) {
                cp = null;
                Bukkit.getServer().getLogger().log(Level.INFO, "[NeXTQuarry] An error occurred with CoreProtect. CoreProtect has been detatched from NeXTQuarry.");
            }
        }
        if (lb != null) {
            try {
                lb.logPlacement(username, loc, type, data);
            } catch (Exception e) {
                lb = null;
                Bukkit.getServer().getLogger().log(Level.INFO, "[NeXTQuarry] An error occurred with LogBlock. LogBlock has been detatched from NeXTQuarry.");
            }
        }
    }

    public void logRemoval(String username, Location loc, int type, byte data) {
        if (cp != null) {
            try {
                cp.logRemoval(username, loc, type, data);
            } catch (Exception e) {
                cp = null;
                Bukkit.getServer().getLogger().log(Level.INFO, "[NeXTQuarry] An error occurred with CoreProtect. CoreProtect has been detatched from NeXTQuarry.");
            }
        }
        if (lb != null) {
            try {
                lb.logRemoval(username, loc, type, data);
            } catch (Exception e) {
                lb = null;
                Bukkit.getServer().getLogger().log(Level.INFO, "[NeXTQuarry] An error occurred with LogBlock. LogBlock has been detatched from NeXTQuarry.");
            }
        }
    }

    public boolean mayEditBlock(Block b, String playername) {
        boolean mayedit = true;
        if (wg != null) {
            if (mayedit) {
                mayedit = wg.mayEditBlock(b, playername);
            }
        }
        if (fa != null) {
            if (mayedit) {
                mayedit = fa.mayEditBlock(b, playername);
            }
        }
        return mayedit;
    }

    private void init() {
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            wg = new WorldGuard();
            MainClass.plugin.getServer().getLogger().log(Level.INFO, "[NeXTQuarry] WorldGuard found & Attatched.");

        } else {
            wg = null;
        }
        if (Bukkit.getPluginManager().getPlugin("Factions") != null) {
            fa = new Factions();
            MainClass.plugin.getServer().getLogger().log(Level.INFO, "[NeXTQuarry] Factions found & Attatched.");

        } else {
            fa = null;
        }
        if (Bukkit.getPluginManager().getPlugin("CoreProtect") != null) {
            cp = new CoreProtect();
            MainClass.plugin.getServer().getLogger().log(Level.INFO, "[NeXTQuarry] CoreProtect found & Attatched.");

        } else {
            cp = null;
        }
        if (Bukkit.getPluginManager().getPlugin("LogBlock") != null) {
            lb = new LogBlock_();
            MainClass.plugin.getServer().getLogger().log(Level.INFO, "[NeXTQuarry] LogBlock found & Attatched.");
        } else {
            lb = null;
        }
    }
}
