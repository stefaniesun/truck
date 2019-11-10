package xyz.util;

import java.io.File;

public class Test {

	public static void main(String[] args) {
		
		File file=new File("blob:http://192.168.1.2:8080/af58dbe0-332b-46fb-8e8b-c34e7cd25d45");
		
		System.out.println(file.length());

	}

}
