import java.awt.Color;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

//A general purpose QuadTree.

public class QuadTree<T> {
	private ImageMeasure measure;
	private double threshold;
	public Node root;
	public Color defaultValue;

	public ArrayList<Pixel> centerPointCollection = new ArrayList<Pixel>();

	public QuadTree(Color[][] data, double threshold, Color defaultValue) {
		this.measure = new ImageMeasure();
		this.threshold = threshold;
		this.defaultValue = defaultValue;
		root = new Node(data, 0, 0, data.length, data[0].length);
	}

	// Gets the pixel data at the given coordinate.
	public Color get(int i, int j) {
		return root.get(i, j);
	}
	
	public Color getFrame(int i, int j) {
		return root.getFrame(i, j);
	}

	// A QuadTree node.
	public class Node {
		public Color value;
		public Object children[];
		public int x, y;
		public int width, height;

		public Node(Color data[][], int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;

			if (((width < 20) || (height < 20) || (measure.measureDetail(data, x, y, width, height) < threshold))) {
				value = measure.approximate(data, x, y, width, height);

				// add the center point to the list
				Pixel tem = new Pixel(x + width / 2, y + height / 2);
				centerPointCollection.add(tem);

			} else {
				children = new Object[4];
				//create 4 children as the collection of four sub square
				children[0] = new Node(data, x, y, width / 2, height / 2);
				children[1] = new Node(data, x + width / 2, y, width - width / 2, height / 2);
				children[2] = new Node(data, x, y + height / 2, width / 2, height - height / 2);
				children[3] = new Node(data, x + width / 2, y + height / 2, width - width / 2, height - height / 2);
			}
		}

		//get the color of the pixel and get it's rgb value
		public Color get(int i, int j) {
			if (value == null) { // if this is not a leaf
				if (i < x + width / 2) {
					if (j < y + height / 2) {
						return ((Node) children[0]).get(i, j);
					} else {
						return ((Node) children[2]).get(i, j);
					}
				} else {
					if (j < y + height / 2) {
						return ((Node) children[1]).get(i, j);
					} else {
						return ((Node) children[3]).get(i, j);
					}
				}
			} else {
				if (((i == x) || (j == y)) && (defaultValue != null)) // if the pixel is on the edge
					return defaultValue;
				else
					return value;
			}
		}
		
		public Color getFrame(int i, int j) {
			if (value == null) { // if this is not a leaf
				if (i < x + width / 2) {
					if (j < y + height / 2) {
						return ((Node) children[0]).get(i, j);
					} else {
						return ((Node) children[2]).get(i, j);
					}
				} else {
					if (j < y + height / 2) {
						return ((Node) children[1]).get(i, j);
					} else {
						return ((Node) children[3]).get(i, j);
					}
				}
			} else {
				if (((i == x) || (j == y))) // if the pixel is on the edge
					return new Color(255,255,255);
				else
					return new Color(0,0,0);
			}
		}

	}
}