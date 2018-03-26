/* (C) 2018, R. Schiedermeier, rs@cs.hm.edu
 * Java 1.8.0_121, Linux x86_64 4.15.4
 * bluna (Intel Core i7-5600U CPU/2.60GHz, 4 cores, 2038 MHz, 16000 MByte RAM)
 **/
package edu.hm.cs.rs.arch18.a01_kiss; 
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Programm zum Ausprobieren eines CSV-Reader.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2018-03-05
 */
public class CSVReaderMain {
    /** Holt CSV-Text und verfuettert ihn an einen CSV-Reader.
     * Druckt das zurueck gegebene Array auf der Konsole aus.
     * @param args Optionaler CSV-Text.
     * Falls angegeben, ersetzt dieses Programm alle $ durch Newlines.
     * Ansonsten liest das Programm CSV-Text von der Konsole und ersetzt dort nichts.
     * @throws IOException bei Lesefehlern im Reader.
     * @throws IllegalArgumentException wenn der CSV-Text Syntaxfehler enthaelt.
     */
    public static void main(String... args) throws IOException {
        final Reader reader = args.length > 0
                             ? new StringReader(args[0].replace('$', '\n')):new InputStreamReader(System.in);
                             
                       
        final CSVReader csvReader = new MyReader();//r -> null;  // Weisen Sie hier Ihren CSVReader zu.
        
        System.out.println(Stream.of(csvReader.read(reader))
                           .map(Arrays::toString)
                           .collect(Collectors.joining(",\n", "[", "]")));
    }

}
