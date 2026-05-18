package domain;

import java.util.List;

public interface CollisionContext {
	public abstract List<Solid> getSolidElements();
	public abstract List<Interactable> getInteractableElements();
}
