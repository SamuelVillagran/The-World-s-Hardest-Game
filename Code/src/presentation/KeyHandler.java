package presentation;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {

	private boolean up;
	private boolean down;
	private boolean left;
	private boolean rigth;
	
	public KeyHandler() {
		up = false;
		down = false;
		rigth = false;
		left = false;
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
			
		int code = e.getKeyCode();
		
		if (code == KeyEvent.VK_W) {
			up = true;
		}
		if (code == KeyEvent.VK_S) {
			down = true;
		}
		if (code == KeyEvent.VK_D) {
			rigth = true;
		}
		if (code == KeyEvent.VK_A) {
			left = true;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if (code == KeyEvent.VK_W) {
			setUp(false);
		}
		if (code == KeyEvent.VK_S) {
			setDown(false);
		}
		if (code == KeyEvent.VK_A) {
			setLeft(false);
		}
		if (code == KeyEvent.VK_D) {
			setRigth(false);
		}
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRigth() {
		return rigth;
	}

	public void setRigth(boolean rigth) {
		this.rigth = rigth;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean getUp() {
		return up;
	}

	public boolean getDown() {
		return down;
	}
	
	public boolean getRigth() {
		return rigth;
	}
	
	public boolean getLeft() {
		return left;
	}
}
