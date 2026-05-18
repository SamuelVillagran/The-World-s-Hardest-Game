package domain;

public class DeadState extends PlayerState {

	public DeadState(Player py) {
		super(py);
	}

	@Override
	public float getSpeed() {
		return 0;
	}

	@Override
	public int getWidth() {
		return 18;
	}

	@Override
	public int getHeight() {
		return 18;
	}

	@Override
	public void onEnemyContact() {
	}
	
	@Override
	public boolean isDead() {
		return true;
	}

}
