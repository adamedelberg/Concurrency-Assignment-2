
// Serial concept class for median filter assignment
// EDLADA002
// August 2015
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

public class Serial {

	// default constructor
	public Serial() {
	}

	// array of data from file stored as floats
	float[] floats;
	// array of data once run through median filter
	float[] filtered;
//testing method
	public Serial(String fileIn, int filter) {
		// file read
				try {
					BufferedReader file = new BufferedReader(new FileReader(fileIn+".txt"));
					Scanner sort = new Scanner(file);
					int length = sort.nextInt();
					// static array of floats of correct length
					floats = new float[length];
					// skip the top line containing the number
					sort.nextLine();

					for (int i = 0; i < length; i++) {
						// get just the float value
						String temp = sort.nextLine().split(" ")[1];
						// parse string to float value
						float tempFloat = Float.parseFloat(temp);
						// add value to array
						floats[i] = tempFloat;
					}

				} catch (Exception e) {
					// error capturing
					System.out.println("File reading error for serial input.\n" + e);
				}

				// run the serial filter
				try {
					float t = 0;	
					MedianFilter runFilter = new MedianFilter(floats, filter);

					for (int i = 0; i < 10; i++) {
						// create a new filter object
						 runFilter = new MedianFilter(floats, filter);
						// start timer
						start();
						// populate array with filtered floats
						filtered = runFilter.filter();
					    // stop timer
						float end = stop();
						t=t+stop();
						//System.out.println("Total sequential run time: " + stop() + " milliseconds");

					}
					System.out.println("Average sequential run time: " + t/10 + " milliseconds");


					
					// write to file
					BufferedWriter out = new BufferedWriter(new FileWriter( "sOut.txt"));
					out.write(t/10+"");
					out.newLine();
					out.write(filtered.length + "\n");
					for (int i = 0; i < filtered.length; i++) {
						out.write((i + 1) + " " + filtered[i] + "");
						out.newLine();
					}
					out.close();
					
				

					

				} catch (Exception e) {
					// error capturing
					System.out.println("Median filter error for serial compute.\n" + e);
					e.printStackTrace();
				}
	}
	
	public Serial(String fileIn, int filter, String fileOut) {
		// file read
		try {
			BufferedReader file = new BufferedReader(new FileReader(fileIn+".txt"));
			Scanner sort = new Scanner(file);
			int length = sort.nextInt();
			// static array of floats of correct length
			floats = new float[length];
			// skip the top line containing the number
			sort.nextLine();

			for (int i = 0; i < length; i++) {
				// get just the float value
				String temp = sort.nextLine().split(" ")[1];
				// parse string to float value
				float tempFloat = Float.parseFloat(temp);
				// add value to array
				floats[i] = tempFloat;
			}

		} catch (Exception e) {
			// error capturing
			System.out.println("File reading error for serial input.\n" + e);
		}

		// run the serial filter
		try {
			// create a new filter object
			MedianFilter runFilter = new MedianFilter(floats, filter);
			// start timer
			start();
			// populate array with filtered floats
			filtered = runFilter.filter();
		    // stop timer
			float end = stop();
			// print results to console
			DecimalFormat millisecond = new DecimalFormat("####.####");
			System.out.println("\n*************************************************");
			System.out.println("* Sequential Results: \t\t\t\t*");
			System.out.println("* Number of elements in the dataset: " + floats.length + "\t*");
			System.out.println("* Sequential code run time: " + millisecond.format(end) + " milliseconds\t*");
			System.out.println("* Output written to file: " + fileOut + ".txt \t\t*");
			System.out.println("*************************************************");

			// write to file
			BufferedWriter out = new BufferedWriter(new FileWriter(fileOut + ".txt"));
			out.write(filtered.length + "\n");
			for (int i = 0; i < filtered.length; i++) {
				out.write((i + 1) + " " + filtered[i] + "");
				out.newLine();
			}
			out.close();

		} catch (Exception e) {
			// error capturing
			System.out.println("Median filter error for serial compute.\n" + e);
			e.printStackTrace();
		}

	}

	// median filter code
	public class MedianFilter {
		// filter class variables
		float[] numbers;
		int filter;
		int length;
		int window;
		float[] output;
		float[] filtered;

		public MedianFilter(float[] numbers, int filter) {
			// float data
			this.numbers = numbers;
			// filter size
			this.filter = filter;
			// length of float array
			length = numbers.length;
			// the section of floats to be filtered (median section)
			window = (filter - 1) / 2;
		}
		// filter method to return filtered array

		public float[] filter() {
			// output result array
			float[] output = new float[length];
			// create new array of same size
			// float[] filtered = new float[length];

			// iterate window through all elements of the array
			for (int i = 0; i < length; i++) {
				// fetch boundary elements
				if (i < window || i >= length - window) {
					output[i] = numbers[i];

				}
				// fetch elements within filter window
				else {
					// create a new float array of the filter size
					float[] temp = new float[filter];
					// iterate through the window
					for (int j = i - window; j <= i + window; j++) {
						// set the filter sized array to the median value
						temp[j - (i - window)] = numbers[j];
					}
					// sort the filter sized array
					Arrays.sort(temp);
					// set the values of the median from the window
					output[i] = temp[window];

				}
			}

			// return filtered array
			return output;
		}

	}

	// system time performance timing
	static long startTime;

	// start the timer
	private static void start() {
		startTime = System.currentTimeMillis();
	}

	// stop the timer
	private static float stop() {
		return (System.currentTimeMillis() - startTime) / 1000.0f;
	}

}
