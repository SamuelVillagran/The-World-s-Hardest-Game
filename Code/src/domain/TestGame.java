package domain;

import java.util.List;

public class TestGame {
    public static void main(String[] args) throws Exception {
        TheDOPOHardestGame game = TheDOPOHardestGame.getGame();
        game.startGame(new GameMode() {
            @Override
            public List<Player> createPlayers() {
                return List.of(new Player("test", null));
            }
        }, 1);
        
        for (int i = 0; i < 5; i++) {
            game.update();
        }
    }
}
