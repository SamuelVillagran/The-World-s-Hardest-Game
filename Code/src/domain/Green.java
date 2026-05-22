package domain;

import java.io.Serializable;

public class Green extends PlayerState implements Serializable {
	private double timeToChange = 0;

	public Green(Player py) {
		super(py);
		py.addLife();
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
		timeToChange += 1.0 / 60.0;
		if(timeToChange >= 0.18) {
			py.substractLife();
			if (py.getLifes() <= 0) {
				py.setState(new DeadState(py));
			} else {
				py.setState(new SlowedState(py));
			}
		}
	}

}
