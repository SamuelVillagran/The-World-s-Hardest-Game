package domain;

import java.io.Serializable;

public class SlowedState extends PlayerState implements Serializable {
	private double timeToChange = 0;

	public SlowedState(Player py) {
		super(py);
	}

	@Override
	public float getSpeed() {
		return  py.getBaseSpeed() * 0.7f;
	}

	@Override
	public float getWidth() {
		return 30.0f;
	}

	@Override
	public float getHeight() {
		return 30.0f;
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
