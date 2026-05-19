package domain;

import java.io.Serializable;

public class HumanPlayer extends Player implements Serializable {

	public HumanPlayer(PlayerType type, String name) throws HardestGameException{
		super(type, name);
	}
	
	@Override
	public String getNameClass() {
		return "player";
	}
}
