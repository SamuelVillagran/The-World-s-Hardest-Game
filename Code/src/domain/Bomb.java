package domain;

public class Bomb extends DinamicObject {

	
	public Bomb(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
	public Bomb() {
		
	}

	@Override
	public String getPathImage() {
		return "/tile/"+getNameClass()+".png";
	}

	@Override
	public String getNameClass() {
		return getClass().getSimpleName().toLowerCase();
	}

	@Override
	public int getWidth() {
		return 15;
	}

	@Override
	public int getHeight() {
		return 15;
	}
	
	@Override
	public void onContact(Player player, Level level) {
		player.destroy();
		level.removeElement(this);
	}

}
