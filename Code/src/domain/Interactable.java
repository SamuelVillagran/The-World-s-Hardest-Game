package domain;

/**
 * Each element has interaction or effects on the players must
 * implement this interface. 
 */
public interface Interactable {
	public void onContact(Player player, Level level);
}
