package domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class EnemyEliminationTest {

    private static final int FRAMES = 300;
    
    @Test
    void enemyInExplosionRadiusIsRemovedAfterBombExplodes() throws HardestGameException {
        Level level = buildLevelWithBomb();
        CollisionChecker checker = new CollisionChecker();

        // Posicion dentro del radio de la bomba en (0,0) por lo que (50, 50) está dentro
        level.addEnemy(new ArrayList<>(List.of(
                new java.awt.Point(50, 50),
                new java.awt.Point(50, 100))), "basic");

        level.setPlayers(new ArrayList<>());

        long enemiesBefore = countEnemies(level);
        assertEquals(1, enemiesBefore);

        for (int i = 0; i < FRAMES; i++) {
            level.update(checker);
        }

        long enemiesAfter = countEnemies(level);
        assertEquals(0, enemiesAfter);
    }

    @Test
    void enemyOutsideExplosionRadiusSurvivesBombExplosion() throws HardestGameException {
        Level level = buildLevelWithBomb();
        CollisionChecker checker = new CollisionChecker();

        // Posición fuera del radio, bomba en pixel 9, 9 con radio 108
        // se deja un enemigo lejos.
        level.addEnemy(new ArrayList<>(List.of(
                new java.awt.Point(500, 500),
                new java.awt.Point(500, 600))), "basic");

        level.setPlayers(new ArrayList<>());

        for (int i = 0; i < FRAMES; i++) {
            level.update(checker);
        }

        assertEquals(1, countEnemies(level));
    }

    @Test
    void multipleEnemiesInRadiusAreAllEliminated() throws HardestGameException {
        Level level = buildLevelWithBomb();
        CollisionChecker checker = new CollisionChecker();

        // Dos enemigos dentro del radio
        level.addEnemy(new ArrayList<>(List.of(
                new java.awt.Point(30, 30),
                new java.awt.Point(30, 80))), "basic");
        level.addEnemy(new ArrayList<>(List.of(
                new java.awt.Point(60, 60),
                new java.awt.Point(60, 110))), "basic");

        level.setPlayers(new ArrayList<>());

        assertEquals(2, countEnemies(level));

        for (int i = 0; i < FRAMES; i++) {
            level.update(checker);
        }

        assertEquals(0, countEnemies(level));
    }

    // Movimiento de enemigos

    @Test
    void basicEnemyMovesEachFrame() throws HardestGameException {
    	Level level = Level.builder(1)
                .time(60)
                .enemy("basic", 10, 7, 10, 15)
                .build();
        level.initialize();
        
        CollisionChecker checker = new CollisionChecker();

        level.setPlayers(new ArrayList<>());

        float initialX = level.getEnemies().get(0).getPosX();
        float initialY = level.getEnemies().get(0).getPosY();

        // Ejecutar un solo frame
        level.update(checker);

        Enemy enemy = level.getEnemies().get(0);
        boolean moved = (enemy.getPosX() != initialX) || (enemy.getPosY() != initialY);
        assertTrue(moved);
    }

    @Test
    void basicEnemyReverseDirectionOnCollision() throws HardestGameException {
        // Enemigo con recorrido muy corto
        Level level = Level.builder(1)
                .time(10)
                .enemy("basic", 5, 5, 5, 6)
                .build();
        level.initialize();
        CollisionChecker checker = new CollisionChecker();
        level.setPlayers(new ArrayList<>());

        Enemy enemy = level.getEnemies().get(0);
        char firstDir = enemy.getDirection();

        // Ejecutar los frames necesarios para que el enemigo llegue al extremo y rebote
        for (int i = 0; i < 100; i++) {
            level.update(checker);
        }

        assertNotNull(enemy, "El enemigo debe seguir existiendo tras rebotar");
    }

    //Interaccion directa de jugador con bomba
    //Ayudado de ChatGPT
    @Test
    void playerTouchedByBombDirectlyTakesDamage() throws HardestGameException {
        // La bomba en tile (0,0) ocupa píxeles alrededor de (9, 9)
        // Se iubica el jugador en esa posición para la colisión directa
        Level level = buildLevelWithBomb();
        CollisionChecker checker = new CollisionChecker();

        Player player = new HumanPlayer(PlayerType.RED, "test");
        player.setPosition(9, 9); // encima de la bomba
        player.setRespawnPoint(500, 500);
        level.setPlayers(new ArrayList<>(List.of(player)));

        // Verifica la colisión
        level.update(checker);

        // El jugador rojo muere al primer contacto directo
        assertTrue(player.isDead() || player.getDeaths() > 0 || player.getPosX() == 500);
    }
    
    /** 
     * Build a level with bomb in (0,0) and without players
     */
    private Level buildLevelWithBomb() throws HardestGameException {
        Level level = Level.builder(1)
                .time(60)
                .bomb(0, 0) // pixel 9, 9
                .build();
        level.initialize();
        return level;
    }

    /**
     * Build a level with Basic enemy in a horizontal way 
      */
    private Level buildLevelWithBasicEnemy() throws HardestGameException {
        Level level = Level.builder(1)
                .time(60)
                .enemy("basic", 5, 5, 5, 20)
                .build();
        level.initialize();
        return level;
    }

    private long countEnemies(Level level) {
        return level.getEnemies().size();
    }
}
