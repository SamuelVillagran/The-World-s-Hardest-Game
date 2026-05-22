package domain;

import java.io.Serializable;

public class Coin extends SuperObject implements Serializable {

	public Coin() {
		posX = 150;
		posY = 150;
	}
	
	public Coin(int x, int y) {
		posX = x;
		posY = y;
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
	public float getWidth() {
		return 15.0f;
	}

	@Override
	public float getHeight() {
		return 15.0f;
	}


	@Override
	public void onContactWithPlayer(Player player, Level level) {
		player.addCoin();
		level.removeElement(this);
	}

	@Override
	public void onContactWithEnemy(Enemy enemy, Level level) {
		
	}
}
