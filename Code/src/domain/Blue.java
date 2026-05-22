package domain;

import java.io.Serializable;

public class Blue extends PlayerState implements Serializable {

	public Blue(Player py) {
		super(py);
	}

	@Override
	public float getSizeMultiplier() {
		return 1.5f;
	}

	@Override
	public void onEnemyContact() {
		py.setState(new DeadState(py));
	}

	@Override
	public float getSpeedMultiplier() {
		return 1.5f;
	}

}
