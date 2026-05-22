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
	public float getWidth() {
		return 18;
	}

	@Override
	public float getHeight() {
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
