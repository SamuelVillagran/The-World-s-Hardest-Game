package domain;

import java.awt.Point;
import java.util.List;

public class Basic extends ColliderEnemy implements AutomaticMovement {

	
	public Basic(Enemy enemy, CollisionChecker cCheker, CollisionContext context) {
		this.enemy = enemy;
		
		this.cCheker = cCheker;
		this.context = context;
		Point point1 = enemy.getMovement().get(0);
		Point point2 = enemy.getMovement().get(1);
		int px1 = (int) point1.getX();
		int py1 = (int) point1.getY();
		int px2 = (int) point2.getX();
		int py2 = (int) point2.getY();
		
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
		enemy.setSpeed(5);
	}

	public void move() {
		int speed = (int) enemy.getSpeed();
		char direction = enemy.getDirection();
		int nextX = enemy.getPosX();
	    int nextY = enemy.getPosY();
	    
	    switch (direction) {
		case 'r' -> nextX += speed;
		case 'l' -> nextX -= speed;
		case 'd' -> nextY += speed;
		case 'u' -> nextY -= speed;
	    }
		if (cCheker.canMove(enemy, nextX, nextY, context)) {
			enemy.move(direction);
		} else {
			if (direction == 'r') enemy.setDirection('l');
			else if (direction == 'l') enemy.setDirection('r');
			else if (direction == 'd') enemy.setDirection('u');
			else if (direction == 'u') enemy.setDirection('d');
		}
	}
}
