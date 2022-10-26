package net.nextbattle.quarry.support;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Factions {
    public boolean mayEditBlock(Block b, String player) {
        Player p = Bukkit.getServer().getPlayer(player);
        if (p != null) {
            FPlayer fp = FPlayers.i.get(p);
            Faction faction = Board.getFactionAt(new FLocation(b.getLocation()));
            if (faction.isNone()) {
                return true;
            }
            if (faction.equals(fp.getFaction()))
            {
                return true;
            }
        }
        return false;
    }
}
