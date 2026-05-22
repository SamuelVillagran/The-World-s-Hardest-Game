package domain;

import java.io.Serializable;

public class Blue extends PlayerState implements Serializable {

	public Blue(Player py) {
		super(py);
	}

	@Override
	public float getSpeed() {
		return py.getBaseSpeed()*1.5f;
	}

	@Override
	public float getWidth() {
		return 45.0f;
	}

	@Override
	public float getHeight() {
		return 45.0f;
	}

	@Override
	public void onEnemyContact() {
		py.setState(new DeadState(py));
	}

}
