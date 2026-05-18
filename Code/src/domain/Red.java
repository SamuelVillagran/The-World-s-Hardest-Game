package domain;

public class Red extends PlayerState {
	
	public Red(Player py) {
		super(py);
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
		py.setState(new DeadState(py));
		
	}
	
}
