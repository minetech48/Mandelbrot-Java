import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

public class Renderer extends JPanel {
	
	JFrame frame;
	private BufferedImage canvas;
	
	public boolean render = true;
	
	public static BigDecimal scale = new BigDecimal(0.002);
	public static SmallPoint position;
	
	public Renderer() {
		frame = new JFrame();
		
		setPreferredSize(new Dimension(1600, 900));
		
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		position = new SmallPoint();
		
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
			
			SmallPoint z, c;
			z = new SmallPoint();
			c = new SmallPoint();
			int maxIteration;
			
			for (int x = -width / 2; x < width / 2; x++) {
				for (int y = -height / 2; y < height / 2; y++) {
					maxIteration = -1;
					z = new SmallPoint();
					
					c.setLocation(new BigDecimal(x).multiply(scale).add(position.x), new BigDecimal(y).multiply(scale).add(position.y));
					
					for (int i = 0; i < 100; i++) {
						//squaring
						squareNum(z);
						//adding
						z.setLocation(z.x.add(c.x), z.y.add(c.y));
						
						if ((z.x.pow(2).add(z.y.pow(2))).compareTo(new BigDecimal(4)) > 0) {
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
	
	
	public static SmallPoint squareNum(SmallPoint point) {
		point.setLocation(point.x.pow(2).subtract(point.y.pow(2)), point.x.multiply(point.y).multiply(new BigDecimal(2)));
		
		return point;
	}
}
