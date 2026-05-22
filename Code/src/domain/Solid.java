package domain;

import java.io.Serializable;

public interface Solid extends Serializable {
	public static boolean isSolid() {
		return true;
	}
}
