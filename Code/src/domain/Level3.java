package domain;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Level3 extends Level implements Serializable  {

	
	
	public Level3(CollisionChecker cChecker) {
		map = new Map(3);
	    registerTiles();        // 1. Las baldosas toman los índices bajos (0, 1, 2...)
	    this.cChecker = cChecker; // 2. Asignas el checker antes de crear los enemigos
	}

	@Override
	public void initialize() {

	    List<Point> movementEnemy1 = new LinkedList<>();
	    addPointToList(2, 6, movementEnemy1);  // Inicio (Izquierda)
	    addPointToList(15, 6, movementEnemy1); // Destino (Derecha)
	    putEnemy(movementEnemy1, "acelerate");

	    List<Point> movementEnemy2 = new LinkedList<>();
	    addPointToList(2, 10, movementEnemy2); // Inicio (Derecha)
	    addPointToList(15, 10, movementEnemy2);  // Destino (Izquierda)
	    putEnemy(movementEnemy2, "acelerate");

	    List<Point> movementEnemy3 = new LinkedList<>();
	    addPointToList(2, 12, movementEnemy3);  // Inicio (Izquierda)
	    addPointToList(15, 12, movementEnemy3); // Destino (Derecha)
	    putEnemy(movementEnemy3, "acelerate");

	    List<Point> movementEnemy4 = new LinkedList<>();
	    addPointToList(15, 11, movementEnemy4); // Inicio (Derecha)
	    addPointToList(3, 11, movementEnemy4);  // Destino (Izquierda)
	    putEnemy(movementEnemy4, "acelerate");
	    
	    List<Point> movementEnemy5 = new LinkedList<>();
	    addPointToList(15, 13, movementEnemy5); // Inicio (Derecha)
	    addPointToList(3, 13, movementEnemy5);  // Destino (Izquierda)
	    putEnemy(movementEnemy5, "acelerate");
	    
	    putBomb(7, 3);
	    putBomb(3, 9);
	    putBomb(14, 14);
	    putBomb(14, 16);
	    putBomb(10, 17);
	    putBomb(6, 17);
	    putBomb(5, 23);
	    putBomb(9, 26);
	    
	    List<Point> zoneInitial = new ArrayList<>();
		addPointToList(3, 2, zoneInitial);
		addPointToList(3, 4, zoneInitial);
		addPointToList(4, 4, zoneInitial);
		addPointToList(4, 2, zoneInitial);
		putZone((ArrayList<Point>) zoneInitial, "initial");
		List<Point> zoneGoal = new ArrayList<>();
		addPointToList(13, 25, zoneGoal);
		addPointToList(7, 28, zoneGoal);
		addPointToList(15, 28, zoneGoal);
		addPointToList(15, 25, zoneGoal);
		putZone((ArrayList<Point>) zoneGoal, "goal");
	}


	@Override
	public int getLevelTime() {
		return 90;
	}

}
