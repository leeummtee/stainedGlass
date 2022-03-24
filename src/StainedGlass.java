import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class StainedGlass extends Frame {

	BufferedImage srcImg;

	BufferedImage finalResult;

	int width, height;

	static BufferedImage I;
	static int px[], py[], color[], cells = 1530;

	// constructor
	// Get an image from the specified file in the current directory on the
	// local hard disk.
	public StainedGlass() {
		try {
			srcImg = ImageIO.read(new File("test.jpg"));

		} catch (Exception e) {
			System.out.println("Cannot load the provided image");
		}
		this.setTitle("Stained Glass Filter");
		this.setVisible(true);

		width = srcImg.getWidth();
		height = srcImg.getHeight();

		finalResult = stainedGlassFilter(srcImg);

		// Anonymous inner-class listener to terminate program
		this.addWindowListener(new WindowAdapter() {// anonymous class definition
			public void windowClosing(WindowEvent e) {
				System.exit(0);// terminate the program
			}// end windowClosing()
		}// end WindowAdapter
		);// end addWindowListener
	}// end constructor

	public BufferedImage stainedGlassFilter(BufferedImage src) {
		BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());

		int n = 0;
		Random rand = new Random();
		// I = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		px = new int[cells];
		py = new int[cells];
		color = new int[cells];
		List<VoronoiPoint> voronoiPointList = new ArrayList<VoronoiPoint>();

		// initiate Lists
		for (int i = 0; i < cells; i++) {
			px[i] = rand.nextInt(width);
			py[i] = rand.nextInt(height);

			voronoiPointList.add(new VoronoiPoint());
		}

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				n = 0;
				for (int i = 0; i < cells; i++) {
					if (distance(px[i], x, py[i], y) < distance(px[n], x, py[n], y)) {
						n = i;
					}
				}
				voronoiPointList.get(n).AddPixel(x, y, src.getRGB(x, y));
				// result.setRGB(x, y, color[n]);
			}
		}
		
		for (int i=0; i < voronoiPointList.size(); i++) {
			
			VoronoiPoint tempVoronoi = voronoiPointList.get(i);
			List<Pixel> pixelCollection = tempVoronoi.getPixelList();
			
			int rgbValue = tempVoronoi.getColor();
					
			for(int j = 0; j < pixelCollection.size();j++) {
				Pixel temp = pixelCollection.get(j);
				result.setRGB(temp.getX(), temp.getY(), rgbValue);
			}

		}

		// Graphics2D g = I.createGraphics();

		return result;
	}

	// Until functions ========

	private double distance(int x1, int x2, int y1, int y2) {
		double d;
		d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)); // Euclidian
		// d = Math.abs(x1 - x2) + Math.abs(y1 - y2); // Manhattan
		// d = Math.pow(Math.pow(Math.abs(x1 - x2), p) + Math.pow(Math.abs(y1 - y2), p),
		// (1 / p)); // Minkovski
		return d;
	}

	private int clip(int v) {
		v = v > 255 ? 255 : v;
		v = v < 0 ? 0 : v;
		return v;
	}

	protected int getRed(int pixel) {
		return (pixel >>> 16) & 0xFF;
	}

	protected int getGreen(int pixel) {
		return (pixel >>> 8) & 0xFF;
	}

	protected int getBlue(int pixel) {
		return pixel & 0xFF;
	}

	// display=================

	public void paint(Graphics g) {
		int w = width / 2;
		int h = height / 2;

		this.setSize(w * 2 + 100, h * 2 + 50);

		g.setColor(Color.BLACK);
		Font f1 = new Font("Verdana", Font.PLAIN, 13);
		g.setFont(f1);

		g.drawImage(srcImg, 20, 50, w, h, this);
		g.drawString("Before", 20, 50 + h + 30);

		g.drawImage(finalResult, 50 + w, 50, w, h, this);
		g.drawString("After", 50 + w, 50 + h + 30);

	}
	// =======================================================//

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StainedGlass img = new StainedGlass();// instantiate this object
		img.repaint();// render the image

	}

}
