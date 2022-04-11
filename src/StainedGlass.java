import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class StainedGlass extends Frame {

	BufferedImage srcImg;
	BufferedImage saveImage;
	BufferedImage finalResult;

	int width, height;

	static BufferedImage I;
	static int px[], py[], color[], cells = 1000;
//	int chooseFilter = 1; // 0 is glass, 1 is quadtree
	public int chooseFilter;
//	int threshHold = 20;
	int minThreshHold = 10;
	public int threshHold = 20;
	int maxThreshHold = 50;
	
	int minIteration = 1;
	public int iteration = 10;
	int maxIteration = 15;

	public Color[][] colors;
	public QuadTree<Color> quadTree;
	
	SliderUI sliderUI;
	
	// constructor
	// Get an image from the specified file in the current directory on the
	// local hard disk.
	public StainedGlass() {
		try {
//			srcImg = ImageIO.read(new File("test3.jpg"));
			loadImage();

		} catch (Exception e) {
			System.out.println("Cannot load the provided image");
		}
		this.setTitle("Stained Glass Filter");
		this.setVisible(true);
		
		width = srcImg.getWidth();
		height = srcImg.getHeight();

		// Initialize section===========
		colors = makeColorArray(srcImg);
		quadTree = new QuadTree<Color>(colors, threshHold / 300.0, new Color(0, 0, 0));
		// ===================The end ==============

		checkFilter();
		
		sliderUI = new SliderUI(this);
		sliderUI.display();

		// Anonymous inner-class listener to terminate program
		this.addWindowListener(new WindowAdapter() {// anonymous class definition
			public void windowClosing(WindowEvent e) {
				System.exit(0);// terminate the program
			}// end windowClosing()
		}// end WindowAdapter
		);// end addWindowListener
	}// end constructor
	
	public void checkFilter() {
		if (chooseFilter == 0) {
			try {
				finalResult = stainedGlassFilter(srcImg);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} // apply the filter to the image
		}

		else if (chooseFilter == 1) {
			try {
				finalResult = applyQuadtree(srcImg);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} // apply the filter to the image
		}
	}
	
	 //refresh the frame
    public void refresh() {
		try {
		       finalResult = deepCopy(saveImage);
		   }
		   catch (Exception e) {
		       System.out.println("cannot load image");
		   }
	   checkFilter();
	   repaint();
	}

	 //load user image
    public void loadImage() {
	   try {
	           JFileChooser chooser = new JFileChooser();
	           FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png");
	           chooser.setFileFilter(filter);
	           int returnVal = chooser.showOpenDialog(null);
	           if (returnVal == JFileChooser.APPROVE_OPTION) {
	               srcImg = deepCopy(ImageIO.read(chooser.getSelectedFile()));
	       }
	           saveImage = deepCopy(ImageIO.read(chooser.getSelectedFile()));
	       this.setSize(width, height);
	   } catch (Exception e) {
	       e.printStackTrace();
	   }
	}
    
    //save image
    public boolean exportImage(Component component) {
        Rectangle rect = component.getBounds();

        try {
            String format = "png";
            String fileName = "ExportedImage." + format;
            BufferedImage captureImage = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_ARGB);
            component.paint(captureImage.getGraphics());
            ImageIO.write(captureImage, format, new File(fileName));
            System.out.printf("Image exported");
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    //making a deep copy of images
    static BufferedImage deepCopy(BufferedImage bi) {
    	   ColorModel cm = bi.getColorModel();
    	   boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    	   WritableRaster raster = bi.copyData(null);
    	   return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public BufferedImage stainedGlassFilter(BufferedImage src) throws IOException {
		BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
		int n = 0;
		List<VoronoiPoint> voronoiPointList = new ArrayList<VoronoiPoint>();
		List<Pixel> centerList = quadTree.centerPointCollection;

		for (int it = 0; it < iteration; it++) {
			// initiate Lists
			voronoiPointList = new ArrayList<VoronoiPoint>();
			for (int i = 0; i < centerList.size(); i++) {
				voronoiPointList.add(new VoronoiPoint());
			}

			// Categorize each of pixel depending on the Voronoi Area they belongs to
			for (int x = 0; x < width; x++) { // for each column in the image boundary
				for (int y = 0; y < height; y++) { // for each row in the image boundary
					n = 0;
					for (int i = 0; i < centerList.size(); i++) { // for each of cell point
						if (distance(centerList.get(i).getX(), x, centerList.get(i).getY(),
								y) < distance(centerList.get(n).getX(), x, centerList.get(n).getY(), y)) { // find the
																											// nearest
																											// cell
																											// center

							n = i;
						}
					}
					voronoiPointList.get(n).AddPixel(x, y, src.getRGB(x, y)); // adding coordinate to the correspond															// collection
				}
			}

			// make a new center list based on area
			centerList = makeCenterList(voronoiPointList);
			System.out.print("iteration:" + it +"\n");
		}

		// Apply the color of each of pixel from collection to the result image
		for (int i = 0; i < voronoiPointList.size(); i++) {

			VoronoiPoint tempVoronoi = voronoiPointList.get(i);
			List<Pixel> pixelCollection = tempVoronoi.getPixelList(); // get the pixel collection of one cell
			List<Pixel> edgeCollection = tempVoronoi.getEdgeList(); // get the edge collection of one cell

			int rgbValue = tempVoronoi.getColor(); // get the average color value in the area

			for (int j = 0; j < pixelCollection.size(); j++) {
				Pixel temp = pixelCollection.get(j); // get the pixel data
				result.setRGB(temp.getX(), temp.getY(), rgbValue); // set the pixel data to result image
			} // end for

			for (int j = 0; j < edgeCollection.size(); j++) {
				Pixel temp = edgeCollection.get(j); // get the pixel data
				result.setRGB(temp.getX(), temp.getY(), Color.white.getRGB()); // set the pixel data to result image
			} // end for

		} //end for

		//end of applying
		ImageIO.write(result, "png", new File("glasseffect" + ".png"));

		// Graphics2D g = I.createGraphics();
		return result;
	}

	// Quad Tree !!!!!
	public BufferedImage applyQuadtree(BufferedImage image) throws IOException {

		BufferedImage result = new BufferedImage(srcImg.getWidth(), srcImg.getHeight(), srcImg.getType());

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				result.setRGB(i, j, quadTree.get(i, j).getRGB());
			}
		}
		ImageIO.write(result, "png", new File("normal_quad" + threshHold + ".png"));

		Graphics2D g = result.createGraphics();
		g.setColor(Color.BLACK);

		/*
		for (int i = 0; i < (quadTree.centerPointCollection).size(); i++) {
			Pixel temp = (quadTree.centerPointCollection).get(i);
			g.fill(new Ellipse2D.Double(temp.getX() - 2.5, temp.getY() - 2.5, 5, 5));
		}*/
		
		return result;
	}

	// Until functions ======== //

	private Color[][] makeColorArray(BufferedImage image) {

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
		return d;
	}

	private List<Pixel> makeCenterList(List<VoronoiPoint> li) {
		List<Pixel> result = new ArrayList<Pixel>();
		for (int i = 0; i < li.size(); i++) {
			result.add(li.get(i).getCenter());
		}

		return result;
	}
	
	public void createNewQuadTree() {
		quadTree = new QuadTree<Color>(colors, threshHold / 300.0, new Color(0, 0, 0));
	}

	// display================================== //

	public void paint(Graphics g) {

		
		int w = 500;
		int h = height * w/width;
		//System.out.println(height);
		
		if(width > height) {
			w = 500;
			h = height * w/width;
			//System.out.println(height);
		}
		
		else if(width <= height) {
			h = 600;
			w = width * h/height;
			//System.out.println(height);
		}

		this.setSize(w * 2 + 100, h + 100);

		g.setColor(Color.BLACK);
		Font f1 = new Font("Verdana", Font.PLAIN, 13);
		g.setFont(f1);

		g.drawImage(srcImg, 20, 50, w, h, this);
		g.drawString("Before", 20, 50 + h + 20);

		g.drawImage(finalResult, 50 + w, 50, w, h, this);
		g.drawString("After", 50 + w, 50 + h + 20);

	}
	// =======================================================//

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StainedGlass img = new StainedGlass();// instantiate this object
		img.repaint();// render the image

	}

}
