package domain;

import java.util.List;

public class CollisionChecker {
	
	public boolean canMove(HitBox mover, int px, int py, CollisionContext context) {
		List<Solid> solidElements = context.getSolidElements();
		boolean isOverlaps = false;
		for (Solid solid : solidElements) {
            if (solid == mover) continue;
            isOverlaps = overlaps(px, py, mover, solid);
            if (isOverlaps) return false;
        }
        return true;
	}
	
	public void checkContactsWithInteractable(Player player, CollisionContext context, Level level) {
		for(Interactable element : context.getInteractableElements()) {
			if(overlaps(player, (HitBox) element)) {
				element.onContact(player, level);
			}
		}
	}
	
	private boolean overlaps(HitBox a, HitBox b) {
		return ((Element)a).getPosX() < ((Element)b).getPosX() + b.getWidth()
        && ((Element)a).getPosX() + a.getWidth() > ((Element)b).getPosX()
        && ((Element)a).getPosY() < ((Element)b).getPosY() + b.getHeight()
        && ((Element)a).getPosY() + a.getHeight() > ((Element)b).getPosY();
	}
	
	
	private boolean overlaps(int px, int py, HitBox mover, HitBox other) {
		 return px < ((Element) other).getPosX() + other.getWidth()
         && px + mover.getWidth() > ((Element)other).getPosX()
         && py < ((Element)other).getPosY() + other.getHeight()
         && py + mover.getHeight() > ((Element)other).getPosY();
	}

}
