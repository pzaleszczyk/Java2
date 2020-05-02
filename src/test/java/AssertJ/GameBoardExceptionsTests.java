package AssertJ;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import App.*;
class GameBoardExceptionsTests {

	GameBoard board;

	@BeforeEach
	void setUp() throws Exception {
		board = new GameBoard();
	}

	@AfterEach
	void tearDown() throws Exception {
		board = null;
	}



	@Test
	@DisplayName("Should return true if exception was thrown by passing null")
	void addToLeaderboardsNullException() {
		assertThatThrownBy(() -> { board.addToLeaderboards(null); })
		.isInstanceOf(IllegalArgumentException.class);

	}
	
	
	@Test
	@DisplayName("Should return true if exception was thrown by passing empty string")
	void addToLeaderboardsEmptyException() {
		
		assertThatThrownBy(() -> { board.addToLeaderboards(""); })
		.isInstanceOf(IllegalArgumentException.class);

	}
	
	
	@Test
	@DisplayName("Should return true if exception was thrown by passing vals lower than 1")
	void gameBoardConstructException() {
		
		SoftAssertions softly = new SoftAssertions();
		softly.assertThatThrownBy(() -> { new GameBoard(-1,-1); })
		.isInstanceOf(IllegalArgumentException.class);
		softly.assertThatThrownBy(() -> { new GameBoard(-1,1); })
		.isInstanceOf(IllegalArgumentException.class);
		softly.assertThatThrownBy(() -> { new GameBoard(1,-1); })
		.isInstanceOf(IllegalArgumentException.class);
		softly.assertAll();
		
	}
	
	@Test
	@DisplayName("Should return true if exception was thrown by passing negative value")
	void addDiscNegativeParameterException() {
		assertThatThrownBy(() -> { board.addDisc(-1); })
		.isInstanceOf(IllegalArgumentException.class);
		
	}
	

	
	@Test
	@DisplayName("Should return true if exception was thrown by passing vals lower than 0")
	void nextMoveNegativeValuesException() {
		
		SoftAssertions softly = new SoftAssertions();
		softly.assertThatThrownBy(() -> { board.nextMove(-1, -1); })
		.isInstanceOf(IllegalArgumentException.class);
		
		softly.assertThatThrownBy(() -> { board.nextMove(-1, 1); })
		.isInstanceOf(IllegalArgumentException.class);
		
		softly.assertThatThrownBy(() -> { board.nextMove(1, -1); })
		.isInstanceOf(IllegalArgumentException.class);
		
		softly.assertAll();
		
	}
	

}
