package com.redmart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AltitudeFileReader {
	
	/**
	 * Method to read altitude data from a File
	 * 
	 * @return
	 * 		Altitude data as int[][] 
	 * @throws IOException
	 */
	public static int[][] readAltitudeDataFromFile() throws IOException {
		final String SPACE = " ";
		final String FILE_LOC = "/Users/rkaranth/Desktop/altitude.txt";
		File file = new File(FILE_LOC);
		BufferedReader bufferedReader = null;
		int[][] altitudeArr = null;
		List<String> altitudeStrList = null;
		
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			String s = bufferedReader.readLine();
			int x = Integer.parseInt(s.split(SPACE)[0]);
			int y = Integer.parseInt(s.split(SPACE)[1]);
			
			altitudeArr = new int[x][y];
			altitudeStrList = new ArrayList<>(x);
			
			while ((s = bufferedReader.readLine()) != null) {
				altitudeStrList.add(s);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File could not be found at : "+FILE_LOC);
		} catch (IOException e) {
			System.out.println("Exception occured while reading file at : "+FILE_LOC);
		} finally {
			if (null != bufferedReader) {
				bufferedReader.close();
			}
		}
		
		//Fall back test array in case file not found 
		if (null == altitudeStrList) {
			int[][] testArr = {
				{4, 8, 7, 3},
				{2, 5, 9, 3},
				{6, 3, 2, 5},
				{4, 4, 1, 6}
			};
			altitudeArr = testArr;
			return altitudeArr;
		}
		
		//File was found and its contents were read
		int i = 0, j = 0;
		for (String lineItem : altitudeStrList) {
			String[] sElements = lineItem.split(SPACE);
			
			for (String sElement : sElements) {
				altitudeArr[i][j++] = Integer.parseInt(sElement);
			}
			i++; j = 0;
		}
		
		//Return the altitude data as array of arrays
		return altitudeArr;
	}
}
