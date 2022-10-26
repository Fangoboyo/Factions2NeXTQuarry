package net.nextbattle.quarry.support;

import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class CoreProtect {

    private CoreProtectAPI getCoreProtect() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CoreProtect");
        if (plugin == null || !(plugin instanceof CoreProtect)) {
            return null;
        }
        if (Double.parseDouble(plugin.getDescription().getVersion()) < 1.6) {
            return null;
        }
        CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getCoreProtect();
        if (CoreProtect.isEnabled() == false) {
            return null;
        }
        return CoreProtect;
    }

    public void logRemoval(String username, Location loc, int type, byte data) {
        getCoreProtect().logRemoval(username, loc, type, data);
    }
    
    public void logPlacement(String username, Location loc, int type, byte data) {
        getCoreProtect().logPlacement(username, loc, type, data);
    }
}
