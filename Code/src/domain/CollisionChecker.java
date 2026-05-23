package domain;

import java.io.Serializable;
import java.util.List;

public class CollisionChecker implements Serializable {
	
	public boolean canMove(Element mover, float nextX, float nextY, CollisionContext context) {
		List<Solid> solidElements = context.getSolidElements();
		boolean isOverlaps = false;
		for (Solid solid : solidElements) {
            if (solid == mover) continue;
            isOverlaps = overlaps(nextX, nextY, mover, (Element) solid);
            if (isOverlaps) return false;
        }
        return true;
	}
	
	public void checkContactsWithInteractable(Player player, CollisionContext context, Level level) {
		for(Interactable element : context.getInteractableElements()) {
			if(overlaps(player, element)) {
				element.onContactWithPlayer(player, level);
			}
		}
	}
	
	private boolean overlaps(Element a, Interactable element) {
		return ((Element)a).getPosX() < ((Element)element).getPosX() + ((Element) element).getWidth()
        && ((Element)a).getPosX() + a.getWidth() > ((Element)element).getPosX()
        && ((Element)a).getPosY() < ((Element)element).getPosY() + ((Element) element).getHeight()
        && ((Element)a).getPosY() + a.getHeight() > ((Element)element).getPosY();
	}
	
	
	private boolean overlaps(float nextX, float nextY, Element mover, Element other) {
		 return nextX < ((Element) other).getPosX() + other.getWidth()
         && nextX + mover.getWidth() > ((Element)other).getPosX()
         && nextY < ((Element)other).getPosY() + other.getHeight()
         && nextY + mover.getHeight() > ((Element)other).getPosY();
	}

}
