package domain;

import java.io.Serializable;

public class DeadState extends PlayerState implements Serializable {

	public DeadState(Player py) {
		super(py);
		py.addDeaths();
	}

	@Override
	public float getSpeed() {
		return 0;
	}

	@Override
	public int getWidth() {
		return 18;
	}

	@Override
	public int getHeight() {
		return 18;
	}

	@Override
	public void onEnemyContact() {
	}
	
	@Override
	public boolean isDead() {
		return true;
	}

}
