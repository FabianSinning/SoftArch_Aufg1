package edu.hm.cs.rs.arch18.a01_kiss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * @author Felix Lang
 *
 */
public class MyReader implements CSVReader {
	/**
	 * Default Konstruktor.
	 */
	private static boolean FLAG = false;
	private static final char BACKSLASH = '\\';
	private static final char NEWLINE = '\n';

	public MyReader() {
	}

	/**
	 * Markiert die aktuelle Position des Readers. Ein Aufruf von resetReader
	 * setzt den Pointer des Readers an diese Position zurueck.
	 * 
	 * @param reader
	 *            Reader Ein Reader der CSV Datei
	 * @throws IOException
	 *             Falls beim Lesen ein Fehler auftritt
	 */
	private void markReader(Reader reader) throws IOException {
		reader.mark(1000);
	}

	/**
	 * Setzt den Reader auf die Position zurueck,die zuvor. durch einen Aufruf
	 * von markReader markiert wurde
	 * 
	 * @param reader
	 *            Reader Ein Reader der CSV Datei
	 * @throws IOException
	 *             Falls beim Lesen ein Fehler auftritt
	 */
	private void resetReader(Reader reader) throws IOException {
		reader.reset();
	}

	/**
	 * Liest bis zu einem LineFeed und gibt den Inhalt jeder Zeile durch ein
	 * String[] Array zurueck.Die Position des Readers wird beim letzten
	 * gelesenen Zeichen gespeichert.
	 * 
	 * @param br
	 *            BufferedReader Reader auf einen CSV Text
	 * @throws IOException
	 *             falls beim Lesen ein Fehler auftritt
	 * @return arr String[] String Array das den Inhalt der Zellen einer Zeile
	 *         enthaelt
	 */
	private String[] getData(BufferedReader br) throws IOException {
		final String[] arr;
		int length = 0;
		// BufferedReader br = reader;
		final String s = br.readLine(); // System.out.println("FFFF : " + s);

		for (int i = 0; i < s.length(); i++) {
			if (i > 0) {
				if (s.charAt(i) == ',' && s.charAt(i - 1) != '\\') {
					length++;
				}
			} else {
				if (s.charAt(i) == ',') {
					length++;
				}
			}
		}
		if (length > 0) {
			length++;
			arr = new String[length];

			for (int firstindex = 0, secondindex = 0, arrayindex = 0; secondindex != s.length()
					&& firstindex != s.length(); secondindex++) {
				char firstcharacter = (secondindex == 0) ? ' ' : s.charAt(secondindex - 1);
				if (secondindex == s.length() - 1 && s.charAt(secondindex) != ',') {
					arr[arrayindex] = s.substring(firstindex + 1, secondindex + 1); // System.out.println(s.substring(firstindex+1,
																					// secondindex+1));
					secondindex++;
					firstindex = secondindex;
				} else if (secondindex == s.length() - 1 && s.charAt(secondindex) == ',') {
					firstindex = (firstindex == 0) ? 0 : ++firstindex;
					arr[arrayindex] = s.substring(firstindex, secondindex); // System.out.println(s.substring(firstindex+1,
																			// secondindex+1));
					secondindex++;
					arr[++arrayindex] = "";
					firstindex = secondindex;
				} else if (s.charAt(secondindex) == ',' && firstcharacter != '\\') {
					if (firstcharacter == ',') {
						// firstindex = (firstindex == 0)? 0 : ++firstindex;
						firstindex = secondindex;
						arr[arrayindex] = "";
						arrayindex++;
					} else {
						firstindex = (firstindex == 0) ? 0 : ++firstindex;
						arr[arrayindex] = s.substring(firstindex, secondindex);
						arrayindex++;
						firstindex = secondindex;
					}
				}
			}
			markReader(br);
			resetReader(br);
			return arr;
		}
		arr = new String[1];
		arr[0] = s;

		markReader(br);
		resetReader(br);
		return arr;
	}

	// prints out the inserted Arguments, for debugging purposes only
	private void printArgument(Reader reader) throws IOException {
		String out = "";
		char c;

		int i = reader.read();
		if (i == -1)
			System.out.println("Empty Input!");
		while (i != -1) {
			c = (char) i;
			if (c == '\n')
				c = '$';
			out += c;
			i = reader.read();
		}
		System.out.println(out);
		reader.reset();
	}

	@Override
	public String[][] read(Reader reader) throws IOException, IllegalArgumentException {

		// final BufferedReader buffreader = new BufferedReader(reader);
		// // System.out.println(countRows(buffreader));

		final String[][] output;
		int lines;
		int[] columnsInLine;

		checkInputFormat(reader);
		printArgument(reader);
		lines = countLines(reader);
		columnsInLine = countColumnsPerLine(reader, lines);
		output = new String[lines][];

		for (int i = 0; i < lines; i++) {
			output[i] = buildLine(reader);
		}

		reader.close();
		return output;
	}

	// input: Reader
	// output: none. Throws exception if the inserted Text is not valid.
	private void checkInputFormat(Reader reader) throws IllegalArgumentException, IOException {

		char c;

		// check for empty argument.
		int j = reader.read();
		if (j == -1) {
			reader.close();
			throw new IllegalArgumentException();
		}

		c = (char) j;

		// step to the last char in the argument and safe it in variable c
		for (int i = j; i != -1; i = reader.read()) {

			// if flag is set, skip the next char
			if (FLAG) {
				reader.read();
				i = 0;
				FLAG = false;
			} else if (i == BACKSLASH) {
				FLAG = true;
			}
			c = (char) i;
		}
		// c is the last char in the argument now. it has to be a '\n', or an
		// exception is thrown
		if (c != '\n') {
			reader.close();
			throw new IllegalArgumentException();
		}
		resetReader(reader);
		// return;
		// TODO: implement with Exceptions
	}

	// input: Reader
	// output: number of lines > int
	private int countLines(Reader reader) throws IOException {
		markReader(reader);
		int lines = 0;
		char c;

		for (int i = reader.read(); i != -1; i = reader.read()) {
			c = (char) i;
			// if the Flag is not set, check for '\n' and backslashes

			if (c == BACKSLASH) {
				reader.read();
			}
			// in case of a Backslash, read the next char. It gets overwritten
			// immediatly
			else if (c == '\n') {
				lines++;
			}
		}

		resetReader(reader);
		return lines;

	}

	// input: Reader
	// output: number of columns > int
	private int countColumns(Reader reader) throws IOException {
		int columns = 1;
		char c;

		for (int i = reader.read(); i != -1; i = reader.read()) {
			c = (char) i;
			// if the Flag is not set, check for '\n' and backslashes

			if (c == BACKSLASH) {
				reader.read();
			}
			// in case of a Backslash, read the next char. It gets overwritten
			// immediately
			else if (c == ',') {
				columns++;
			} else if (c == '\n') {
				resetReader(reader);
				return columns;
			}
		}

		return -1;
		// TODO: implement
	}

	private String[] buildLine(Reader reader) throws IOException {
		int columns = countColumns(reader);
		String[] out = new String[columns];
		System.out.println(columns);
		for (int i = 0; i < out.length; i++) {
			out[i] = buildCell(reader);
		}
		System.out.println(out.length);
		// TODO: implement concatination of chars to String
		return out;
	}

	private String buildCell(Reader reader) throws IOException {
		String out = "\"";
		char c;
		for (int i = reader.read(); i != -1; i = reader.read()) {
			c = (char) i;
			if (FLAG) {
				out += c;
				FLAG = false;
			} else {
				if (c == BACKSLASH) {
					FLAG = true;
				} else if (c == ',' || c == '\n') {
					out += '"';
					return out;
				} else
					out += c;
			}
		}

		return out;

	}

	private int[] countColumnsPerLine(Reader reader, int lines) throws IOException {
		int[] out = new int[lines];
		int i = reader.read();
		int lin = 0;
		char c;
		int count = 1;
		while (lin < lines && i != -1) {

			c = (char) i;

			// check for '\n' , comma and backslashes
			// in case of a Backslash, read the next char. It gets overwritten
			// immediately
			if (c == BACKSLASH) {
				i = reader.read();

			} else if (c == ',') {
				count++;

			} else if (c == '\n') {
				out[lin] = count;
				lin++;
				count = 1;
			}
			i = reader.read();

		}
		for (int grr = 0; grr < lines; grr++) {
			System.out.println("Cells in line "+ (grr+1)+ ": "+out[grr]);
		}
		resetReader(reader);
		return out;
	}

}
