package domain;

/**
 * Each element has interaction or effects on the players must
 * implement this interface. 
 */
public interface Interactable {
	public void onContactWithPlayer(Player player, Level level);
	public void onContactWithEnemy(Enemy enemy, Level level);
	
}

