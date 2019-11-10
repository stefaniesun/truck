package xyz.util;

import java.awt.Color;

public class ColorUtil {

	 public static Color toColorFromString(String colorStr){  
	        colorStr = colorStr.substring(4);  
	        Color color =  new Color(Integer.parseInt(colorStr, 16)) ;  
	        //java.awt.Color[r=0,g=0,b=255]  
	        return color;  
	    }  
}
