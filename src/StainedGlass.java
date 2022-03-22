import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;

import javax.imageio.ImageIO;

public class StainedGlass extends Frame{
	
	BufferedImage src;

	BufferedImage finalResult;

	int width, width1;
	int height, height1;
	
	// constructor
			// Get an image from the specified file in the current directory on the
			// local hard disk.
	public StainedGlass() {try {
				src = ImageIO.read(new File("test.jpg"));

			} catch (Exception e) {
				System.out.println("Cannot load the provided image");
			}
			this.setTitle("Stained Glass Filter");
			this.setVisible(true);

			width = src.getWidth();
			height = src.getHeight();


			//finalResult = over(shadedStatue, statueMatte, backgroundImg);

			// Anonymous inner-class listener to terminate program
			this.addWindowListener(new WindowAdapter() {// anonymous class definition
				public void windowClosing(WindowEvent e) {
					System.exit(0);// terminate the program
				}// end windowClosing()
			}// end WindowAdapter
			);// end addWindowListener
		}// end constructor



		public BufferedImage colorCorrect(BufferedImage src, BufferedImage bg) {
			BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());

			for (int x = 0; x < src.getWidth(); x++) {
				for (int y = 0; y < src.getHeight(); y++) {
					int bg_rgb = bg.getRGB(x, y);
					int src_rgb = src.getRGB(x, y);
					float[] bg_hsb = Color.RGBtoHSB(getRed(bg_rgb), getGreen(bg_rgb), getBlue(bg_rgb), null);
					float[] src_hsb = Color.RGBtoHSB(getRed(src_rgb), getGreen(src_rgb), getBlue(src_rgb), null);

					int corrected = Color.HSBtoRGB(bg_hsb[0], bg_hsb[1], src_hsb[2]);

					result.setRGB(x, y, new Color(corrected).getRGB());
				}
			}

			return result;
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

		public void paint(Graphics g) {
			int w = width / 2; // door image
			int h = height / 2;


			this.setSize(w * 2 + 100, h * 2 + 50);

			g.setColor(Color.BLACK);
			Font f1 = new Font("Verdana", Font.PLAIN, 13);
			g.setFont(f1);

			g.drawImage(src, 20, 50, w, h, this);
			g.drawString("Before", 20, 50 + h + 30);
			
			g.drawImage(src, 50 + w, 50, w, h, this);
			g.drawString("After", 50 + w, 50 + h + 30);

		}
		// =======================================================//

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StainedGlass img = new StainedGlass();// instantiate this object
		img.repaint();// render the image

	}

}
