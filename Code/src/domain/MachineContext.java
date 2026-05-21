package domain;

import java.io.Serializable;

public class MachineContext implements Serializable {

	private int goalXPos;
	private int goalYPos;
	
	public MachineContext(Player self, int goalXPos, int goalYPos) {
		this.goalXPos = goalXPos;
		this.goalYPos = goalYPos;
	}
	
	
}
