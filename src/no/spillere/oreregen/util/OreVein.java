package no.spillere.oreregen.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class OreVein {

	private Material type;

	private List<int[]> coords;

	public OreVein(Material type, List<int[]> coords) {
		this.type = type;
		this.coords = coords;
	}

	@Override
	public String toString() {
		return "OreVein [type=" + type + ", coords=" + coords + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coords == null) ? 0 : coords.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OreVein other = (OreVein) obj;
		if (coords == null) {
			if (other.coords != null)
				return false;
		} else if (!coords.equals(other.coords))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public Material getType() {
		return type;
	}

	public void setType(Material type) {
		this.type = type;
	}

	public List<int[]> getCoords() {
		return coords;
	}

	public void setCoords(List<int[]> coords) {
		this.coords = coords;
	}

	public List<Location> getLocations(){
		World world = Bukkit.getWorlds().get(0);
		List<Location> locations = new ArrayList<Location>();
		for (int[] coords : coords) {
			Location loc = new Location(world, coords[0], coords[1], coords[2]);
			locations.add(loc);
		}
		return locations;
	}

	public List<Block> getBlocks(){
		List<Block> blocks = new ArrayList<Block>();
		for (Location loc : getLocations()) {
			blocks.add(loc.getBlock());
		}
		return blocks;
	}

}