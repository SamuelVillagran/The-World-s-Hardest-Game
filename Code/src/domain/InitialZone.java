package domain;

import java.io.Serializable;

public class InitialZone extends Zone implements Serializable {

	public InitialZone(Figure figure) {
		super(figure);
	}

	@Override
	public void whenPlayerEnter(Player player, Level level) {
		//No hace nada
	}

}
