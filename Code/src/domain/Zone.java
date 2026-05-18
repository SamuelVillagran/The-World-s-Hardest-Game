package domain;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Zone {

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
	 * @param x horizontal position element to check.
	 * @param y vertical position element to check.
	 * @return true if is inside, false otherwise.
	 */
	public boolean contains(int x, int y) {
		return figure.contains(x, y);
	}
	
	public int getSpawnX() {
		return figure.getXCenter();
	}
	
	public int getSpawnY() {
		return figure.getYCenter();
	}
	public abstract void whenPlayerEnter(Player player, Level level);
}
