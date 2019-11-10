package xyz.util;

import java.math.BigDecimal;

public class NumberUtil {

	 public static boolean isNum(String str) {
	        try {
	            new BigDecimal(str);
	            return true;
	        } catch (Exception e) {
	            return false;
	        }
	    }
}
