package domain;

import java.io.Serializable;

public class DeadState extends PlayerState implements Serializable {

	public DeadState(Player py) {
		super(py);
		py.addDeaths();
	}


	public float getSizeMultiplier() {
		return 0.5f;
	}

	@Override
	public void onEnemyContact() {
	}
	
	@Override
	public boolean isDead() {
		return true;
	}


	@Override
	public float getSpeedMultiplier() {
		return 0.0f;
	}

}
