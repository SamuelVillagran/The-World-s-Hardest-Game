package domain;

public class GameSaveState {

	private static final long serialVersionUID = 1L;
    public int levelNumber;
    public int p1X, p1Y;
    
    public GameSaveState(int level, int x, int y) {
        this.levelNumber = level;
        this.p1X = x;
        this.p1Y = y;
    }
}
