package domain;

public class LifeSource extends DinamicObject {

	public LifeSource(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
	
	@Override
	public String getPathImage() {
		
		return super.getNameSuperClass()+"/"+super.getClassName()+"/"+this.getClassName()+".png";
	}

	@Override
	public String getNameClass() {
		return getClass().getSimpleName();
	}


	@Override
	public int getWidth() {
		return 25;
	}


	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 25;
	}


	@Override
	public void onContact(Player player, Level level) {
		player.addLife();
		level.removeElement(this);
	}

	
	
}
