import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Renderer extends JPanel {
	
	JFrame frame;
	private BufferedImage canvas;
	
	public boolean render = true;
	
	public static double scale = 0.002;
	public static Point2D.Double position;
	
	public Renderer() {
		frame = new JFrame();
		
		setPreferredSize(new Dimension(1600, 900));
		
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		position = new Point2D.Double(0, 0);
		
		UserInput input = new UserInput();
		addMouseListener(input);
		
		frame.setVisible(true);
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		if (render) {
			int height = getHeight(), width = getWidth();
			canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			
			boolean drawPixel;
			
			Point2D.Double z, c;
			z = new Point2D.Double(0, 0);
			c = new Point2D.Double(0, 0);
			int maxIteration;
			
			for (int x = -width / 2; x < width / 2; x++) {
				for (int y = -height / 2; y < height / 2; y++) {
					maxIteration = -1;
					z.setLocation(0, 0);
					
					c.setLocation(x * scale + position.x, y * scale + position.y);
					
					for (int i = 0; i < 100; i++) {
						//squaring
						squareNum(z);
						//adding
						z.setLocation(z.x + c.x, z.y + c.y);
						
						if (Math.sqrt(z.x * z.x + z.y * z.y) > 2) {
							maxIteration = i;
							break;
						}
					}
					
					if (maxIteration == -1)
						canvas.setRGB(x + width / 2, y + height / 2, Color.black.getRGB());
					else
						canvas.setRGB(x + width / 2, y + height / 2, Color.HSBtoRGB((float) maxIteration / 100f, 1, 1));
				}
			}
			//render = false;
		}
		
		g2.drawImage(canvas, null, null);
		
		System.out.println(g2);
	}
	
	public void render() {
		render = true;
	}
	
	
	public static Point2D.Double squareNum(Point2D.Double point) {
		point.setLocation(Math.pow(point.x, 2) -Math.pow(point.y, 2), 2* point.x * point.y);
		
		return point;
	}
}
