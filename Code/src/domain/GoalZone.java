package domain;

import java.io.Serializable;

public class GoalZone extends Zone implements Serializable {

	public GoalZone(Figure figure) {
		super(figure);
	}

	@Override
	public void whenPlayerEnter(Player player) {
		if(contains(player.getPosX(), player.getPosY())) {
			player.markGoalCompleted();
		}
	}

}
