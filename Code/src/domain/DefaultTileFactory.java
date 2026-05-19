package domain;

import java.io.Serializable;

/**
 * Concrete creator of TileFactory class
 */
public class DefaultTileFactory extends TileFactory implements Serializable {

	@Override
	public Tile createTile(int tileNum, int posX, int posY) {
		switch(tileNum) {
		case 0:
			return new Floor(posX, posY);
		case 1:
			return new Wall(posX, posY);
		case 2: 
			return new GreenTile(posX, posY);
		default:
			return null;
		}
	}

	
}
