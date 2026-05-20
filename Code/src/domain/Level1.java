package domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Level1 extends Level {
	
	public Level1(CollisionChecker cChecker) {
	    map = new Map(1);
	    registerTiles();        // 1. Las baldosas toman los índices bajos (0, 1, 2...)
	    this.cChecker = cChecker; // 2. Asignas el checker antes de crear los enemigos
	    initialize();           // 3. Creas los enemigos al final
	}


	@Override
	public void initialize() { // Ayudado a poner por Gemini IA 2026, supervisado 
	    // Enemigo 1: Fila 8, empieza a la izquierda (col 9) y va a la derecha (col 25)
	    List<Point> movementEnemy1 = new LinkedList<>();
	    addPointToList(8, 7, movementEnemy1);  // Inicio (Izquierda)
	    addPointToList(8, 24, movementEnemy1); // Destino (Derecha)
	    putEnemy(movementEnemy1, "basic");

	    // Enemigo 2: Fila 9, empieza a la derecha (col 25) y va a la izquierda (col 9)
	    List<Point> movementEnemy2 = new LinkedList<>();
	    addPointToList(9, 24, movementEnemy2); // Inicio (Derecha)
	    addPointToList(9, 7, movementEnemy2);  // Destino (Izquierda)
	    putEnemy(movementEnemy2, "basic");

	    // Enemigo 3: Fila 10, empieza a la izquierda (col 9) y va a la derecha (col 25)
	    List<Point> movementEnemy3 = new LinkedList<>();
	    addPointToList(10, 7, movementEnemy3);  // Inicio (Izquierda)
	    addPointToList(10, 24, movementEnemy3); // Destino (Derecha)
	    putEnemy(movementEnemy3, "basic");

	    // Enemigo 4: Fila 11, empieza a la derecha (col 25) y va a la izquierda (col 9)
	    List<Point> movementEnemy4 = new LinkedList<>();
	    addPointToList(11, 24, movementEnemy4); // Inicio (Derecha)
	    addPointToList(11, 7, movementEnemy4);  // Destino (Izquierda)
	    putEnemy(movementEnemy4, "basic");

	    List<Point> zoneInitial = new ArrayList<>();
		addPointToList(6, 2, zoneInitial);
		addPointToList(6, 5, zoneInitial);
		addPointToList(13, 2, zoneInitial);
		addPointToList(13, 5, zoneInitial);
		putZone((ArrayList<Point>) zoneInitial, "initial");
		
		List<Point> zoneGoal = new ArrayList<>();
			addPointToList(6, 26, zoneInitial);
			addPointToList(6, 29, zoneInitial);
			addPointToList(13, 26, zoneInitial);
			addPointToList(13, 29, zoneInitial);
			putZone((ArrayList<Point>) zoneInitial, "goal");
	}
	
	@Override
	public boolean isCompleted() { //verifica que al menos un jugador llegó a la meta con monedas.
		for (Player player : players) {
	        if (player.hasGoalCompleted() && player.getCollectedCoins() >= numCoin) {
	            return true;
	        }
	    }
	    return false;
	}


	@Override
	public int getLevelTime() {
		return 90;
	}
	
	
}
