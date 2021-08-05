import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Renderer extends Thread {
	public static BufferedImage canvas, oldCanvas;
	
	public static double scale = 0.002, oldScale = scale;
	public static Point2D.Double position, oldPosition;
	
	public static int cornerOffsetX, cornerOffsetY;
	
	static int maxIterations = 500;
	
	private static int maxThreads = 8, threadCount;
	private static int ZoneCountX = 16, ZoneCountY = 16;
	private static ArrayList<Renderer> renderThreads;
	
	private int width, height, startX, startY;
	private boolean running = true;
	
	public Renderer(int startX, int width, int startY, int height) {
		this.startX = startX;
		this.width = width;
		this.startY = startY;
		this.height = height;
		
		this.start();
	}
	
	public static void init() {
		renderThreads = new ArrayList<>(ZoneCountX * ZoneCountY);
	}
	
	public static void render() {
		int width = canvas.getWidth()/ZoneCountX;
		int height = canvas.getHeight()/ZoneCountY;
		
		for (Renderer renderer : renderThreads) {
			renderer.end();
		}
		renderThreads.clear();
		threadCount = 0;
		
		for (int x = 0; x < ZoneCountX; x++) {
			for (int y = 0; y < ZoneCountY; y++) {
				renderThreads.add(new Renderer(width * x, width * (x + 1), height * y, height * (y + 1)));
			}
		}
	}
	
	public void run() {
		//waiting for available thread
		while (threadCount > maxThreads) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		threadCount++;
		
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
		
		threadCount--;
	}
	
	public void end() {
		running = false;
	}
	
	public static Point2D.Double squareNum(Point2D.Double point) {
		point.setLocation(Math.pow(point.x, 2) -Math.pow(point.y, 2), 2* point.x * point.y);
		
		return point;
	}
}
