package domain;

import java.io.Serializable;

public abstract class Entity extends Element implements Serializable {

	protected float speed;
	
	
	public String getNameClass() {
		return this.getClass().getSimpleName().toLowerCase();
	}
	
}
