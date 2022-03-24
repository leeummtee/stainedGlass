import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class VoronoiPoint 
	{
		
	    private int blueTotal = 0; 
	    private int greenTotal = 0; 
	    private int redTotal = 0; 
	    
	    private int blueAverage = 0; 
	    private int greenAverage = 0; 
	    private int redAverage = 0;
	    
	    private List<Pixel> pixelCollection = new ArrayList<Pixel>(); 
	    

	    private void CalculateAverages() 
	    {
	        if (pixelCollection.size() > 0) 
	        {
	            blueAverage = blueTotal / pixelCollection.size(); 
	            greenAverage = greenTotal / pixelCollection.size(); 
	            redAverage = redTotal / pixelCollection.size(); 
	        }
	    }
	    
	    public int getColor() {
	    	this.CalculateAverages();
			return new Color(redAverage, greenAverage, blueAverage).getRGB();
	    }
	    
	    public List<Pixel> getPixelList(){
	    	return pixelCollection;
	    }


	    public void AddPixel(int x, int y, int rgb) 
	    { 
	        blueTotal += new Color(rgb).getBlue(); 
	        greenTotal += new Color(rgb).getGreen();  
	        redTotal += new Color(rgb).getRed();
	        
	        Pixel p = new Pixel(x,y);
	        pixelCollection.add(p);

	    } 
	    
	}

