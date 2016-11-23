/**
 * TCSS 343-HW4
 */

/**
 * This class is for bonus challenge
 * 
 * @author Sung Lee
 * @author Travis Stinebaugh
 * @author Tzu-Chien Lu tclu82@uw.edu
 * @version Spring 2016
 */
public class challenge {

	
	/**
	 * Main method
	 * 
	 * @param theArgs
	 */
	public static void main(String[] theArgs) {
		int n = Integer.parseInt(theArgs[0]);
		boolean[][] stones = new boolean[n][n];
		for(int i = 2; i <= 2 * Integer.parseInt(theArgs[1]); i+= 2) {
			stones[Integer.parseInt(theArgs[i])][Integer.parseInt(theArgs[i + 1])] = true;
		}
		for(boolean[] b : stones) {
			for(boolean bool : b) {
				System.out.print(bool + "\t");
			}
			System.out.println();
		}
	}
}
