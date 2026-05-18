package domain;

import java.awt.Point;

public class Acelerate extends ColliderEnemy implements AutomaticMovement {

	
	public Acelerate(Enemy enemy, CollisionChecker cCheker, CollisionContext context) {
		this.enemy = enemy;
		
		this.cCheker = cCheker;
		this.context = context;
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
		enemy.setSpeed(enemy.getSpeed()*2);
	}


	public void move() { 
		int nextX = enemy.getPosX();
	    int nextY = enemy.getPosY();
	   
		if (!cCheker.canMove(enemy, nextX, nextY, context)) {
			if (enemy.getDirection() == 'r' || enemy.getDirection() == 'l') {
				enemy.setDirection((enemy.getDirection() == 'r') ? 'l' : 'r');
			}
			if (enemy.getDirection() == 'u' || enemy.getDirection() == 'd') {
				enemy.setDirection((enemy.getDirection() == 'u') ? 'd' : 'u');
				
			}
		}
		enemy.move(enemy.getDirection());
	}
}
