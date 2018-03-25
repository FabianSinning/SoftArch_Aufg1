/* (C) 2018, R. Schiedermeier, rs@cs.hm.edu
 * Oracle Corporation Java 1.8.0_121, Linux i386 4.14.12
 * violet: Intel Core i7 920/2660 MHz, 8 Core(s), 24100 MByte RAM
 **/
package edu.hm.cs.rs.arch18.a01_kiss;

import java.io.IOException;
import java.io.Reader;

/** Interface fuer CSV-Reader.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2018-03-16
 */
@FunctionalInterface public interface CSVReader {
    /** Liest CSV-Text vom Reader und packt Zeilen und Zellen in ein Array.
     * @param reader Reader mit CSV-Text. Nicht null.
     * @return Zweidimensionales Array mit Zeilen in der ersten Dimension
     * und Zellen (Spalten) in der zweiten.
     * Nicht null und kein Element null.
     * @throws IOException bei Lesefehlern vom Reader.
     * @throws IllegalArgumentException wenn der CSV-Text fehlerhaft ist.
     */
    String[][] read(Reader reader) throws IOException, IllegalArgumentException;

}
