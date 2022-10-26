package net.nextbattle.quarry.types;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockLocation {
    private int x;
    private int y;
    private int z;
    private World world;
    
    public BlockLocation(int x, int y, int z, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }
    
    public BlockLocation(Block b)
    {
        this.x = b.getX();
        this.y = b.getY();
        this.z = b.getZ();
        this.world = b.getWorld();
    }
    
    public BlockLocation(Location loc)
    {
        this.x = (int) loc.getX();
        this.y = (int) loc.getY();
        this.z = (int) loc.getZ();
        this.world = loc.getWorld();
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getZ() {
        return z;
    }
    
    public World getWorld() {
        return world;
    }
    
    public Location getLocation() {
        return new Location(world,x,y,z);
    }
    
    public Block getBlock() {
        return world.getBlockAt(getLocation());
    }
    
    public boolean equals(BlockLocation bl) {
        if (bl.getWorld().equals(world) && bl.getX() == x && bl.getY() == y && bl.getZ() == z)
        {
            return true;
        }
        return false;
    }
}
