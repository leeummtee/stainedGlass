import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class VoronoiPoint {

	private int blueTotal = 0;
	private int greenTotal = 0;
	private int redTotal = 0;

	private int blueAverage = 0;
	private int greenAverage = 0;
	private int redAverage = 0;
	
	private int xCount=0;
	private int yCount=0;
	private int xTotal=0;
	private int yTotal=0;

	private List<Pixel> pixelCollection = new ArrayList<Pixel>();// pixels of voronoi cell

	private List<Pixel> edgeCollection = new ArrayList<Pixel>();

	private void CalculateAverages() {
		if (pixelCollection.size() > 0) {
			blueAverage = blueTotal / pixelCollection.size();
			greenAverage = greenTotal / pixelCollection.size();
			redAverage = redTotal / pixelCollection.size();
		}
	}

	public int getColor() {
		this.CalculateAverages();
		return new Color(redAverage, greenAverage, blueAverage).getRGB();
	}

	
	public List<Pixel> getPixelList() {
		return pixelCollection;
	}

	
	//get edge list of the pixel collection
	public List<Pixel> getEdgeList() {
		int smallestY = 999999999;
		int biggestY = 0;

		int smallestX = 999999999;
		int biggestX = 0;

		// get the extreme value
		for (Pixel p : pixelCollection) {

			// get extreme Y value
			if (p.getY() < smallestY) {
				smallestY = p.getY();
			}
			if (p.getY() > biggestY) {
				biggestY = p.getY();
			}

			// get extreme X value
			if (p.getX() < smallestX) {
				smallestX = p.getX();
			}
			if (p.getX() > biggestX) {
				biggestX = p.getX();
			}
		}

		// get the horizontal border
		for (int i = smallestY; i <= biggestY; i++) {

			int smallX = 999999999;
			int bigX = 0;
			for (Pixel p : pixelCollection) {
				if (p.getY() == i) {
					if (p.getX() < smallX) {
						smallX = p.getX();
					} // end if
					if (p.getX() > bigX) {
						bigX = p.getX();
					} // end if

				} // end if
			} // end for

			// add the pixel of border to edge list
			edgeCollection.add(new Pixel(smallX, i));
			if (bigX != smallX)
				edgeCollection.add(new Pixel(bigX, i));
		} // end for

		// get the vertical border
		for (int i = smallestX; i <= biggestX; i++) {

			int smallY = 999999999;
			int bigY = 0;
			for (Pixel p : pixelCollection) {
				if (p.getX() == i) {
					if (p.getY() < smallY) {
						smallY = p.getY();
					} // end if
					if (p.getY() > bigY) {
						bigY = p.getY();
					} // end if

				} // end if
			
			} // end for

			// add the pixel of border to edge list
			edgeCollection.add(new Pixel(i, smallY));
			if (bigY != smallY)
				edgeCollection.add(new Pixel(i, bigY));
		} // end for

		return edgeCollection;
	}

	
	public Pixel getCenter() {
		return (new Pixel(xTotal / xCount, yTotal / yCount));
	}
	
	// add pixel to the pixel list and add the rgb value to total
	public void AddPixel(int x, int y, int rgb) {
		blueTotal += new Color(rgb).getBlue();
		greenTotal += new Color(rgb).getGreen();
		redTotal += new Color(rgb).getRed();

		Pixel p = new Pixel(x, y);
		pixelCollection.add(p);
		
		
		//Cumulate X Y value for center point
		xCount++;
		yCount++;		
		xTotal += x;
		yTotal += y;

	}

}
