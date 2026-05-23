package domain;

import java.io.Serializable;

public abstract class Element implements Serializable {

	protected float posX;
	protected float posY;
	
	public abstract String getPathImage();
	public abstract String getNameClass();
	
	
	public float getPosX() {
		return posX;
	}
	public float getPosY() {
		return posY;
	}
	
	public float getWidth() {
		return 32.0f;
	}
	
	public float getHeight() {
		return 32.0f;
	}
}
