package domain;

public class DimensionGame {
	
	public static final int MAXWORLDCOL = 30;
	public static final int MAXWORLDROW = 18;
	
	public static final int TILESIZE = 36;
	public static final int TILESIZEWIDTH = 36;
	public static final int TILESIZEHEIGHT = 36;
	
	
	public static final int PLAYERSIZEWIDTH = 30;
	public static final int PLAYERSIZEHEIGHT = 30;
	
	public static final int SCREENWIDTH = TILESIZE*MAXWORLDCOL;
	public static final int SCREENHEIGHT = TILESIZE*MAXWORLDROW;
	
	public static int getMaxWorldCol() {
	    return MAXWORLDCOL;
	}

	public static int getMaxWorldRow() {
	    return MAXWORLDROW;
	}

	public static int getTileSize() {
	    return TILESIZE;
	}

	public static int getTileSizeWidth() {
	    return TILESIZEWIDTH;
	}

	public static int getTileSizeHeight() {
	    return TILESIZEHEIGHT;
	}

	public static int getPlayerSizeWidth() {
	    return PLAYERSIZEWIDTH;
	}

	public static int getPlayerSizeHeight() {
	    return PLAYERSIZEHEIGHT;
	}

	public static int getScreenWidth() {
	    return SCREENWIDTH;
	}

	public static int getScreenHeight() {
	    return SCREENHEIGHT;
	}

}
