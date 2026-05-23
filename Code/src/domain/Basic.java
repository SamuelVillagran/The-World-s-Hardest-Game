package domain;

import java.awt.Point;
import java.io.Serializable;
import java.util.List;

public class Basic extends ColliderEnemy implements AutomaticMovement, Serializable {

	@SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
	public Basic(Enemy enemy) {
		this.enemy = enemy;
		setInitialDirection();
		enemy.setSpeed(5);
	}

	@Override
	public void move(CollisionChecker checker, Level level) {
		int speed = (int) enemy.getSpeed();
		char direction = enemy.getDirection();
		float nextX = enemy.getPosX();
	    float nextY = enemy.getPosY();
	    
	    switch (direction) {
		case 'r' -> nextX += speed;
		case 'l' -> nextX -= speed;
		case 'd' -> nextY += speed;
		case 'u' -> nextY -= speed;
	    }
		if (checker.canMove(enemy, nextX, nextY, level)) {
			enemy.move(direction);
		} else {
			if (direction == 'r') enemy.setDirection('l');
			else if (direction == 'l') enemy.setDirection('r');
			else if (direction == 'd') enemy.setDirection('u');
			else if (direction == 'u') enemy.setDirection('d');
		}
	}
}
