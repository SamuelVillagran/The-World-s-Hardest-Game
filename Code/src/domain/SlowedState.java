package domain;

public class SlowedState extends PlayerState{
	private double timeToChange = 0;

	public SlowedState(Player py) {
		super(py);
	}

	@Override
	public float getSpeed() {
		return  py.getBaseSpeed() * 0.7f;
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
			py.setState(new DeadState(py));
		}
	}
}
