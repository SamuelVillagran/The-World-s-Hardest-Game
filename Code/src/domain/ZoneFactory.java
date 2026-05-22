package domain;

import java.awt.Point;
import java.util.List;

public final class ZoneFactory {

	private ZoneFactory() {
	}

	public static Zone create(String type, List<Point> figure) {
		return switch (type) {
			case "goal" -> new GoalZone(new Figure(figure));
			case "initial" -> new InitialZone(new Figure(figure));
			default -> throw new IllegalArgumentException("Tipo de zona no soportado: " + type);
		};
	}
}
