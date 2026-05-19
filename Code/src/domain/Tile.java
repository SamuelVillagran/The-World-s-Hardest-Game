package domain;

import java.io.Serializable;

public abstract class Tile extends Element implements Serializable {

	public String getNameSuperClass() {
		return this.getClass().getSuperclass().getSimpleName().toLowerCase();
	}
	
	@Override
	public int getWidth() {
		return 36;
	}

	@Override
	public int getHeight() {
		return 36;
	}

}
