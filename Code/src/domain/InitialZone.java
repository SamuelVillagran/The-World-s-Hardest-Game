package domain;

public class InitialZone extends Zone{

	public InitialZone(Figure figure) {
		super(figure);
	}

	@Override
	public void whenPlayerEnter(Player player, Level level) {
		// No hace nada porque es la zona inicial
	}

}
