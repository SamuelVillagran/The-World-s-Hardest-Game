package domain;

import java.io.Serializable;
import java.util.List;

public class CollisionChecker implements Serializable {
	
	public boolean canMove(Element mover, int px, int py, CollisionContext context) {
		List<Solid> solidElements = context.getSolidElements();
		boolean isOverlaps = false;
		for (Solid solid : solidElements) {
            if (solid == mover) continue;
            isOverlaps = overlaps(px, py, mover, (Element) solid);
            if (isOverlaps) return false;
        }
        return true;
	}
	
	public void checkContactsWithInteractable(Player player, CollisionContext context, Level level) {
		for(Interactable element : context.getInteractableElements()) {
			if(overlaps(player, element)) {
				element.onContact(player, level);
			}
		}
	}
	
	private boolean overlaps(Element a, Interactable element) {
		return ((Element)a).getPosX() < ((Element)element).getPosX() + ((Element) element).getWidth()
        && ((Element)a).getPosX() + a.getWidth() > ((Element)element).getPosX()
        && ((Element)a).getPosY() < ((Element)element).getPosY() + ((Element) element).getHeight()
        && ((Element)a).getPosY() + a.getHeight() > ((Element)element).getPosY();
	}
	
	
	private boolean overlaps(int px, int py, Element mover, Element other) {
		 return px < ((Element) other).getPosX() + other.getWidth()
         && px + mover.getWidth() > ((Element)other).getPosX()
         && py < ((Element)other).getPosY() + other.getHeight()
         && py + mover.getHeight() > ((Element)other).getPosY();
	}

}
