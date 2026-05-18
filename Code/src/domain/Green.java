package domain;

public class Green extends PlayerState{
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
	public int getWidth() {
		return 30;
	}

	@Override
	public int getHeight() {
		return 30;
	}

	@Override
	public void onEnemyContact() {
		timeToChange += 1.0 / 60.0;
		if(timeToChange >= 0.18) {
			py.substractLife();
			py.setState(new SlowedState(py));
		}
	}

}
