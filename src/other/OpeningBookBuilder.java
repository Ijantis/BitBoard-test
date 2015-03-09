package other;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class OpeningBookBuilder {

	/**
	 * Takes in a path of a polyglot .bin file containing the opening book and creates
	 * a useable opening book in src/book/myBook.txt
	 * 
	 * @param openingBook
	 *            - The path of the opening book database to be used for the
	 *            engine's opening book.
	 */

	public static void createOpeningBook(Path openingBook) {

		try {
			Path myFile = openingBook;
			byte[] data = Files.readAllBytes(myFile);

			ArrayList<String> storage = new ArrayList<String>();
			String total = "";
			for (int i = 0; i < data.length; i++) {

				if (data[i] < 0) {
					total += Integer.toBinaryString(data[i]).substring(
							Integer.toBinaryString(data[i]).length() - 8);
				} else {

					total += String.format("%8s",
							Integer.toBinaryString(data[i])).replace(' ', '0');

				}
				if (total.length() == 64) {
					long temp = Long.parseUnsignedLong(total, 2);
					String nextValue = Long.toHexString(temp);

					while (nextValue.length() != 16) {
						nextValue = "0" + nextValue;
					}
					System.out.println("total length = " + total.length());
					System.out.println(total);
					System.out.println(nextValue);
					System.out.println(nextValue.length());
					System.out.println();
					storage.add(nextValue);
					total = "";
					continue;

				}
			}

			System.out.println(storage.size());

			File outputFile = new File("src/book/myBook.txt");

			outputFile.delete();
			outputFile.createNewFile();

			PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
			for (int i = 0; i < storage.size(); i += 2) {
				writer.println(storage.get(i) + "-" + storage.get(i + 1));

			}
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
