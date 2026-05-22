package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is LevelDefinition class
 * This save the final result of level, give and modify information of level (getters and setters in the constructor)
 * Its state is inmutable  
 * LevelDefinition class, structured with the help of GPT 5.5 AI.
 * This class used design patter Value Object
 */
public class LevelDefinition implements Serializable {

	private int mapNumber;
	private int coinsRequired;
	private int timeLimitSeconds;
	private List<LevelComponent> components;

	/**
	 * Constructor of LevelDefinition, set the attributes of LevelDefinition
	 * @param mapNumber mapNumber is the number of map that going to create
	 * @param coinsRequired coinsRequired is the integer of numbers that requires at the level
	 * @param timeLimitSeconds timeLimitSeconds is the limit time given in seconds that players can win level 
	 * @param components components are diferents objects that can add to level
	 */
	LevelDefinition(int mapNumber, int coinsRequired, int timeLimitSeconds, List<LevelComponent> components) {
		this.mapNumber = mapNumber;
		this.coinsRequired = coinsRequired;
		this.timeLimitSeconds = timeLimitSeconds;
		this.components = new ArrayList<>(components);
	}

	public int mapNumber() {
		return mapNumber;
	}

	public int coinsRequired() {
		return coinsRequired;
	}

	public int timeLimitSeconds() {
		return timeLimitSeconds;
	}

	public List<LevelComponent> components() {
		return new ArrayList<>(components);
	}
}
