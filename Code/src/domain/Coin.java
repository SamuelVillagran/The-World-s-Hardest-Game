package domain;

import java.io.Serializable;

public class Coin extends SuperObject implements Serializable {

	private SkinCoin skin;
	
	public Coin() {
		posX = 150;
		posY = 150;
	}
	
	public Coin(int x, int y) {
		posX = x;
		posY = y;
	}
	
	public Coin(float x, float y, SkinCoin skin) {
		posX = x;
		posY = y;
		this.skin = skin;
	}

	@Override
	public String getPathImage() {
		return "/"+ super.getName()+"/"+getNameClass()+".png";
	}

	@Override
	public String getNameClass() {
		if (skin == null) return  this.getClass().getSimpleName().toLowerCase();
		return this.getClass().getSimpleName().toLowerCase()+skin.getNameSkin();
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
		if (skin != null) {
			skin.onContactWithPlayer(player, level);
		}
	}

	@Override
	public void onContactWithEnemy(Enemy enemy, Level level) {
		
	}
	
	public void setSkin(SkinCoin skin) {
		this.skin = skin;
	}
}
