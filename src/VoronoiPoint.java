import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class VoronoiPoint 
	{
		
	    private int xOffset = 0;
	    private int yOffset = 0;

	    private int blueTotal = 0; 
	    private int greenTotal = 0; 
	    private int redTotal = 0; 
	    
	    private int blueAverage = 0; 
	    private int greenAverage = 0; 
	    private int redAverage = 0; 
	    
	    public VoronoiPoint(int x, int y) {
	    	this.xOffset = x;
	    	this.yOffset = y;
	    }
	    

	    public void CalculateAverages() 
	    {
	        if (pixelCollection.size() > 0) 
	        {
	            blueAverage = blueTotal / pixelCollection.size(); 
	            greenAverage = greenTotal / pixelCollection.size(); 
	            redAverage = redTotal / pixelCollection.size(); 
	        }
	    }


	    private List<Pixel> pixelCollection = new ArrayList<Pixel>(); 


	    public void AddPixel(Pixel pixel) 
	    { 
	        blueTotal += new Color(pixel.getColor()).getBlue(); 
	        greenTotal += new Color(pixel.getColor()).getGreen();  
	        redTotal += new Color(pixel.getColor()).getRed();  

	    } 
	    
	}

