package ch.dlyn.mouse;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

public class EntityHideUtil {
	
	public static HashMap<Player, ArrayList<Player>> seenMap = new HashMap<>();
	
	public final static void hideEntity(Player observer, Player p, ProtocolManager manager) {
        PacketContainer destroyEntity = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        destroyEntity.getIntegerArrays().write(0, new int[] { p.getEntityId() });
        
        if (seenMap.containsKey(p)) {
        	if (!seenMap.get(p).contains(observer)) {
        		seenMap.get(p).add(observer);
        	}
        } else {
        	ArrayList<Player> t = new ArrayList<>();
        	t.add(observer);
        	seenMap.put(p, t);
        }
        
        try {
            manager.sendServerPacket(observer, destroyEntity);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot send server packet.", e);
        }
    }
	
	public final static void showEntity(Player observer, Player p, ProtocolManager manager) {
		
		if (seenMap.containsKey(p)) {
			if (seenMap.get(p).contains(observer)) {
				seenMap.get(p).remove(observer);
			} else {
				return;
			}
		} else {
			return;
		}
		
        if (manager != null) {
            manager.updateEntity(p, Arrays.asList(observer));
        }
    }
	
	// CREDIT: trevorzucker@github
	// canSee() function is modified from his NameHide plugin, which implements this functionality differently. 
	
	static String[] blacklist = new String[] {
	        "GRASS",
	        "BAMBOO",
	        "FLOWER",
	        "MUSHROOM",
	        "DEAD_BUSH",
	        "SUGAR_CANE",
	        "VINE",
	        "NETHER_WART",
	        "STEM",
	        "SAPLING",
	        "BARRIER",
	        "BEETROOT",
	        "BANNER",
	        "CARPET",
	        "BRAIN_CORAL",
	        "CHORUS_FLOWER",
	        "COMMAND_BLOCK ",
	        "MOVING_PISTON",
	        "PISTON_HEAD",
	        "FIRE",
	        "SNOW",
	        "ICE",
	        "PORTAL",
	        "CAKE_BLOCK",
	        "ENDER_PORTAL",
	        "ROSE",
	        "SUNFLOWER",
	        "ORCHID",
	        "CAMPFIRE",
	        "DANDELION",
	        "RAIL",
	        "KELP",
	        "CRYSTAL",
	        "GATEWAY",
	        "PORTAL",
	        "END_ROD",
	        "FERN",
	        "GLASS_PANE",
	        "PRESSURE_PLATE",
	        "IRON_BARS",
	        "TRAPDOOR",
	        "LADDER",
	        "LILAC",
	        "LILY",
	        "TULIP",
	        "BLUET",
	        "POPPY",
	        "POTTED",
	        "ALLIUM",
	        "REDSTONE",
	        "SCAFFOLDING",
	        "BERRY",
	        "BERRIES",
	        "TORCH",
	        "HEAD",
	        "WALL",
	        "DOOR",
	        "AIR",
	        "SLAB",
	        "FENCE",
	        "LEAF",
	        "LEAVES"
	    };
	
	public static boolean canSee(Player p, Player other, boolean sprinting) {
		Location dest = other.getEyeLocation();
		
        Location eye = p.getEyeLocation();
        Vector eyeVec = eye.clone().toVector();
        Vector direction = eyeVec.subtract(dest.toVector()).normalize();
        double dist = eye.distance(dest);
        if (sprinting) return true;
        if (Mouse.notSneakingPlayers.contains(other.getUniqueId())) {
        	return true;
        }
        for(int i = 0; i < dist; i++) {
            Location eyeTemp = eye.clone();
            Vector dirTemp = direction.clone();
            dirTemp.multiply(i);
            dirTemp.multiply(-1);
            eyeTemp.add(dirTemp);
            Block at = eyeTemp.getBlock();
            if (at != null) {
                boolean found = false;
                for(String s : blacklist)
                if (at.getType().toString().contains(s) && !at.getType().toString().contains("BLOCK")) {
                    found = true;
                    break;
                }
                if (!found)
                    return false;
            }
        }
        
        return true;
    }
}
