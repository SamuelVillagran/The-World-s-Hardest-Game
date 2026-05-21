package domain;

import java.io.Serializable;

public abstract class StateEnemy implements Serializable {

	protected Enemy enemy;
	
	public void setEnemy(Enemy enemy) {
		this.enemy = enemy;
	}
	
}
