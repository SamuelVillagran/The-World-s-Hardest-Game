package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LevelDefinition implements Serializable {

	private int mapNumber;
	private int coinsRequired;
	private int timeLimitSeconds;
	private List<LevelComponent> components;

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
