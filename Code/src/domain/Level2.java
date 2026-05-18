package domain;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

public class Level2 extends Level {

	public Level2(CollisionChecker cCheker) {
		numCoin = 3;
		map = new Map(2);
		registerTiles();        // 1. Las baldosas toman los índices bajos (0, 1, 2...)
		this.cCheker = cCheker; // 2. Asignas el checker antes de crear los enemigos
		initialize();           // 3. Creas los enemigos al final
	}

	@Override
	public void initialize() {
		int NUM_ENEMIES = 18, START_ROW_DOWN = 4, START_ROW_UP = 13, START_COL = 6;
		
		for (int i = 0; i < NUM_ENEMIES; i++) {
			if (i%2==0) {
				List<Point> movementEnemy = new LinkedList<>();
			    addPointToList(START_ROW_DOWN, START_COL+i, movementEnemy);  // Inicio (Izquierda)
			    addPointToList(START_ROW_UP, START_COL+i, movementEnemy); // Destino (Derecha)
			    putEnemy(movementEnemy, "vertical");
			}
			if (i%2==1) {
				List<Point> movementEnemy = new LinkedList<>();
			    addPointToList(START_ROW_UP, START_COL+i, movementEnemy);  // Inicio (Izquierda)
			    addPointToList(START_ROW_DOWN, START_COL+i, movementEnemy); // Destino (Derecha)
			    putEnemy(movementEnemy, "vertical");
			}
		}
		putCoin(4, 6);
		putCoin(13, 6);
		putCoin(4, 23);
		putCoin(13, 23);
	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void spawnPlayers(List<Player> pys) {
		// TODO Auto-generated method stub
		
	}
	
}
