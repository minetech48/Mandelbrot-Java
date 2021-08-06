import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Display extends JPanel {
	
	JFrame frame;
	
	//setting up/ initializing
	public Display() {
		frame = new JFrame();
		
		setPreferredSize(new Dimension(1600, 900));
		
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Renderer.position = new Point2D.Double(0, 0);
		Renderer.oldPosition = new Point2D.Double(0, 0);
		
		UserInput input = new UserInput();
		addMouseListener(input);
		frame.addKeyListener(input);
		
		Renderer.canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		frame.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent evt) {
				Renderer.canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
				
				Renderer.cornerOffsetX = -Renderer.canvas.getWidth()/2;
				Renderer.cornerOffsetY = -Renderer.canvas.getHeight()/2;
				
				render();
			}
		});
		
		frame.setVisible(true);
		render();
		
		//display update clock
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		
		executor.scheduleAtFixedRate(new Runnable() {public void run() {frame.repaint();}}, 0, 20, TimeUnit.MILLISECONDS);
	}
	
	public void render() {
		Renderer.oldCanvas = Renderer.canvas;
		Renderer.canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		Renderer.render();
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		//drawing old, translated image as background while new image renders
		if (Renderer.oldCanvas != null) {
				double scale = Renderer.oldScale / Renderer.scale;
				double offsetX = -(Renderer.position.x - Renderer.oldPosition.x)/Renderer.scale;
				double offsetY = -(Renderer.position.y - Renderer.oldPosition.y)/Renderer.scale;
				
				AffineTransform defaultTransform = g2.getTransform();
				
				g2.transform(AffineTransform.getTranslateInstance(offsetX, offsetY));
				g2.transform(AffineTransform.getTranslateInstance(Renderer.canvas.getWidth()/2, Renderer.canvas.getHeight()/2));
				g2.transform(AffineTransform.getScaleInstance(scale, scale));
				g2.transform(AffineTransform.getTranslateInstance(-Renderer.canvas.getWidth()/2, -Renderer.canvas.getHeight()/2));
				
				g2.drawImage(Renderer.oldCanvas, null, null);
				
				g2.setTransform(defaultTransform);
		}
		
		g2.drawImage(Renderer.canvas, null, null);
	}
}
