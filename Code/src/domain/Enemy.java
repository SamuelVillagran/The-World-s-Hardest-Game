package domain;

import java.awt.Point;
import java.io.Serializable;
import java.util.List;

/**
 * @implNote takeDamage() added with Claude Sonnet 4.6
 */
public class Enemy extends Entity implements Interactable, AutomaticMovement, Damageable, Serializable {

	private AutomaticMovement strategyMovement;
	private List<Point> movement;
	private char direction;

	public Enemy(List<Point> movement) {
		this.movement = movement;
		Point firstPoint = movement.get(0);
		posX = (int) firstPoint.getX();
		posY = (int) firstPoint.getY();
		size = 0.5f;
		speed = 4.0f;
	}

	/**
	 * Makes move the player of game
	 * 
	 * @param direction direction is where going to move the player
	 */
	public void move(char direction) {
		switch (direction) {
			case 'u':
				posY -= speed;
				direction = 'u';
				break;
			case 'd':
				posY += speed;
				direction = 'd';
				break;
			case 'l':
				posX -= speed;
				direction = 'l';
				break;
			case 'r':
				posX += speed;
				direction = 'r';
				break;
		}
	}

	public void move() {
		strategyMovement.move();
	}

	@Override
	public String getPathImage() {
		return "/enemy/blue.png";
	}

	@Override
	public int getWidth() {
		return 10;
	}

	@Override
	public int getHeight() {
		return 10;
	}

	public void setPoints(List<Point> points) {
		movement = points;
	}

	@Override
	public void onContact(Player player, Level level) {
		player.onEnemyContact();
	}

	/**
	 * Receive damage from a bomb explosion.
	 * The enemy is removed from the level.
	 */
	@Override
	public void takeDamage(Level level) {
		level.removeElement(this);
	}

	public List<Point> getMovement() {
		return movement;
	}

	public char getDirection() {
		return direction;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setStrategyMovement(AutomaticMovement strategyMovement) {
		this.strategyMovement = strategyMovement;
	}

	public void setDirection(char direction) {
		this.direction = direction;
	}

	public float getSpeed() {
		return this.speed;
	}
}
