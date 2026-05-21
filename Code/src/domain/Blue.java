package domain;

import java.io.Serializable;

public class Blue extends PlayerState implements Serializable {

	public Blue(Player py) {
		super(py);
	}

	@Override
	public float getSpeed() {
		return py.getBaseSpeed();
	}

	@Override
	public int getWidth() {
		return 34;
	}

	@Override
	public int getHeight() {
		return 34;
	}

	@Override
	public void onEnemyContact() {
		py.setState(new DeadState(py));
	}

}
