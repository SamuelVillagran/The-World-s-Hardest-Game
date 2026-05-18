package domain;

import java.awt.Point;

public class Vertical extends EnemyCollision implements AutomaticMovement {

	
	
	public Vertical(Enemy enemy, CollisionChecker cCheker, CollisionContext context) {
		this.enemy = enemy;
		
		this.cCheker = cCheker;
		this.context = context;
		Point point1 = enemy.getMovement().get(0);
		Point point2 = enemy.getMovement().get(1);
		int px1 = (int) point1.getX();
		int py1 = (int) point1.getY();
		int px2 = (int) point2.getX();
		int py2 = (int) point2.getY();
		
		if (py1 < py2) {
		   	enemy.setDirection('d'); // Va hacia abajo
		} else {
		  	enemy.setDirection('u'); // Va hacia arriba
		}
		enemy.setSpeed(3);
	}
	
	@Override
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
		enemy.move(enemy.getDirection());
		}
	}
}
