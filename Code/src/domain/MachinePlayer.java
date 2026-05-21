package domain;

import java.io.Serializable;

public class MachinePlayer implements Serializable {

	private MachineContext machineContext;
	private Behavior machineBehavior;
	
	public MachinePlayer(Behavior machineBehavior) {
		this.machineBehavior = machineBehavior;
	}
	
	public void updateMachineContext(MachineContext context) {
		
	}
	
}
