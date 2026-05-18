package domain;

public abstract class Entity extends Element {

	protected float speed;
	
	
	public String getNameClass() {
		return this.getClass().getSimpleName().toLowerCase();
	}
	
}
