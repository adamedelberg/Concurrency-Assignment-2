
// Driver class for median filter assignment
// EDLADA002
// August 2015

import java.io.IOException;
import java.util.Scanner;

public class Driver {

	private static Scanner scannerInput;
	private static String fileIn;
	private static String filter;
	private static String fileOut;
	private static int filterSize;

	public static void main(String[] args) {
		// user input from the console
		scannerInput = new Scanner(System.in);
		// print command line interface
		System.out.println("1 D Median Filter Driver Program");
		System.out.println("Please enter: <data file name> <filter size> <output file name>");
		System.out.println("Type \"i\" for more information.");
		System.out.println("Type \"q\" to quit.");
		// receive next line
		String input = scannerInput.nextLine();

		while (!input.toLowerCase().equals("q")) {
			if (input.toLowerCase().equals("i")) {

				int cores = Runtime.getRuntime().availableProcessors();
				long memory = Runtime.getRuntime().freeMemory();

				String version = System.getProperty("java.version");
				System.out.println("\nThis system is running JVM version " + version + ".");
				System.out.println("Available processor cores: " + cores);
				System.out.println("Total allocated memory: " + memory / 1000000 + "kB");

				// command line menu
				System.out.println("\nPlease enter: <data file name> <filter size> <output file name>");
				System.out.println("Type \"i\" for more information.");
				System.out.println("Type \"q\" to quit.");
				input = scannerInput.nextLine();

			}

			else { // parsing the input from console
				String element[] = input.split(" ");
				fileIn = element[0];
				filter = element[1];
				fileOut = element[2];
				filterSize = 0;
				// error checking for filter size
				try {
					filterSize = Integer.parseInt(filter);
					// filter too small or even in size
					if (filterSize < 3 || filterSize % 2 == 0) {
						throw new Exception();
					}
					break;

					// catch filter size error
				} catch (Exception e) {
					// an incorrect integer value - quit
					System.out.println("Please enter a valid odd integer filter size.");
					System.out.println("\n---------TERMINATED---------");
					System.exit(1);
				}
			}
		}

		// run serial test
	//	Serial serial = new Serial(fileIn, filterSize, fileOut);
		// run parallel test
		Parallel parallel = new Parallel(fileIn, filterSize, fileOut);

	}

}
