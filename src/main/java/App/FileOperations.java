package App;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class FileOperations{
	
	public static <T> void saveFile(String filename, ArrayList<T> list) {

		if(filename == null || list == null)
			throw new IllegalArgumentException();
		try {
			PrintWriter pw;
			pw = new PrintWriter(new FileOutputStream(filename));
			for (T a : list) {
				pw.println(a.toString());
			}

			pw.close();
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File saving error.");
		}
	}

	@SuppressWarnings("resource")
	public static <T> ArrayList<String> loadFile(String filename) {
		
		File file = createFile(filename);
		Scanner sc;
		ArrayList<String> values = new ArrayList<String>();

		try {
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				values.add(sc.nextLine());
			}
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Something happened to file during execution");
		}
		return values;
	}
	
	public static File createFile(String filename) {
		File file = new File(filename);
		try {
			file.createNewFile();
		} catch (IOException ex) {
			throw new IllegalArgumentException("IO Exception");
		}
		return file;
	}
}
