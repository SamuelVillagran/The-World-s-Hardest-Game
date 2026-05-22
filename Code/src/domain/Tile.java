package domain;

import java.io.Serializable;

public abstract class Tile extends Element implements Serializable {

	public String getNameSuperClass() {
		return this.getClass().getSuperclass().getSimpleName().toLowerCase();
	}
	
	@Override
	public float getWidth() {
		return 36.0f;
	}

	@Override
	public float getHeight() {
		return 36.0f;
	}

}
