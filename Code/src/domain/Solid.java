package domain;

import java.io.Serializable;

public interface Solid extends HitBox, Serializable {
	public static boolean isSolid() {
		return true;
	}
}
