package Junit5;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import App.FileOperations;
import App.Score;

public class FileOperationsTest {

	
	@Test
    @DisplayName("Should be true if value from file is same as before")
    public void testLoadFile() {
    	ArrayList<Score> test = new ArrayList<Score>();
    	test.add(new Score(8.0,"osiem"));
    	
       	FileOperations.saveFile("testfile.txt", test);
       	ArrayList<Score> result = Score.toList(FileOperations.loadFile("testfile.txt"));
       	
       	
       	assertEquals("osiem",result.get(0).name);
    }
	
	
	@Test
	@DisplayName("Should return true if exception was thrown by passing empty values")
	void savefileNullException() {
		ArrayList<Score> temp = new ArrayList<Score>();
		assertAll(
		() -> assertThrows(
				IllegalArgumentException.class,
				() -> FileOperations.saveFile(null, null)
				),
		() -> assertThrows(
				IllegalArgumentException.class,
				() -> FileOperations.saveFile("nulltest", null)
				),
		() -> assertThrows(
				IllegalArgumentException.class,
				() -> FileOperations.saveFile(null, temp)
				)
		
		);

	}
	
	@Test
	@DisplayName("Should return true if file not found exception was thrown")
	void loadfileNotExistsException() {
		assertThrows(
				IllegalArgumentException.class,
				() -> FileOperations.loadFile("!$@$!@#?!$///...")
				);

	}
	
	@Test
	@DisplayName("Should return true if exception was thrown by passing not existing file")
	void savefileNotExistsException() {
		ArrayList<Score> temp = new ArrayList<Score>();
		assertThrows(
				IllegalArgumentException.class,
				() -> FileOperations.saveFile("/dev/null/fadsasd!!@#!@#",temp)
				);

	}
}
