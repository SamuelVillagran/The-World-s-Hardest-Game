package domain;

import java.io.Serializable;

/**
 * Abstract creator
 * 
 * Define the factory method createTile where concrete subclasses
 * have to implements to instance the type of tile according to
 * to the name read of the map
 * Helped to make with GPT 5.5
 * */
public abstract class TileFactory implements Serializable {
	/**
	 * Factory method, creates and initialize the corresponding Tile to the
	 * cell located in the column and in the corresponding row.
	 * @param tileNum number read from the map file.
	 * @param posX position X in pixels.
	 * @param posY position Y in pixels.
	 * @return a concrete instance of the corresponding tile
	 * 	null if the number us unknown.
	 */
	public abstract Tile createTile(int tileNum, int posX, int posY);

}
