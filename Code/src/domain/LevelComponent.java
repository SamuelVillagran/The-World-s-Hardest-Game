package domain;

import java.io.Serializable;

/**
 * Level component class, structured with the help of GPT 5.5 AI.
 * Uses the Command pattern to set the instruction that level uses to add elements to level.
 */
public interface LevelComponent extends Serializable {

	void addTo(Level level);
}
