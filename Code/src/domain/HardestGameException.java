package domain;

import java.io.Serializable;

public class HardestGameException extends Exception implements Serializable {
	public static final String PLAYER_TYPE_UNKNOWN = "Tipo de jugador desconocido.";
	public static final String FILE_NO_FOUND = "El juego que se seleccionó no fue encontrado.";
	public static final String TIME_OVER = "Tiempo se ha acabado!";
	
	
	public HardestGameException(String message) {
		super(message);
	}
}
