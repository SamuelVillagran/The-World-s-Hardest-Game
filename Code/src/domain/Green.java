package domain;

import java.io.Serializable;

public class Green extends PlayerState implements Serializable {
	private double timeToChange = 0;

	public Green(Player py) {
		super(py);
		py.addLife();
	}

	
	@Override
	public float getSizeMultiplier() {
		return 1.0f;
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

	@Override
	public float getSpeedMultiplier() {
		return 1.0f;
	}
}
