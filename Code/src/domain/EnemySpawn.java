package domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class EnemySpawn implements LevelComponent {

	private String type;
	private List<Point> movement;

	public EnemySpawn(String type, Point... movement) {
		this.type = type;
		this.movement = new ArrayList<>(List.of(movement));
	}

	@Override
	public void addTo(Level level) {
		level.addEnemy(new ArrayList<>(movement), type);
	}
}
