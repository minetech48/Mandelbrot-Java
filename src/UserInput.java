import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UserInput implements MouseListener {
	
	
	public void mouseClicked(MouseEvent e) {
		Renderer.scale*= 0.5;
		System.out.println(Renderer.scale);
		
		Renderer.position.setLocation(
				Renderer.position.x + (e.getX() - Main.renderer.getWidth()/2) *Renderer.scale,
				Renderer.position.y + (e.getY() - Main.renderer.getHeight()/2) *Renderer.scale
		);
		
		Main.renderer.render();
		Main.renderer.frame.repaint();
	}
	
	public void mousePressed(MouseEvent e) {}
	
	public void mouseReleased(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
