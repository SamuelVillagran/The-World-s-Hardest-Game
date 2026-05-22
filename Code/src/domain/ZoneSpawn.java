package domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class ZoneSpawn implements LevelComponent {

	private String type;
	private List<Point> points;

	public ZoneSpawn(String type, Point... points) {
		this.type = type;
		this.points = new ArrayList<>(List.of(points));
	}

	@Override
	public void addTo(Level level) {
		level.addZone(new ArrayList<>(points), type);
	}
}
