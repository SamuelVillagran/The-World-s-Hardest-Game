package domain;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class C1Tests {
	@Test
	public void shouldBluePlayerFasterThanRed() throws HardestGameException {
		Player red = new HumanPlayer(PlayerType.RED, "test");
		Player blue = new HumanPlayer(PlayerType.BLUE, "test");
		red.setPosition(0, 0);
		blue.setPosition(0, 30);
		
		red.move('l');
		blue.move('l');
		
		assertNotEquals(red.getPosX(), blue.getPosX());
	}
	
	@Test
	public void shouldntWalkOnWalls() throws HardestGameException{
		TheDOPOHardestGame.resetForTesting();
		TheDOPOHardestGame game = TheDOPOHardestGame.getGame();
		
		Player player = new HumanPlayer(PlayerType.RED, "test");
		Level level = Level.builder(1).time(900).build();
		
		// Jugador en area libre, fila 9 - col 10
		// Se coloca muro justo debajo del borde inferior del jugador
		// 324 + 20 = 344
		level.addWall(360, 344);
		
		game.loadTestLevel(level, List.of(player));
		// setPosition depués de loadTestLevel para anular el spawn inicial
		player.setPosition(360, 324);
		
		game.movePlayer1('d'); // intenta ir a y=327 muro en y=344 lo bloquea
		
		assertEquals(324, player.getPosY());
	}
	
	@Test
	public void playerShouldMoveFreedom() throws HardestGameException {
		TheDOPOHardestGame.resetForTesting();
		TheDOPOHardestGame game = TheDOPOHardestGame.getGame();
		
		Player player = new HumanPlayer(PlayerType.RED, "test");
		Level level = Level.builder(1).time(600).build();
		
		game.loadTestLevel(level, List.of(player));
		// Jugador en un área libre
		player.setPosition(400, 324);
		
		int initialYPos = player.getPosY(); // 324
		game.movePlayer1('d'); // 327
		game.movePlayer1('d'); // 330
		game.movePlayer1('d'); // 333
		
		assertNotEquals(initialYPos, player.getPosY());
		assertEquals(initialYPos + 9, player.getPosY());
	}
	
	@Test
	public void shouldZoneCheckIfPlayerIsInside() throws HardestGameException {
		Player player = new HumanPlayer(PlayerType.RED, "test");
		
		// Creando los vertices de la zona - Forma de cuadrado
		ArrayList<Point> vertex = new ArrayList<>();
		vertex.add(new Point(0,0));
		vertex.add(new Point(150,0));
		vertex.add(new Point(150, 150));
		vertex.add(new Point(0, 150));
		Zone zone = new InitialZone(new Figure(vertex));
		
		//Movemos jugador a dentro de la zona
		player.setPosition(50, 50);
		assertTrue(zone.contains(player.getPosX(), player.getPosY()));
		
		//Retiramos el jugador de la zona
		player.setPosition(250, 250);
		assertFalse(zone.contains(player.getPosX(), player.getPosY()));
	}
	
	@Test
	public void shouldCheckPointZoneReassignSpawnPositionPlayer() throws HardestGameException {
		Player player = new HumanPlayer(PlayerType.RED, "test");
		ArrayList<Point> vertex = new ArrayList<>();
		vertex.add(new Point(0,0));
		vertex.add(new Point(150,0));
		vertex.add(new Point(150, 150));
		vertex.add(new Point(0, 150));
		Zone checkPoint = new CheckPoint(new Figure(vertex));
		
		int respawnXZone = checkPoint.getSpawnX();
		int respawnYZone = checkPoint.getSpawnY();
		
		//Jugador estaria fuera del checkPoint
		player.setPosition(160, 160);
		checkPoint.whenPlayerEnter(player);
		assertNotEquals(respawnXZone, player.getRespawnY());
		assertNotEquals(respawnYZone, player.getRespawnY());
		
		//Jugador entra al checkPoint.
		player.setPosition(70,70);
		checkPoint.whenPlayerEnter(player);
		assertEquals(respawnXZone, player.getRespawnX());
		assertEquals(respawnYZone, player.getRespawnY());
	}
	

}
