package domain;

import java.io.Serializable;

public abstract class PlayerState implements Serializable {
	protected Player py;
	
	public PlayerState(Player player) {
		py = player;
	}
	
	public abstract float getSizeMultiplier();
	public abstract void onEnemyContact();
	
	public boolean isDead(){ //Solo lo sobre escribirá DeadState
		return false;
	}

	public abstract float getSpeedMultiplier();

}
