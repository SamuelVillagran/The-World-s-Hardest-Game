package domain;

import java.io.Serializable;

public abstract class Entity extends Element implements Serializable {

	protected float width;
	protected float height;
	protected float speed;
	
	
	public String getNameClass() {
		return this.getClass().getSimpleName().toLowerCase();
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
}
