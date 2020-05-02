package Hamcrest;


import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

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
       	
       	
       	assertThat(result.get(0).name,equalTo("osiem"));
    }
}
