package domain;

import java.io.Serializable;

public class Red extends PlayerState implements Serializable {
	
	public Red(Player py) {
		super(py);
	}

	@Override
	public float getSpeed() {
		return py.getBaseSpeed();
	}

	@Override
	public float getWidth() {
		return 30;
	}

	@Override
	public float getHeight() {
		return 30;
	}

	@Override
	public void onEnemyContact() {
		py.setState(new DeadState(py));
		
	}
	
}
