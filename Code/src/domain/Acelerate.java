package domain;

import java.io.Serializable;

public class Acelerate extends ColliderEnemy implements AutomaticMovement, Serializable {

	@SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
	public Acelerate(Enemy enemy) {
		this.enemy = enemy;
		enemy.setSpeed(enemy.getSpeed()*2);
		setInitialDirection();
	}

	@Override
	public void move(CollisionChecker checker, Level level) {
		float nextX = enemy.getPosX();
		float nextY = enemy.getPosY();
	   
		if (!checker.canMove(enemy, nextX, nextY, level)) {
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
