import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UserInput implements MouseListener, KeyListener {
	
	
	public void mouseClicked(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {
		Renderer.position.setLocation(
				Renderer.position.x + (e.getX() - Main.display.getWidth()/2) *Renderer.scale,
				Renderer.position.y + (e.getY() - Main.display.getHeight()/2) *Renderer.scale
		);
		Renderer.scale*= 0.2;
		
		System.out.println(Renderer.scale);
		
		Main.display.render();
	}
	
	public void mouseReleased(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	public void keyTyped(KeyEvent e) {}
	
	public void keyPressed(KeyEvent e) {
		boolean repaint = false;
		int amt = 1;
		
		if (e.isShiftDown())
			amt = 10;
		else if (e.isControlDown())
			amt = 100;
		else if (e.isAltDown())
			amt = 100000;
		
		switch (e.getKeyChar()) {
		case '+':
			Renderer.maxIterations+= amt;
			repaint = true;
			break;
		case '-':
			Renderer.maxIterations-= amt;
			repaint = true;
			break;
		}
		
		if (repaint) {
			System.out.println(Renderer.maxIterations);
			
			Main.display.render();
		}
	}
	
	public void keyReleased(KeyEvent e) {}
}
