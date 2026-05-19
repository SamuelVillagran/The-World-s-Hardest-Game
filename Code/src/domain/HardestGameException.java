package domain;

import java.io.Serializable;

public class HardestGameException extends Exception implements Serializable {
	public static final String PLAYER_TYPE_UNKNOWN = "Tipo de jugador desconocido.";
	
	
	public HardestGameException(String message) {
		super(message);
	}
}
