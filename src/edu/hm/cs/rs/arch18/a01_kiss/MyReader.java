package edu.hm.cs.rs.arch18.a01_kiss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/** MyReader kann genutzt werden, um den Inhalt aus CSV Dateien zu lesen.
 * Um Daten zu lesen, kann die <b>read</b> Methode genutzt werden, die
 * als Argument ein Reader Objekt auf die CSV Datei benötigt.
 * 
 * @author fabian
 *
 */
public class MyReader implements CSVReader {
	/** Default Konstruktor.
	 * */
	public MyReader(){}
	/**Prüft, ob das CSV Format korrekt ist.
	 * Ist dies nicht der Fall, bricht es mit einer Exception ab.
	 * @param reader Reader Ein Reader der CSV Datei
	 * @throws IllegalArgumentException Falls das CSV Format nicht korrekt ist
	 * @throws IOException Falls beim Lesen ein Fehler auftritt
	 * */
	private void checkCSVFormatIsCorrect(Reader reader) throws IllegalArgumentException,IOException{
			int a=0;
			int b=0;
			markReader(reader);
			do{
				// a = reader.read();
				 b = reader.read();
				 if(b == -1 && (char)a != '\n'){
					 reader.close();
					 throw new IllegalArgumentException();
					 }
				 else if(a == '\\' && (b == -1 || (char)b == '\n')){
					 reader.close();
					 throw new IllegalArgumentException();
				 }
				 a = b;
			}while(b != -1);
			resetReader(reader);
	}
	/**Markiert die aktuelle Position des Readers.
	 * Ein Aufruf von resetReader setzt den Pointer des Readers an
	 * diese Position zurück.
	 * @param reader Reader Ein Reader der CSV Datei
	 * @throws IOException Falls beim Lesen ein Fehler auftritt
	 * */
	private void markReader(Reader reader) throws IOException{
		reader.mark(1000);
	}
	/**Setzt den Reader auf die Position zurück,die zuvor.
	 * durch einen Aufruf von markReader markiert wurde
	 * @param reader Reader Ein Reader der CSV Datei
	 * @throws IOException Falls beim Lesen ein Fehler auftritt
	 * */
	private void resetReader(Reader reader) throws IOException{
		reader.reset();
	}
	
	/**Zählt Anzahl der Zeilen der CSV Datei.
	 * Die Position des Readers wird am DateiAnfang markiert und
	 * vor dem Retunieren der Methode zurückgesetzt.
	 * @param reader Reader Ein Reader der CSV Datei
	 * @throws IOException Falls beim Lesen ein Fehler auftritt
	 * @return int Anzahl der Zeilen
	 * */
	private int countRows(Reader reader) throws IOException{
		int rows = 0;
		
		markReader(reader);
		for(int s=0;s != -1; s = reader.read()){
			if((char)s == '\n')rows++;
		}
		resetReader(reader);
		
		return rows;
	}
	
	/**Liest bis zu einem LineFeed und gibt den Inhalt jeder Zeile
	 * durch ein String[] Array zurück.Die Position des Readers wird
	 * beim letzten gelesenen Zeichen gespeichert.
	 * 
	 * @param br BufferedReader Reader auf einen CSV Text
	 * @throws IOException falls beim Lesen ein Fehler auftritt
	 * @return arr String[] String Array das den Inhalt der Zellen einer Zeile enthält
	 * */
	private String[] getData(BufferedReader br) throws IOException{
		final String[] arr;
		int length = 0;
		//BufferedReader br = reader;
		final String s = br.readLine(); //System.out.println("FFFF :  " + s);
		
		for(int i=0; i<s.length(); i++){
			if(s.charAt(i) == ',' && s.charAt(i-1)!= '\\'){
				length++;
			}
		}
		if(length>0){
			length++;
			arr = new String[length];
			
			for(int firstindex=0,secondindex=0,arrayindex=0; secondindex != s.length() && firstindex != s.length();secondindex++){
				if(s.charAt(secondindex) == ',' && s.charAt(secondindex-1) != '\\'){
					firstindex = (firstindex == 0)? 0 : ++firstindex;
					arr[arrayindex] = s.substring(firstindex, secondindex);
					arrayindex++;
					firstindex = secondindex;
				}
				else if(secondindex == s.length()-1){
					arr[arrayindex] = s.substring(firstindex+1, secondindex+1); //System.out.println(s.substring(firstindex+1, secondindex+1));
					firstindex = secondindex;
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
	

	@Override
	public String[][] read(Reader reader) throws IOException, IllegalArgumentException {
		
		final String[][] arr;
		int length = 0;
		
		final BufferedReader buffreader = new BufferedReader(reader);
		
		checkCSVFormatIsCorrect(reader);
		
		System.out.println("Test!");
		//System.out.println(countRows(buffreader));
		
		length = countRows(buffreader);
		arr = new String[length][];
		
		for(int i=0; i<length; i++){
			arr[i] = getData(buffreader);
		}
		reader.close();
		
		return arr;
	}

}
