package domain;

import java.io.Serializable;

public class Bomb extends DinamicObject implements Serializable {

	
	public Bomb(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}
	

	@Override
	public String getPathImage() {
		return "/superobject/"+getNameClass()+".png";
	}

	@Override
	public String getNameClass() {
		return getClass().getSimpleName().toLowerCase();
	}

	@Override
	public int getWidth() {
		return 32;
	}

	@Override
	public int getHeight() {
		return 32;
	}
	
	@Override
	public void onContact(Player player, Level level) {
		player.destroy();
		level.removeElement(this);
	}

}
