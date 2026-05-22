package domain;

/**
 * Class helped to make with GPT 5.5
 */
public final class EnemyMovementStrategyFactory {

	private EnemyMovementStrategyFactory() {
	}

	public static AutomaticMovement create(String type, Enemy enemy) {
		return switch (type) {
			case "basic" -> new Basic(enemy);
			case "vertical" -> new Vertical(enemy);
			case "acelerate" -> new Acelerate(enemy);
			default -> throw new IllegalArgumentException("Tipo de enemigo no soportado: " + type);
		};
	}
}
