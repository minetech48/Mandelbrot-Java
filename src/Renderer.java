import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Renderer implements Runnable {
	public static BufferedImage canvas, oldCanvas;
	
	public static double scale = 0.002, oldScale = scale;
	public static Point2D.Double position, oldPosition;
	
	public static int cornerOffsetX, cornerOffsetY;
	
	static int maxIterations = 500;
	
	private static int ZoneCountX = 16, ZoneCountY = 16;
	private static ArrayList<Renderer> renderThreads;
	
	private static ScheduledExecutorService executor;
	
	private static boolean init;
	
	private int width, height, startX, startY;
	private boolean running = true;
	
	public Renderer(int startX, int width, int startY, int height) {
		this.startX = startX;
		this.width = width;
		this.startY = startY;
		this.height = height;
	}
	
	public static void init() {
		renderThreads = new ArrayList<>(ZoneCountX * ZoneCountY);
		
		executor = Executors.newScheduledThreadPool(1);
	}
	
	public static void render() {
		int width = canvas.getWidth()/ZoneCountX;
		int height = canvas.getHeight()/ZoneCountY;
		
		if (init) {
			for (Renderer renderer : renderThreads) {
				renderer.end();
				executor.shutdownNow();
			}
		}else {
			init();
		}
		
		Renderer renderer;
		
		for (int x = 0; x < ZoneCountX; x++) {
			for (int y = 0; y < ZoneCountY; y++) {
				renderer = new Renderer(width * x, width * (x + 1), height * y, height * (y + 1));
				
				renderThreads.add(renderer);
				executor.execute(renderer);
			}
		}
	}
	
	public void run() {
		
		Point2D.Double z, c;
		z = new Point2D.Double(0, 0);
		c = new Point2D.Double(0, 0);
		int iteration;
		
		for (int x = startX; x < width; x++) {
			for (int y = startY; y < height; y++) {
				iteration = -1;
				z.setLocation(0, 0);
				
				c.setLocation((x + cornerOffsetX) * scale + position.x, (y + cornerOffsetY) * scale + position.y);
				
				for (int i = 0; i < maxIterations; i++) {
					//squaring
					squareNum(z);
					//adding
					z.setLocation(z.x + c.x, z.y + c.y);
					
					if (Math.sqrt(z.x * z.x + z.y * z.y) > 2) {
						iteration = i;
						break;
					}
				}
				
				if (!running)
					break;
				
				if (iteration == -1)
					canvas.setRGB(x, y, Color.black.getRGB());
				else
					canvas.setRGB(x, y, Color.HSBtoRGB((float) iteration / 255f, 1, 1));
			}
			if (!running)
				break;
		}
	}
	
	public void end() {
		running = false;
	}
	
	public static Point2D.Double squareNum(Point2D.Double point) {
		point.setLocation(Math.pow(point.x, 2) -Math.pow(point.y, 2), 2* point.x * point.y);
		
		return point;
	}
}
