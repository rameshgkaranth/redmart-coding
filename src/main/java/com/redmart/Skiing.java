/**
 * 
 */
package com.redmart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that finds the longest and largest drop by reading
 * altitude data for a ski range
 * 
 * @author rkaranth
 *
 */
public class Skiing {

	private static int altitudeArr[][];
	private static int arrXLength = 0, arrYLength = 0;
	private static int longestSkiPath[];
	private static int dp[][];
	private static final String COMMA = ",";
	
	/**
	 * Gets the longest path from current position
	 * 
	 * @param x
	 * @param y
	 * @return
	 * 		longest path as an int[]
	 */
	public static int[] getLongestSkiPathFromCurrentPos(int x, int y) {
		int[] skiPath = dp[getKeyFromIndex(x, y)];
		if (null != skiPath) {
			return skiPath;
		}
		
		List<String> coordinatesList = getPossibleSkiPathsFromCurrentPos(x, y);
		int[][] tempSkiPaths = new int[4][];
		int[] tempLongestSkiPath = new int[0];
		int counter = 0;
		
		if (!coordinatesList.isEmpty()) {
			for (String coordinates : coordinatesList) {
				int c1 = Integer.parseInt(coordinates.split(COMMA)[0]);
				int c2 = Integer.parseInt(coordinates.split(COMMA)[1]);
				tempSkiPaths[counter++] = getLongestSkiPathFromCurrentPos(c1, c2);
			}
			
			tempLongestSkiPath = getLongestSkiPathFromPossibleSolutions(tempSkiPaths);
		} 

		int key = getKeyFromIndex(x, y);
		dp[key] = appendCurrentPosToSkiPath(x, y, tempLongestSkiPath);
		
		
		return dp[key];
	}
	
	/**
	 * Method to get the longest path from a set of paths
	 * 
	 * @param paths
	 * @return
	 * 		longest path from a possible paths as an int[]
	 */
	public static int[] getLongestSkiPathFromPossibleSolutions(int[][] paths) {
		int[] longSkiPath = new int[0];
		
		for (int i = 0; i < paths.length; i++) {
			longSkiPath = compateSkiPaths(paths[i], longSkiPath);
		}
		
		return longSkiPath;
	}
	
	/**
	 * Method to compare 2 ski paths. <br>
	 * Comparison rules are below<br>
	 * <li>Lengths are compares for path1 & path2
	 * <li>If lengths are same then the drop is compared
	 * 
	 * @param path1
	 * @param path2
	 * @return
	 * 		Returns path1 if largest, else path2
	 */
	public static int[] compateSkiPaths(int[] path1, int[] path2) {
		if (null == path1 || path1.length == 0)
			return path2;
		
		if (path1.length == path2.length) {
			int diff1 = path1[path1.length - 1] - path1[0];
			int diff2 = path2[path2.length - 1] - path2[0];
			
			return (diff1 > diff2 ? path1 : path2);
		}
		
		return (path1.length > path2.length ? path1 : path2);
	}
	
	/**
	 * Method to identify possible neighbors from current position
	 * 
	 * @param x
	 * @param y
	 * @return
	 * 		Returns a List of String with comma separated co-ordinates
	 */
	public static List<String> getPossibleSkiPathsFromCurrentPos(int x, int y) {
		List<String> coordsOfPossiblePaths = new ArrayList<>();
		StringBuilder builder = null;
		
		int altitudeAtCurrentPos = altitudeArr[x][y];
		
		int x1 = -1, x2 = -1, x3 = -1, x4 = -1;
		int y1 = -1, y2 = -1, y3 = -1, y4 = -1;
		
		//Various combinations of positions in altitude array
		if (x == 0 && y == 0) {
			x1 = 1; y1 = 0; x2 = 0; y2 = 1;
		} else if (x == 0  && y > 0 && y < arrYLength - 1) {
			x1 = 0; y1 = y - 1; x2 = x + 1; y2 = y; x3 = x; y3 = y + 1;
		} else if (x == 0 && y == arrYLength - 1) {
			x1 = 0; y1 = y - 1; x2 = x + 1; y2 = y;
		} else if (y == 0 && x > 0 && x < arrXLength - 1) {
			x1 = x - 1; y1 = 0; x2 = x; y2 = y + 1; x3 = x + 1; y3 = 0;
		} else if (y == 0 && x == arrXLength - 1) {
			x1 = x - 1; y1 = y; x2 = x; y2 = y + 1;
		} else if (x > 0 && x < arrXLength - 1 && y > 0 && y < arrYLength - 1) {
			x1 = x - 1; y1 = y; x2 = x; y2 = y - 1; x3 = x; y3 = y + 1; x4 = x + 1; y4 = y;
		} else if (x == arrXLength - 1 && y > 0 && y < arrYLength - 1) {
			x1 = x; y1 = y - 1; x2 = x - 1; y2 = y; x3 = x; y3 = y + 1;
		} else if (y == arrYLength - 1 && x > 0 && x < arrXLength - 1) {
			x1 = x - 1; y1 = y; x2 = x; y2 = y - 1; x3 = x + 1; y3 = y;
		} else if (y == arrYLength - 1 && x == arrXLength - 1) {
			x1 = x; y1 = y - 1; x2 = x - 1; y2 = y;
		}
		
		//A max of 4 neighbors are possible, check each one to see if
		//we can ski downhill
		if (x1 >= 0 && y1 >= 0 && altitudeAtCurrentPos > altitudeArr[x1][y1]) {
			builder = new StringBuilder();
			coordsOfPossiblePaths.add(
					builder.append(x1).append(COMMA).append(y1).toString());
		}
		
		if (x2 >= 0 && y2 >=0 && altitudeAtCurrentPos > altitudeArr[x2][y2]) {
			builder = new StringBuilder();
			coordsOfPossiblePaths.add(
					builder.append(x2).append(COMMA).append(y2).toString());
		}
		
		if (x3 >= 0 && y3 >= 0 && altitudeAtCurrentPos > altitudeArr[x3][y3]) {
			builder = new StringBuilder();
			coordsOfPossiblePaths.add(
					builder.append(x3).append(COMMA).append(y3).toString());
		}
		
		if (x4 >= 0 && y4 >= 0 && altitudeAtCurrentPos > altitudeArr[x4][y4]) {
			builder = new StringBuilder();
			coordsOfPossiblePaths.add(
					builder.append(x4).append(COMMA).append(y4).toString());
		}
		
		return coordsOfPossiblePaths;
	}
	
	/**
	 * Method to append current position to ski path. This gives
	 * the ski path if we started from this position provided by 
	 * co-ordinates x, y
	 * 
	 * @param x
	 * @param y
	 * @param skiPath
	 * @return
	 * 		an int[] representing the ski path
	 */
	public static int[] appendCurrentPosToSkiPath(int x, int y, int[] skiPath) {
		int[] path = new int[skiPath.length + 1];
		
		for (int i = 0; i < skiPath.length; i++) {
			path[i] = skiPath[i];
		}
		
		path[skiPath.length] = altitudeArr[x][y];
		
		return path;
	}
	
	/**
	 * This method is used to generate key for storing intermediate 
	 * path results in a map.
	 * If x == 0, then key = y
	 * Else ((x * arrXLength) + (y + 1) - 1)
	 * 
	 * @param x
	 * @param y
	 * @return
	 * 		an int representing a key for a position x, y
	 */
	public static int getKeyFromIndex(int x, int y) {
		if (x == 0) {
			return y;
		} else {
			return (x * arrXLength + (y + 1)) - 1;
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		//Read altitude data from file
		altitudeArr = AltitudeFileReader.readAltitudeDataFromFile();
		
		//Set x, y lengths of array
		arrXLength = altitudeArr.length;
		arrYLength = altitudeArr[0].length;
		
		//Lets say longest ski path as of now is 0
		longestSkiPath = new int[0];
		
		//A map to store intermediate results
		dp = new int[arrXLength * arrYLength][];
		
		//Iterate for each element and get ski path for each position
		for (int x = 0; x < arrXLength; x++) {
			for (int y= 0; y < arrYLength; y++) {
				int[] longestSkiPathFromCurrentPos = getLongestSkiPathFromCurrentPos(x, y);
				
				//Compare with previously evaluated ski path to store the largest
				longestSkiPath = compateSkiPaths(longestSkiPathFromCurrentPos, longestSkiPath);
			}
		}
		
		for (int x = longestSkiPath.length - 1; x >= 0; x--) {
			System.out.println(longestSkiPath[x]);
		}
		
		System.out.println("LENGTH : "+longestSkiPath.length);
		System.out.println("DROP : "+(longestSkiPath[longestSkiPath.length - 1] - longestSkiPath[0]));
		
	}
	
}
