
// Parallel code class for median filter assignment
// EDLADA002
// August 2015

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Parallel {
	// fork join pool object
	static final ForkJoinPool pool = new ForkJoinPool();
	// array of data from file stored as floats
	float[] floats;

	// Testing method
	public Parallel(String fileIn, int filter, int sequential) {
		// length of file
		int length = 0;
		// file read
		try {
			BufferedReader file = new BufferedReader(new FileReader(fileIn + ".txt"));
			Scanner sort = new Scanner(file);
			length = sort.nextInt();
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
			System.out.println("File reading error for parallel input.\n" + e);
		}

		// run the parallel filter
		try {

			// test run
			MedianFilter runFilterr = new MedianFilter(floats, new float[length], filter, 0, length, sequential);
			float t = 0;
			for (int i = 0; i < 10; i++) {
				// create new parallel filter object
				runFilterr = new MedianFilter(floats, new float[length], filter, 0, length, sequential);
				// start timer
				start();
				// invoke the fork join pool
				pool.invoke(runFilterr);
				// stop timer
				float end = stop();
				t = t+stop();
			//	System.out.println("Total parallel run time: " + stop() + " milliseconds");

			}
			float[] temp = runFilterr.output;

			System.out.println("Average parallel run time: " + t/10 + " milliseconds");
			// write to file
			BufferedWriter out = new BufferedWriter(new FileWriter("pOut.txt"));
			out.write(t/10+"");
			out.newLine();
			out.write(floats.length + "\n");
			for (int i = 0; i < floats.length; i++) {
				out.write((i + 1) + " " + temp[i] + "");
				out.newLine();
			}
			out.close();

		} catch (Exception e) {
			System.out.println("Testing Error");
			e.printStackTrace();
		}
	}

	// driver called method
	public Parallel(String fileIn, int filter, String fileOut) {
		// length of file
		int length = 0;
		// file read
		try {

			BufferedReader file = new BufferedReader(new FileReader(fileIn + ".txt"));
			Scanner sort = new Scanner(file);
			length = sort.nextInt();
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
			System.out.println("File reading error for parallel input.\n" + e);
		}

		// run the parallel filter
		try {
			// create new parallel filter object
			MedianFilter runFilterr = new MedianFilter(floats, new float[length], filter, 0, length, 500);
			// start timer
			start();
			// invoke the fork join pool
			pool.invoke(runFilterr);
			// stop timer
			float end = stop();
			// print the results
			DecimalFormat millisecond = new DecimalFormat("####.####");
			System.out.println("\n*************************************************");
			System.out.println("* Parallel Results: \t\t\t\t*");
			System.out.println("* Number of elements in the dataset: " + floats.length + "\t*");
			System.out.println("* Parallel code run time: " + millisecond.format(end) + " milliseconds\t*");
			System.out.println("* Output written to file: " + fileOut + ".txt \t\t*");
			System.out.println("*************************************************");

			float[] temp = runFilterr.output;
			// write to file
			BufferedWriter out = new BufferedWriter(new FileWriter(fileOut + ".txt"));
			out.write(floats.length + "\n");
			for (int i = 0; i < floats.length; i++) {
				out.write((i + 1) + " " + temp[i] + "");
				out.newLine();
			}
			out.close();

		} catch (Exception e) {
			System.out.println("Median filter error for parallel computation.\n");
			e.printStackTrace();
		}

	}

	// median filter code
	public class MedianFilter extends RecursiveAction {
		// filter class variables
		float[] numbers;
		float[] output;
		int filter;
		int window;
		int length;
		int lo;
		int hi;
		// sequential cutoff set to predetermined value or changed through
		// testing class
		int SEQUENTIAL_CUTOFF = 1000;

		// method to set sequential cutoff for testing
		public void setCutoff(int cutoff) {
			SEQUENTIAL_CUTOFF = cutoff;
		}

		public MedianFilter(float[] num, float[] out, int filt, int lo, int hi, int SEQUENTIAL_CUTOFF) {
			this.lo = lo;
			this.hi = hi;
			numbers = num;
			output = out;
			filter = filt;
			length = num.length;
			// the section of floats to be filtered (median section)
			window = (filter - 1) / 2;
			this.SEQUENTIAL_CUTOFF = SEQUENTIAL_CUTOFF;
		}

		protected void compute() {

			if ((hi - lo) < SEQUENTIAL_CUTOFF) {
				// iterate window through all elements of the array
				for (int a = lo; a < hi; a++) {
					// fetch boundary elements
					if (a < window || a >= length - window) {
						output[a] = numbers[a];
					}
					// fetch elements within filter window
					else {
						// create a new float array of the filter size
						float[] temp = new float[filter];
						// iterate through the window
						for (int j = a - window; j <= a + window; j++) {
							// set the filter sized array to the median value
							temp[j - (a - window)] = numbers[j];
						}
						// sort the filter sized array
						Arrays.sort(temp);
						// set the values of the median from the window
						output[a] = temp[window];

					}

				}

			} else {
				// create left and right split
				MedianFilter left = new MedianFilter(numbers, output, filter, lo, (hi + lo) / 2, SEQUENTIAL_CUTOFF);
				MedianFilter right = new MedianFilter(numbers, output, filter, (hi + lo) / 2, hi, SEQUENTIAL_CUTOFF);
				left.fork();
				right.compute();
				left.join();
			}

		}

	}

	// performance timing
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
