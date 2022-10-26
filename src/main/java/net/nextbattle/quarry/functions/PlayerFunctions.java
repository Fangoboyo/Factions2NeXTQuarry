package net.nextbattle.quarry.functions;

import java.util.HashMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerFunctions {
    
    public static boolean addItems(Inventory inv, ItemStack is) {
        if (is == null) { return true; }
        HashMap<Integer, ItemStack> didnotfit;
        didnotfit = inv.addItem(is);
        if (didnotfit.isEmpty()) { return true; }
        for (ItemStack is2 : didnotfit.values()) {
            if (is2.equals(is)) {
                return false;
            }            
        }
        return true;
    }
}
