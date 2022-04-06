

public class Pixel {
	
	 private int xOffset = 0; 
	 private int yOffset = 0; 
	
	public Pixel(int x, int y) {
    	this.xOffset = x;
    	this.yOffset = y;
    }

	public int getX() {
		return xOffset;
	}

	public int getY() {
		return yOffset;
	}	
}
