package domain;

public class CheckPoint extends Zone{
	private boolean isActive = true;
	
	public CheckPoint(Figure figure) {
		super(figure);
	}

	@Override
	public void whenPlayerEnter(Player player, Level level) {
		if(isActive) {
			player.setRespawnPoint(getRespawnX(), getRespawnY());
			isActive = false;
		}
	}
	
	private int getRespawnX() {
		return figure.getXCenter();
	}
	
	private int getRespawnY() {
		return figure.getYCenter();
	}

}
