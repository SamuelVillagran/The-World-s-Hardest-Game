package domain;

import java.io.Serializable;

public class Wall extends Tile  implements Solid, Serializable {

	public Wall() {
		posX = 150;
		posY = 150;
	}
	
	public Wall(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
	public String getNameClass() {
		return this.getClass().getSimpleName().toLowerCase();
	}
	
	public String getPathImage() {
		return "/"+getNameSuperClass()+"/"+getNameClass()+".png";
	}
}
