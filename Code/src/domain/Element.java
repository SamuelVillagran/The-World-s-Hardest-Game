package domain;

import java.io.Serializable;

public abstract class Element implements Serializable {

	protected int posX;
	protected int posY;
	
	public abstract String getPathImage();
	public abstract String getNameClass();
	
	
	public int getPosX() {
		return posX;
	}
	public int getPosY() {
		return posY;
	}
	
	public float getWidth() {
		return 32.0f;
	}
	
	public float getHeight() {
		return 32.0f;
	}
}
