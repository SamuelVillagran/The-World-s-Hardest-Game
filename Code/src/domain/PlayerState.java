package domain;

import java.io.Serializable;

public abstract class PlayerState implements StateEntity, Serializable {
	protected Player py;
	
	public PlayerState(Player player) {
		py = player;
	}
	
	public abstract float getSpeed();
	public abstract float getWidth();
	public abstract float getHeight();
	public abstract void onEnemyContact();
	
	public boolean isDead(){ //Solo lo sobre escribirá DeadState
		return false;
	}
}
