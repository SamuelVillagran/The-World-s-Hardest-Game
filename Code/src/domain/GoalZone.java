package domain;

public class GoalZone extends Zone{

	public GoalZone(Figure figure) {
		super(figure);
	}

	@Override
	public void whenPlayerEnter(Player player, Level level) {
			player.markGoalCompleted();
	}

}
