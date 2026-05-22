package domain;

import java.io.Serializable;

public class LifeSource extends DinamicObject implements Serializable {

	public LifeSource(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
	
	@Override
	public String getPathImage() {
		
		return super.getNameSuperClass()+"/"+super.getClassName()+"/"+this.getClassName()+".png";
	}

	@Override
	public String getNameClass() {
		return getClass().getSimpleName();
	}


	@Override
	public float getWidth() {
		return 25.0f;
	}


	@Override
	public float getHeight() {
		return 25.0f;
	}


	@Override
	public void onContact(Player player, Level level) {
		player.addLife();
		level.removeElement(this);
	}

	
	
}
