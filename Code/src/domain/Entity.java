package domain;

public abstract class Entity extends Element {

	protected float speed;
	protected StateEntity state;
	
	
	public String getNameClass() {
		return this.getClass().getSimpleName().toLowerCase();
	}
	
	public String getNameState() {
		return state.getClass().getSimpleName().toLowerCase();
	}
	
	public String getPathImage() {
		return "/"+getNameClass()+"/"+getNameState()+".png";		
	}
	
}
