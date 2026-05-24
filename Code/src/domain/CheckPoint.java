package domain;

import java.io.Serializable;

public class CheckPoint extends Zone implements Serializable {
	private boolean isActive = true;
	
	public CheckPoint(Figure figure) {
		super(figure);
	}

	@Override
	public void whenPlayerEnter(Player player) {
		if(isActive && contains(player.getPosX(), player.getPosY())) {
			player.setRespawnPoint(getRespawnX(), getRespawnY());
			isActive = false;
		}
	}
	
	private int getRespawnX() {
		return figure.getXCenter();
	}
	
	private int getRespawnY() {
		return figure.getYCenter();
	}

}
