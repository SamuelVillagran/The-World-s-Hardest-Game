package domain;

import java.io.Serializable;

public abstract class DinamicObject extends SuperObject implements Serializable {

	private boolean isActive;
	
	public String getNameSuperClass() {
		return this.getClass().getSuperclass().getSimpleName();
	}
	
	public String getClassName() {
		return this.getClass().getSimpleName();
	}
	
	
}
