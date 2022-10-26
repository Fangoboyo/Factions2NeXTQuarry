package net.nextbattle.quarry.functions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.server.v1_5_R3.ChunkCoordIntPair;
import net.minecraft.server.v1_5_R3.EntityPlayer;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_5_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class WorldFunctions {

    public static BlockFace getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return BlockFace.NORTH;
        } else if (22.5 <= rotation && rotation < 67.5) {
            return BlockFace.NORTH;
        } else if (67.5 <= rotation && rotation < 112.5) {
            return BlockFace.EAST;
        } else if (112.5 <= rotation && rotation < 157.5) {
            return BlockFace.EAST;
        } else if (157.5 <= rotation && rotation < 202.5) {
            return BlockFace.SOUTH;
        } else if (202.5 <= rotation && rotation < 247.5) {
            return BlockFace.SOUTH;
        } else if (247.5 <= rotation && rotation < 292.5) {
            return BlockFace.WEST;
        } else if (292.5 <= rotation && rotation < 337.5) {
            return BlockFace.WEST;
        } else if (337.5 <= rotation && rotation < 360.0) {
            return BlockFace.NORTH;
        } else {
            return null;
        }
    }
    
    public static ArrayList<Chunk> chunkqueue = new ArrayList<>();

    public static void queueBlock(Block b, int typeId, byte data) {
        Chunk c = b.getChunk();
        net.minecraft.server.v1_5_R3.Chunk chunk = ((CraftChunk) c).getHandle();
        chunk.a(b.getX() & 15, b.getY(), b.getZ() & 15, typeId, data);
        if (!chunkqueue.contains(c)) {
            chunkqueue.add(c);
        }
    }

    public static void processQueue() {
        if (chunkqueue.size() == 0) { return; }
        try {
        for (Chunk c : chunkqueue) {
            ((CraftChunk) c).getHandle().initLighting();
        }
        List<World> worlds = new ArrayList<>();
        for (Chunk c : chunkqueue) {
            if (!worlds.contains(c.getWorld())) {
                worlds.add(c.getWorld());
            }
        }
        for (World world : worlds) {
            int highestx = 0;
            int lowestx = 0;
            int highestz = 0;
            int lowestz = 0;
            int i = 0;
            List<ChunkCoordIntPair> pairs = new ArrayList<ChunkCoordIntPair>();

            for (Chunk c : chunkqueue) {
                if (c.getWorld().equals(world)) {
                    pairs.add(new ChunkCoordIntPair(c.getX(), c.getZ()));
                    if (i == 0) {
                        highestx = c.getX();
                        lowestx = c.getX();
                        highestz = c.getZ();
                        lowestz = c.getZ();
                    } else {
                        if (c.getX() > highestx) {
                            highestx = c.getX();
                        }
                        if (c.getX() < lowestx) {
                            lowestx = c.getX();
                        }
                        if (c.getZ() > highestz) {
                            highestz = c.getZ();
                        }
                        if (c.getZ() < lowestz) {
                            lowestz = c.getZ();
                        }
                    }
                    i++;
                }
            }
            for (Player player : world.getPlayers()) {
                int px = player.getLocation().getBlock().getChunk().getX();
                int pz = player.getLocation().getBlock().getChunk().getZ();
                if (px >= (lowestx - 1) && px <= (highestx + 1) && pz >= (lowestz - 1) && pz <= (highestz + 1)) {
                    EntityPlayer ep = ((CraftPlayer) player).getHandle();
                    Set<ChunkCoordIntPair> queued = new HashSet<ChunkCoordIntPair>();
                    for (Object o : ep.chunkCoordIntPairQueue) {
                        queued.add((ChunkCoordIntPair) o);
                    }
                    for (ChunkCoordIntPair pair : pairs) {
                        if (!queued.contains(pair)) {
                            ep.chunkCoordIntPairQueue.add(pair);
                        }
                    }
                }
            }
        }
        } catch (Exception e) {}
    }
}
