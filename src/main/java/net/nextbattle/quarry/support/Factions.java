package net.nextbattle.quarry.support;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Factions {
    public boolean mayEditBlock(Block b, String player) {
        Player p = Bukkit.getServer().getPlayer(player);
        if (p != null) {
            UPlayer fp = UPlayer.get(p);
            Faction faction = BoardColls.get().getFactionAt(PS.valueOf(b.getLocation()));
            if (faction.isNone()) {
                return true;
            }
            return faction.equals(fp.getFaction());
        }
        return false;
    }
}
