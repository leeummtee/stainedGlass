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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class StainedGlass extends Frame {

	BufferedImage srcImg;

	BufferedImage finalResult;

	int width, height;

	static BufferedImage I;
	static int px[], py[], color[], cells = 1000;

	// constructor
	// Get an image from the specified file in the current directory on the
	// local hard disk.
	public StainedGlass() {
		try {
			srcImg = ImageIO.read(new File("test2.jpg"));

		} catch (Exception e) {
			System.out.println("Cannot load the provided image");
		}
		this.setTitle("Stained Glass Filter");
		this.setVisible(true);

		width = srcImg.getWidth();
		height = srcImg.getHeight();

		// finalResult = stainedGlassFilter(srcImg); // apply the filter to the image
		finalResult = applyQuadtree(srcImg); // apply the filter to the image

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

		// Categorize each of pixel depending on the Voronoi Area they belongs to
		for (int x = 0; x < width; x++) { // for each column in the image boundary
			for (int y = 0; y < height; y++) { // for each row in the image boundary
				n = 0;
				for (int i = 0; i < cells; i++) { // for each of cell point
					if (distance(px[i], x, py[i], y) < distance(px[n], x, py[n], y)) { // find the nearest cell center

						n = i;
					}
				}

				voronoiPointList.get(n).AddPixel(x, y, src.getRGB(x, y)); // adding coordinate to the correspond
																			// collection
			}
		}

		// end of adding pixel

		// Apply the color of each of pixel from collection to the result image
		for (int i = 0; i < voronoiPointList.size(); i++) {

			VoronoiPoint tempVoronoi = voronoiPointList.get(i);
			List<Pixel> pixelCollection = tempVoronoi.getPixelList(); // get the pixel collection of one cell
			List<Pixel> edgeCollection = tempVoronoi.getEdgeList(); // get the pixel collection of one cell

			int rgbValue = tempVoronoi.getColor(); // get the average color value in the area

			for (int j = 0; j < pixelCollection.size(); j++) {
				Pixel temp = pixelCollection.get(j); // get the pixel data
				result.setRGB(temp.getX(), temp.getY(), rgbValue); // set the pixel data to result image
			} // end for

			for (int j = 0; j < edgeCollection.size(); j++) {
				Pixel temp = edgeCollection.get(j); // get the pixel data
				result.setRGB(temp.getX(), temp.getY(), Color.white.getRGB()); // set the pixel data to result image
			} // end for

		} // end for

		// end of applying

		// Graphics2D g = I.createGraphics();

		return result;
	}

	public BufferedImage applyQuadtree(BufferedImage image) {

		BufferedImage result = new BufferedImage(srcImg.getWidth(), srcImg.getHeight(), srcImg.getType());
		Color[][] colors = makeColorArray(image);

		int width = image.getWidth();
		int height = image.getHeight();
		int threshHold = 9;
		QuadTree<Color> quadTree = new QuadTree<Color>(colors, threshHold / 300.0, new Color(0, 0, 0));

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				result.setRGB(i, j, quadTree.get(i, j).getRGB());
			}
		}

		return result;
	}

	// Until functions ======== //

	private Color[][] makeColorArray(BufferedImage image) {

		int width = image.getWidth();
		int height = image.getHeight();

		Color colors[][] = new Color[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				colors[i][j] = new Color(image.getRGB(i, j));
			}
		}

		return colors;
	}

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

	// display================================== //

	public void paint(Graphics g) {
		int w = width/2 ;
		int h = height/2 ;

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
