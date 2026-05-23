package domain;

import java.io.Serializable;

/**
 * @implNote takeDamage() added with Claude Sonnet 4.6
 */
public abstract class Player extends Entity implements Damageable, Serializable {

	private static final int INITIAL_LIFES = 1;

	private int collectedCoins;
	private int deaths;
	private int totalDeathsGot;
	private int lifes;
	protected String name;
	private PlayerType playerType;
	private int respawnX, respawnY;
	private boolean goalCompleted = false;
	protected PlayerState state;
	
	/**
	 * 
	 * @param type
	 * @throws HardestGameException
	 */
	public Player(PlayerType type, String name) throws HardestGameException {
		deaths = 0;
		collectedCoins = 0;
		setAttributesPlayer(75, 75);
		this.name = name;
		speed = 3;
		lifes = INITIAL_LIFES;
		totalDeathsGot = 0;
		playerType = type;
		this.state = createInitialState(type);
		width = 20.0f;
		height = 20.0f;
	}
	
	private PlayerState createInitialState(PlayerType type) throws HardestGameException {
		switch (type) {
			case RED : return new Red(this);
			case BLUE : return new Blue(this);
			case GREEN : return new Green(this);
			default : throw new HardestGameException(HardestGameException.PLAYER_TYPE_UNKNOWN); 
		}
	}
	
	public Player(int x, int y) {
		deaths = 0;
		collectedCoins = 0;
		setAttributesPlayer(x, y);
		lifes = INITIAL_LIFES;
		state = new Red(this);
		setRespawnPoint(x,y);
		width = 20.0f;
		height = 20.0f;
		speed = 3;
	}
	

	public void setRespawnPoint(int x, int y) {
		respawnX = x;
		respawnY = y;
	}
	
	public void respawn() throws HardestGameException {
		this.posX = respawnX;
		this.posY = respawnY;
		this.lifes = INITIAL_LIFES;
		this.state = createInitialState(playerType);
	}
	
	public int getRespawnX() {
		return respawnX;
	}
	
	public int getRespawnY() {
		return respawnY;
	}
	
	public float getSpeed() {
		return speed*state.getSpeedMultiplier();
	}

	public void setAttributesPlayer(int x, int y) {
		posX = x;
		posY = y;
	}
	
	/**
	 * Change the state of the Player
	 * @param statePY the new state wished: it could be Slowed or Dead.
	 */
	public void setState(PlayerState statePY) {
		System.out.println("Estado cambiado a: " + statePY.getClass().getSimpleName());
		this.state = statePY;
	}

	/**
	 * Makes move the player of game
	 * @param direction direction is where going to move the player
	 */
	public void move(char direction) {
		float speedPlus = this.speed*state.getSpeedMultiplier();
		switch (direction) {
			case 'u': posY -= speedPlus;
				break;
			case 'd': posY += speedPlus;
				break;
			case 'l': posX -= speedPlus;
				break;
			case 'r': posX += speedPlus;
				break;
		}
	}
	
	public void move(char direction, CollisionContext context, CollisionChecker checker) {
		float speedPlus = this.speed*state.getSpeedMultiplier();;
		
		float nextX = posX;
		float nextY = posY;
		
		switch (direction) {
		case 'u': nextY -= speedPlus;
			break;
		case 'd': nextY += speedPlus;
			break;
		case 'l': nextX -= speedPlus;
			break;
		case 'r': nextX += speedPlus;
			break;
		default: 
			return;
		}
		
		if (!checker.canMove(this, nextX, nextY, context)) return;
		
		posX = nextX;
		posY = nextY;
	}
	
	public void reset() {
		this.collectedCoins = 0;
		this.goalCompleted = false;
		this.lifes = INITIAL_LIFES;
		this.deaths = 0;
		try {
			this.state = createInitialState(playerType);
		} catch(HardestGameException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Check if the player is dead.
	 * @return
	 */
	public boolean isDead() {
		return state.isDead();
	}
	
	@Override
	public float getWidth() {
		return width*state.getSizeMultiplier();
	}
	
	@Override
	public float getHeight() {
		return height*state.getSizeMultiplier();
	}
	
	public String getName() {
		return name;
	}
	
	public void onEnemyContact() {
		state.onEnemyContact();
	}

	public void addCoin() {
		collectedCoins ++;
	}

	public void destroy() {
		if (isDead()) {
			return;
		}
		setState(new DeadState(this));
	}

	/**
	 * Receive damage from a bomb (direct contact or explosion area).
	 */
	@Override
	public void takeDamage(Level level) {
		if (isDead()) {
			return;
		}
		substractLife();
		if (lifes <= 0) {
			destroy();
		}
	}

	public void addLife() {
		lifes++;
	}

	public int getLifes() {
		return lifes;
	}
	
	public void substractLife() {
		if (lifes > 0) {
			lifes --;
		}
	}
	
	public PlayerType getPlayerType() {
		return playerType;
	}
	
	public void addDeaths() {
		deaths ++;
	}
	
	public void substractDeaths() {
		deaths --;
	}

	public int getDeaths() {
		return deaths;
	}

	public int getCollectedCoins() {
		return collectedCoins;
	}
	
	public void markGoalCompleted() {
		goalCompleted = true;
	}
	
	public boolean hasGoalCompleted() {
		return goalCompleted;
	}
	
	public void setPosition(int x, int y) {
		posX = x;
		posY = y;
	}
	
	
	public int getTotalDeathsGot() {
		return totalDeathsGot;
	}
	
	public String getNameState() {
		return state.getClass().getSimpleName().toLowerCase();
	}

	public String getPathImage() {
		return "/"+getNameClass()+"/"+getNameState()+".png";		
	}
}
