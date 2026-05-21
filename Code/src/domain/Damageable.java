package domain;

/**
 * Any entity that can receive damage from a bomb explosion or direct contact.
 * Implemented by Player and Enemy.
 *
 * @implNote Implemented with Claude Sonnet 4.6
 */
public interface Damageable {
	void takeDamage(Level level);
}
