import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

// Test class for median filter assignment
// EDLADA002
// August 2015

public class PerformanceTest {

	public static void main(String[] args) throws IOException {

		// user input from the console
		Scanner scannerInput = new Scanner(System.in);
		// print command line interface
		System.out.println("1 D Median Filter Performance Testing Program");
		System.out.println("Type \"t\" to start testing.");
		System.out.println("Type \"a\" to start auto test.");
		System.out.println("Type \"i\" for more information.");
		System.out.println("Type \"q\" to quit.");
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
				System.out.println("\nType \"t\" to start testing.");
				System.out.println("Type \"q\" to quit.");
				input = scannerInput.nextLine();

			}

			else if (input.toLowerCase().equals("a")) {
				// variables for the auto test
				int filter[] = new int[] { 3, 5, 7, 9, 11 };
				int sequential[] = new int[] { 50, 200, 500, 1000, 1500 };
				String file[] = new String[] { "inp1", "inp2", "inp3", "inp4" };
				float[] max = new float[100];
				BufferedWriter out = new BufferedWriter(new FileWriter("auto.txt"));
				out.write("Performance Test: ");
				int count = 0;
				for (int i = 0; i < file.length; i++) {
					for (int j = 0; j < filter.length; j++) {
						for (int k = 0; k < sequential.length; k++) {
							Serial auto = new Serial(file[i], filter[j]);
							Parallel auto2 = new Parallel(file[i], filter[j], sequential[k]);
							try {
								// read the two separate files
								BufferedReader pOut = new BufferedReader(new FileReader("pOut.txt"));
								BufferedReader sOut = new BufferedReader(new FileReader("sOut.txt"));
								Scanner pScan = new Scanner(pOut);
								Scanner sScan = new Scanner(sOut);
								// read the times
								float pTime = pScan.nextFloat();
								float sTime = sScan.nextFloat();
								// creates file with all the testing values
								out.newLine();
								out.write("With " + file[i] + " and filter " + filter[j] + " and sequential limit "
										+ sequential[k]);
								out.newLine();
								out.write("Sequential Time: " + sTime);
								out.newLine();
								out.write("Parallel Time: " + pTime);
								out.newLine();
								max[count] = (pTime / sTime) * 100;
								count++;
								out.write("Parallel Speed Up: " + (pTime / sTime) * 100 + "%");
								out.newLine();
								out.newLine();
								out.flush();

							} catch (IOException e) {
								e.printStackTrace();
							}

						}

					}

				}
				// sort the array
				Arrays.sort(max);
				// get the maximum speed up
				float maximum = max[max.length - 1];
				System.out.println("The highest speed up was: " + maximum);
				// close the file
				out.close();
				// end loop
				break;
			}

			else {

				System.out.println("Please enter the test filter value:");
				// receive the input
				int filter = scannerInput.nextInt();
				// check valid filter size
				if (filter < 3 || filter % 2 != 1) {
					System.out.println("Invalid filter size.");
					System.out.println("\n---------TERMINATED---------");
					System.exit(1);
				} else {
					System.out.println("Please enter the test sequential threshold:");
					int sequential = scannerInput.nextInt();
					System.out.println("Please select a data test set:");
					System.out.println("1. Test data set of 35 999 elements");
					System.out.println("2. Test data set of 100 001 elements");
					System.out.println("3. Test data set of 440 001 elements");
					System.out.println("4. Test data set of 1 999 886 elements");
					int selection = scannerInput.nextInt();
					// run the test methods
					switch (selection) {
					case 1:
						Serial test1Serial = new Serial("inp1", filter);
						Parallel test1 = new Parallel("inp1", filter, sequential);
						break;
					case 2:
						Serial test2Serial = new Serial("inp2", filter);
						Parallel test2 = new Parallel("inp2", filter, sequential);
						break;
					case 3:
						Serial test3Serial = new Serial("inp3", filter);
						Parallel test3 = new Parallel("inp3", filter, sequential);
						break;
					case 4:
						Serial test4Serial = new Serial("inp4", filter);
						Parallel test4 = new Parallel("inp4", filter, sequential);
						break;
					}
					// read files created by test methods
					try {
						// read the two separate files
						BufferedReader pOut = new BufferedReader(new FileReader("pOut.txt"));
						BufferedReader sOut = new BufferedReader(new FileReader("sOut.txt"));

						Scanner pScan = new Scanner(pOut);
						Scanner sScan = new Scanner(sOut);
						// read the times
						float pTime = pScan.nextFloat();
						float sTime = sScan.nextFloat();
						// print the result
						System.out.println("The parallel code execution time was " + Math.round((pTime / sTime) * 100)
								+ "% faster");
						break;
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}
}
