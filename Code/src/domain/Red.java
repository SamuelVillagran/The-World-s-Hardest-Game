package domain;

import java.io.Serializable;

public class Red extends PlayerState implements Serializable {
	
	public Red(Player py) {
		super(py);
	}

	@Override
	public float getSizeMultiplier() {
		return 1.0f;
	}
	

	@Override
	public float getSpeedMultiplier() {
		return 1.0f;
	}


	@Override
	public void onEnemyContact() {
		py.setState(new DeadState(py));
		
	}
	
}
