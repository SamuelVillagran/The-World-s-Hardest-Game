package domain;

import java.awt.Point;
import java.io.Serializable;

public abstract class ColliderEnemy implements Serializable {

	protected Enemy enemy;
	
	protected void setInitialDirection() {
		int dx2 = 0, dy2 = 0;
		Point point1 = enemy.getMovement().get(0);
		Point point2 = enemy.getMovement().get(1);
		int px1 = (int) point1.getX();
		int py1 = (int) point1.getY();
		int px2 = (int) point2.getX();
		int py2 = (int) point2.getY();
		dx2 = (enemy.getPosX() - px2); 
		dy2 = (enemy.getPosY() - py2); // Las condicionales y código de para abajo fue perfeccionado con Gemini IA 2026
		// 1. Si las Y son iguales, el camino es horizontal (Izquierda a Derecha)
		if (py1 == py2) {
		    if (px1 < px2) {
		        enemy.setDirection('r'); // Va a la derecha
		    } else {
		    	enemy.setDirection('l'); // Va a la izquierda
		    }
		}

		// 2. Si las X son iguales, el camino es vertical (Arriba a Abajo)
		if (px1 == px2) {
		    if (py1 < py2) {
		    	enemy.setDirection('d'); // Va hacia abajo
		    } else {
		    	enemy.setDirection('u'); // Va hacia arriba
		    }
		}
	}
}
