package domain;

public abstract class PlayerState implements StateEntity {
	protected Player py;
	
	public PlayerState(Player player) {
		py = player;
	}
	
	public abstract float getSpeed();
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract void onEnemyContact();
	
	public boolean isDead(){ //Solo lo sobre escribirá DeadState
		return false;
	}
}
