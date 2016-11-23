import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

/**
 * TCSS 343-HW4
 */

/**
 * This class contains brute-force, divide and conquer and dynamic programming to find cheapest sequence.
 * 
 * @author Sung Lee lbb2812@uw.edu
 * @author Travis Stinebaugh tbs95@uw.edu
 * @author Tzu-Chien Lu tclu82@uw.edu
 * @version Spring 2016
 */
public class tcss343 {  
	
	/** Input file */
	private static final String INPUT = "input.txt";
	
	/** Default input table size */
	private static final int INPUT_TABLE_LENGTH = 10;
	
	/**
	 * Main method
	 * 
	 * @param theArgs is the command line argument
	 * @throws IOException 
	 */
	public static void main(String... theArgs) throws IOException {				
		int length = 0;
		Scanner sc;
		String s;
		int[][] myArr;
		int index = 0;
		int[] inputArray = new int[640000];
		try {
			// generate input file
			generateInputFile();
			
			sc = new Scanner(new File(INPUT));
			
			while (sc.hasNext()) {
				s = sc.next(); 
				if (s.equalsIgnoreCase("NA")) {
					inputArray[index] = -1;
				} else {
					inputArray[index] = Integer.parseInt(s);
				}
				index++;
			}
			length = (int) Math.sqrt(index);
			myArr = new int[length][length];
			index = 0;
			
			// Populate 2d array from the input file
			for (int i = 0; i < length; i++) {
			
				for (int j = 0; j < length; j++) {
					myArr[i][j] = inputArray[index];
					index++;
				}
			}			

			bruteForce(myArr);
			divideAndConquer(myArr);
			dp(myArr);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates input file with random numbers
	 */
	private static void generateInputFile() {
		int[][] inputArray = new int[INPUT_TABLE_LENGTH][INPUT_TABLE_LENGTH];
		Random rand = new Random();
		
		PrintStream inputFile = null;
		try {
			inputFile = new PrintStream(new File("input.txt"));
			for (int i = 0; i < inputArray.length; i++) {
				
				for (int j = 0; j < inputArray.length; j++) {
					
					/** NA or infinity */
					if (i > j) {
						inputFile.print("NA\t");
					} else if (i < j) {
						
						/** Generate random number for testing */
						inputArray[i][j] = rand.nextInt(50) + 1; 
						inputFile.print(inputArray[i][j] + "\t");
						
					} else {
						inputArray[i][j] = 0;
						inputFile.print(inputArray[i][j] + "\t");
					} 
				}
				inputFile.println();
			}	
		} catch (final FileNotFoundException e) {
            System.out.println("File Not Found! " + e);
        } 
	}

	/**
	 * Brute Force method
	 * 
	 * O (2^n)
	 * 
	 * @param theArr is the input array
	 */
	private static void bruteForce(int[][] theArr) {
		long start = System.currentTimeMillis();
		ArrayList<Integer> al = bruteForceHelper(theArr);
		int result = bfCheapest(al, theArr);		
		long duartion = System.currentTimeMillis() - start;
		
		System.out.print("The Path for Brute Force is 1->");
		
		if (result < theArr[0][theArr.length-1]) {		
				
			for (Integer i : al) System.out.print(i+1 + "->");
		} 
		else result = theArr[0][theArr.length-1];		
		
		System.out.print(theArr.length);
		System.out.println("\nBrute Force result is " + result);
		System.out.println("It took " + duartion + " ms.\n");
	}
		
	/**
	 * Brute Force helper method
	 * 
	 * O (2^n)
	 * 
	 * @param theArr is the input array
	 * @return ArrayList<Integer>
	 */
	private static ArrayList<Integer> bruteForceHelper(int[][] theArr) {
		ArrayList<Integer> index = new ArrayList<>();
		int i, min = theArr[0][theArr.length-1];
		
		for (i = 1; i < theArr.length-1; i++) index.add(i);		
		
		ArrayList<ArrayList<Integer>> path = new ArrayList<>();
		
		/** Initialize inner array list*/
		path.add(new ArrayList<Integer>());
		
		for (Integer x : index) {
			ArrayList<ArrayList<Integer>> newPath = new ArrayList<>();
			
			for (ArrayList<Integer> al : path) {
				newPath.add(al);
				ArrayList<Integer> newList = new ArrayList<>(al);
				newList.add(x);
				newPath.add(newList);
				int temp = bfCheapest(newList, theArr);
					
				if (temp < min) index = newList;
		
				min = Math.min(temp, min);
			}
			path = newPath;
		}		
		return index;
	}

	/**
	 * Calculate BF cheapest 
	 * 
	 * @param theList is the list
	 * @param theArr is the input array
	 * @return cheapest value
	 */
	private static int bfCheapest(ArrayList<Integer> theList, int[][] theArr) {
		int j = 0, cheapest = 0;
		
		for (int i = 0; i < theList.size(); i++) {
			cheapest += theArr[j][theList.get(i)];
			j = theList.get(i);
		}
		cheapest += theArr[j][theArr.length-1];
		
		return cheapest;
	}
	
	/**
	 * Divide and Conquer method
	 * 
	 * @param theArr is the input array
	 */
	private static void divideAndConquer(int[][] theArr) {
		ArrayList<Integer> possible_values = new ArrayList<Integer>();
		ArrayList<String> paths = new ArrayList<String>();
		long start = System.currentTimeMillis();
		dcHelper(theArr, 0, theArr.length, 0, "", possible_values, paths);
		int result = Collections.min(possible_values);
		String theCheapestPath = paths.get(possible_values.indexOf(result));
		
		long duartion = System.currentTimeMillis() - start;
		System.out.print("The Path for Divide and Conquer is " + theCheapestPath);
		System.out.println("\nDivide and Conquer result is " + result);
		System.out.println("It took " + duartion + " ms.\n");
		
	}
	
	/**
	 * Divide and conquer helper method
	 * 
	 * @param theArr is the input array
	 * @param start is the start
	 * @param end is the end
	 * @param current_count is the current count
	 * @param thePath is the path
	 * @param thePVs is the possible values
	 * @param thePaths is the path
	 */
	private static void dcHelper(int[][] theArr, int start, int end, int current_count, 
			                     String thePath, ArrayList<Integer> thePVs, ArrayList<String> thePaths) {
		
		for(int i = start + 1; i < end; i++){
			int currentStart = start +1;
			int currentEnd = i +1;
			dcHelper(theArr, i, end, current_count + theArr[start][i], thePath + 
					 currentStart + "->" + currentEnd + "   ", thePVs, thePaths);
		}
		
		if (start + 1 == end) {
			thePVs.add(current_count);
			thePaths.add(thePath);
		}
	}
	
	/**
	 * Dynamic programming method
	 * 
	 * O (n^2)
	 * 
	 * @param theArr is the input array
	 */
	private static void dp(int[][] theArr) {
		int [] shortest = new int[theArr.length];
		ArrayList<Integer> path = new ArrayList<>();
		long start = System.currentTimeMillis();
		int min = dpHelper(theArr, shortest, path);
		long duration = System.currentTimeMillis() - start;
		System.out.println("\nDynamic Programming result is " + min);
		System.out.println("It took " + duration + " ms.\n");
	}
	
	/**
	 * DP helper method
	 * 
	 * O (n^2)
	 * 
	 * @param theArr is the input array
	 * @param theCheapest is the cheapest array
	 * @param thePath is the path	
	 * @return int is the minimum result
	 */
	private static int dpHelper(int[][] theArr, int[] theCheapest, ArrayList<Integer> thePath) {
		int i, j, temp, index = theCheapest.length -1;
		
		for (i = 1; i < theArr.length; i++) {
			/** filled with max integer */
			theCheapest[i] = Integer.MAX_VALUE;
			
			for (j = 0; j < i; j++) {
				temp = theCheapest[j] + theArr[j][i];
				
				if (temp < theCheapest[i]) theCheapest[i] = temp;		
			}
		}
		/** Add index to shortest path */
		thePath.add(index);
		showPath(index, theCheapest, theArr, thePath);
		return theCheapest[theArr.length - 1];
	}
	
	/**
	 * Print DP's cheapest path
	 * 
	 * @param theIndex is the index
	 * @param theCheapest is the cheapest array
	 * @param theArr is the input array
	 * @param thePath is the path
	 */
	private static void showPath (int theIndex, int[] theCheapest, int[][] theArr, ArrayList<Integer> thePath) {
		int i, j;
		for(i = theIndex; i >= 0; i--) {
			/** Make sure repeat from j to index */
			i = theIndex;
			
			for(j = 0; j <= i; j++) {
				/** Find the cheapest cost and store the index */
				if(theCheapest[i] == theCheapest[j] + theArr[j][i]) {
					thePath.add(j);
					i = j;
					theIndex = j;
				}
			}
		}
		
		/** Print the shortest path */
		System.out.print("\nThe Path for Dynamic Programming is: ");
		
		for(theIndex = thePath.size() - 1; theIndex >= 0 ; theIndex--) {
			
			if (theIndex == 0) System.out.print(thePath.get(theIndex) + 1);
			
			else System.out.print(thePath.get(theIndex) + 1 + "->");
		}
	}
}
