package domain;

import java.io.Serializable;

public abstract class SuperObject extends Element implements Interactable, Serializable{

	public String getName() {
		return "superobject";
	}

	
	
}
