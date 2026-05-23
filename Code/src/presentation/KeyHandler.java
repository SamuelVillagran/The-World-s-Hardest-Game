package presentation;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {

	private boolean up;
	private boolean down;
	private boolean left;
	private boolean rigth;
	private boolean w;
	private boolean s;
	private boolean a;
	private boolean d;
	private boolean esc;
	private boolean escToggle;

	public KeyHandler() {
		up = false;
		down = false;
		rigth = false;
		left = false;
		a = false;
		s = false;
		d = false;
		w = false;
		escToggle = false;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
			
		int code = e.getKeyCode();
		
		if (code == KeyEvent.VK_W) {
			w = true;
		}
		if (code == KeyEvent.VK_S) {
			s = true;
		}
		if (code == KeyEvent.VK_A) {
			a = true;
		}
		if (code == KeyEvent.VK_D) {
			d = true;
		}
		if (code == KeyEvent.VK_UP) {
			up = true;
		}
		if (code == KeyEvent.VK_DOWN) {
			down = true;
		}
		if (code == KeyEvent.VK_LEFT) {
			left = true;
		}
		if (code == KeyEvent.VK_RIGHT) {
			rigth = true;
		}
		if (code == KeyEvent.VK_ESCAPE) {
			esc = true;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if (code == KeyEvent.VK_W) {
			w = false;
		}
		if (code == KeyEvent.VK_S) {
			s = false;
		}
		if (code == KeyEvent.VK_A) {
			a = false;
		}
		if (code == KeyEvent.VK_D) {
			d = false;
		}
		if (code == KeyEvent.VK_UP) {
			up = false;
		}
		if (code == KeyEvent.VK_DOWN) {
			down = false;
		}
		if (code == KeyEvent.VK_LEFT) {
			left = false;
		}
		if (code == KeyEvent.VK_RIGHT) {
			rigth = false;
		}
		if (code == KeyEvent.VK_ESCAPE) {
			esc = false;
			escToggle = true;
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
	
	public boolean getW() {
		return w;
	}

	public void setW(boolean w) {
		this.w = w;
	}

	public boolean getS() {
		return s;
	}

	public void setS(boolean s) {
		this.s = s;
	}

	public boolean getA() {
		return a;
	}

	public void setA(boolean a) {
		this.a = a;
	}

	public boolean getD() {
		return d;
	}

	public void setD(boolean d) {
		this.d = d;
	}
	
	public boolean getEsc() {
		return esc;
	}

	/**
	 * Devuelve true una sola vez cuando Escape fue soltado y lo consume.
	 * Usar para toggle de pausa sin repetir en cada frame mientras se sostiene la tecla.
	 */
	public boolean consumeEscToggle() {
		boolean fired = escToggle;
		escToggle = false;
		return fired;
	}
}
