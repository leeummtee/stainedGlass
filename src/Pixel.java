

public class Pixel {
	
	 private int xOffset = 0; 
	 private int yOffset = 0; 
	 private int rgb = 0;
	
	public Pixel(int x, int y, int c) {
    	this.xOffset = x;
    	this.yOffset = y;
    	this.rgb = c;
    }

	public int getColor() {
		return rgb;
	}
	
	public int getX() {
		return xOffset;
	}

	public int getY() {
		return yOffset;
	}	
}
