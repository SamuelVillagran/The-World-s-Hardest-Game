package domain;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Zone implements Serializable {

	protected List<Tile> tiles;
	protected Figure figure;
	
	/**
	 * This is the zone Where going to affect the player
	 * @param form form is the coordinates of tiles to make the zone
	 */
	public Zone(Figure figure) {
		this.figure = figure;
		tiles = new ArrayList<>();
	}
	
	/**
	 * Check if a point is inside.
	 * @param f horizontal position element to check.
	 * @param g vertical position element to check.
	 * @return true if is inside, false otherwise.
	 */
	public boolean contains(float f, float g) {
		return figure.contains(f, g);
	}
	
	public int getSpawnX() {
		return figure.getXCenter();
	}
	
	public int getSpawnY() {
		return figure.getYCenter();
	}
	public abstract void whenPlayerEnter(Player player);
}
