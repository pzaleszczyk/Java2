package AssertJ;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;

import org.assertj.core.api.SoftAssertions;
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
       	
       	
       	assertThat(result.get(0).name).isEqualTo("osiem");
    }
	
	@Test
	@DisplayName("Should return true if exception was thrown by passing empty values")
	void savefileNullException() {
		ArrayList<Score> temp = new ArrayList<Score>();
		SoftAssertions softly = new SoftAssertions();
		softly.assertThatThrownBy(() -> { FileOperations.saveFile(null, null); })
		.isInstanceOf(IllegalArgumentException.class);	
		softly.assertThatThrownBy(() -> { FileOperations.saveFile("nulltest", null); })
		.isInstanceOf(IllegalArgumentException.class);	
		softly.assertThatThrownBy(() -> { FileOperations.saveFile(null, temp); })
		.isInstanceOf(IllegalArgumentException.class);
		softly.assertAll();
	}
	
	@Test
	@DisplayName("Should return true if file not found exception was thrown")
	void loadfileNotExistsException() {
		assertThatThrownBy(() -> { FileOperations.loadFile("!$@$!@#?!$///..."); })
		.isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("IO Exception");
	}
	
	@Test
	@DisplayName("Should return true if exception was thrown by passing not existing file")
	void savefileNotExistsException() {
		ArrayList<Score> temp = new ArrayList<Score>();
		
		assertThatThrownBy(() -> { FileOperations.saveFile("/dev/null/fadsasd!!@#!@#",temp); })
		.isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("File saving error.");

	}
	
	
}
