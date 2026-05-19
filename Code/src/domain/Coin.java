package domain;

import java.io.Serializable;

public class Coin extends SuperObject implements Serializable {

	public Coin() {
		posX = 150;
		posY = 150;
		size = 0.5f;
	}
	
	public Coin(int x, int y) {
		posX = x;
		posY = y;
		size = 0.5f;
	}
	
	@Override
	public String getPathImage() {
		return "/"+ super.getName()+"/"+getNameClass()+".png";
	}

	@Override
	public String getNameClass() {
		return  this.getClass().getSimpleName().toLowerCase();
	}

	@Override
	public int getWidth() {
		return 15;
	}

	@Override
	public int getHeight() {
		return 15;
	}

	@Override
	public void onContact(Player player, Level level) {
		player.addCoin();
		level.removeElement(this);
	}
}
