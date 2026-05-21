package domain;

/**
 * Observer interface that the presentation will implement to get the different
 * events of the the game loop.
 */
public interface GameObserver {

	/**
	 * Method called before each update of the logic frame.
	 * Presentation sends the users input to the domain.
	 */
	public void preUpdate();
	
	/**
	 * Method called after each update of the logic frame.
	 * Presentation has to draw the screen.
	 */
	public void postUpdate();
	
	/**
	 * Method called after second elapse in game.
	 * @param secondsRemaining remaining seconds in the actual level.
	 */
	public void secondsElapsed(int secondsRemaining);
}
