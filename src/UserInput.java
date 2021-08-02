import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;

public class UserInput implements MouseListener {
	
	
	public void mouseClicked(MouseEvent e) {
		Renderer.scale = Renderer.scale.multiply(new BigDecimal(0.5));
		System.out.println(Renderer.scale);
		
		Renderer.position.setLocation(
				Renderer.position.x.add(new BigDecimal(e.getX() - Main.renderer.getWidth()/2)).multiply(Renderer.scale),
				Renderer.position.y.add(new BigDecimal(e.getY() - Main.renderer.getHeight()/2)).multiply(Renderer.scale)
		);
		
		Main.renderer.render();
		Main.renderer.frame.repaint();
	}
	
	public void mousePressed(MouseEvent e) {}
	
	public void mouseReleased(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
