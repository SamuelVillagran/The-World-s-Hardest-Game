package domain;

import java.awt.Point;
import java.util.List;

public abstract class Enemy extends Entity implements Interactable{

	protected List<Point> movement;
	protected char direction;
	
	public Enemy() {
		posX = 210;
		posY = 210;
		size = 0.5f;
		speed = 3.0f;
	}
	
	public Enemy(int x, int y) {
		size = 0.5f;
		posX = x; posY = y;
	}
	
	public Enemy(List<Point> movement) {
		this.movement = movement;
		Point firstPoint = movement.get(0);
		posX = (int) firstPoint.getX();
		posY = (int) firstPoint.getY();
		size = 0.5f;
		speed = 3.0f;
	}
	
	/**
	 * Makes move the player of game
	 * @param direction direction is where going to move the player
	 */
	public void move(char direction) {
		switch (direction) {
			case 'u': posY -= speed; direction = 'u';
				break;
			case 'd': posY += speed; direction = 'd';
				break;
			case 'l': posX -= speed; direction = 'l';
				break;
			case 'r': posX += speed; direction = 'r';
				break;
		}
	}
	
	public abstract void move();

	@Override
	public String getPathImage() {
		return "/enemy/blue.png";
	}

	@Override
	public int getWidth() {
		return 32;
	}

	@Override
	public int getHeight() {
		return 32;
	}

	public void setPoints(List<Point> points) {
		movement = points;
	}
	
	@Override
	public void onContact(Player player, Level level) {
		player.onEnemyContact();
		
	}

	
}
