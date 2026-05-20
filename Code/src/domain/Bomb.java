package domain;

import java.io.Serializable;
import java.util.List;

/**
 * A bomb that:
 *  - Damages ANY entity (player or enemy) that touches it directly.
 *  - Every 5 seconds triggers an explosion that damages every entity
 *    within a 3x3 tile radius (108x108 px centered on the bomb).
 *  - The bomb is NEVER removed from the level.
 *
 * @implNote Implemented with Claude Sonnet 4.6
 */
public class Bomb extends DinamicObject implements Interactable, AutomaticMovement, Serializable {

	private static final int FPS = 60;
	private static final int FUSE_SECONDS = 5;
	private static final int FUSE_TICKS   = FPS * FUSE_SECONDS;           // 300 ticks
	private static final int BLAST_TILES  = 3;
	private static final int BLAST_PX     = BLAST_TILES * DimensionGame.TILESIZE; // 108 px
	

	/** Counts game frames. Resets after each explosion. */
	private int tickCounter = 0;

	/** Flag set by move() and consumed by Level.update() to fire the explosion. */
	private boolean pendingExplosion = false;

	public Bomb(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}

	// ── AutomaticMovement ────────────────────────────────────────────────

	/**
	 * Called every frame by Level.update().
	 * Counts frames and marks a pending explosion every 5 seconds.
	 */
	@Override
	public void move() {
		tickCounter++;
		if (tickCounter >= FUSE_TICKS) {
			tickCounter      = 0;
			pendingExplosion = true;
		}
	}

	// ── Interactable ─────────────────────────────────────────────────────

	/**
	 * Triggered when a player walks directly over the bomb.
	 * Also checks if any enemy overlaps the bomb right now.
	 * The bomb is NEVER removed from the level.
	 */
	@Override
	public void onContact(Player player, Level level) {
		player.takeDamage(level);
		// Also hit any enemy that is standing on the bomb at this moment
		damageOverlappingEnemies(level);
	}

	// ── Explosion ────────────────────────────────────────────────────────

	/**
	 * Called by Level.update() after move() to fire a pending explosion.
	 * Public so Level can call it without reflection.
	 */
	public void explodeIfPending(Level level) {
		if (pendingExplosion) {
			pendingExplosion = false;
			explode(level);
		}
	}

	/**
	 * Damages every Damageable entity (players AND enemies) in a 3x3 tile
	 * area centered on this bomb.
	 */
	private void explode(Level level) {
		int halfBlast = BLAST_PX / 2;
		int bx = posX - halfBlast;
		int by = posY - halfBlast;
		List<Damageable> targets = level.getDamageablesInArea(bx, by, BLAST_PX, BLAST_PX);
		for (Damageable d : targets) {
			d.takeDamage(level);
		}
	}

	/**
	 * Damages every enemy whose bounding box overlaps the bomb's own hitbox.
	 * Called from onContact so that enemies touching the bomb are also hit.
	 */
	private void damageOverlappingEnemies(Level level) {
		List<Damageable> targets = level.getDamageablesInArea(posX, posY, getWidth(), getHeight());
		for (Damageable d : targets) {
			if (d instanceof Enemy) {
				d.takeDamage(level);
			}
		}
	}

	// ── Element metadata ─────────────────────────────────────────────────

	@Override
	public String getPathImage() {
		return "/superobject/" + getNameClass() + ".png";
	}

	@Override
	public String getNameClass() {
		return getClass().getSimpleName().toLowerCase();
	}

	@Override
	public int getWidth() {
		return 32;
	}

	@Override
	public int getHeight() {
		return 32;
	}
}
