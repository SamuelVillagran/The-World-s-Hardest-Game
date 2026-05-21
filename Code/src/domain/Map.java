package domain;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Map implements Serializable {

	private int[][] mapTileNum; //Datos para presentación.
	private Figure polygon;
	private List<Tile> tiles;
	private TileFactory tileFactory; //Clase creadora de Tiles.
	
	public Map(int level) {
		this(level, new DefaultTileFactory());
	}
	
	public Map(int level ,TileFactory factory) {
		mapTileNum = new int[DimensionGame.MAXWORLDROW][DimensionGame.MAXWORLDCOL];
		tiles = new ArrayList<>();
		this.tileFactory = factory;
		loadMap(level);
	}
	
	public String getPathLevel(int currentLevel) {
		return "/level/level"+currentLevel+".txt";
	}

	public int[][] loadMap(int currentLevel) {
		try {
			String filePathMap = getPathLevel(currentLevel);
			InputStream is = getClass().getResourceAsStream(filePathMap);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			
			while (col < DimensionGame.MAXWORLDCOL && row < DimensionGame.MAXWORLDROW) {
				String line = br.readLine();
				
				while (col < DimensionGame.MAXWORLDCOL) {
					String numbers[] = line.trim().split("\\s+");
				
					int num = Integer.parseInt(numbers[col]);
					
					mapTileNum[row][col] = num;
					
					//Calcula coordenadas en pixeles
					int posX = col * DimensionGame.TILESIZE;
                    int posY = row * DimensionGame.TILESIZE;
                    
                    //Instancia el tipo de tile correspondiente
                    Tile tile = tileFactory.createTile(num, posX, posY);
                    if(tile != null) {
                    	tiles.add(tile);
                    }
					col++;
				}
				if (col == DimensionGame.MAXWORLDCOL) {
					col = 0;
					row++;
				}
			}
			br.close();
		
		} catch (Exception e){
			e.printStackTrace();
		}
		return mapTileNum;
	}
	
	public List<Tile> getTiles(){
		return tiles;
	}
	public int[][] getMapTileNum() {
		return mapTileNum;
	}
	
}
