package Junit5;
import static org.junit.jupiter.api.Assertions.*;
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
		assertThrows(
				IllegalArgumentException.class,
				() -> board.addToLeaderboards(null)
				);

	}
	
	@Test
	@DisplayName("Should return true if exception was thrown by passing empty string")
	void addToLeaderboardsEmptyException() {
		assertThrows(
				IllegalArgumentException.class,
				() -> board.addToLeaderboards("")
				);

	}
	
	@Test
	@DisplayName("Should return true if exception was thrown by passing vals lower than 1")
	void gameBoardConstructException() {
		

		
		assertAll(
		() -> assertThrows(
				IllegalArgumentException.class,
				() -> new GameBoard(1,-1)
				),
		() -> assertThrows(
				IllegalArgumentException.class,
				() -> new GameBoard(-1,1)
				),
		() -> assertThrows(
				IllegalArgumentException.class,
				() -> new GameBoard(-1,-1)
				)
		);
		
	}
	
	@Test
	@DisplayName("Should return true if exception was thrown by passing negative value")
	void addDiscNegativeParameterException() {

		assertThrows(
				IllegalArgumentException.class,
				() -> board.addDisc(-1)
				);
		
	}
	
	@Test
	@DisplayName("Should return true if exception was thrown by passing vals lower than 0")
	void nextMoveNegativeValuesException() {
		
		assertAll(
		() -> assertThrows(IllegalArgumentException.class,() ->  board.nextMove(-1, -1)),
		() -> assertThrows(IllegalArgumentException.class,() ->  board.nextMove(1, -1)),
		() -> assertThrows(IllegalArgumentException.class,() ->  board.nextMove(-1, 1))
		);
		
	}
	

}
