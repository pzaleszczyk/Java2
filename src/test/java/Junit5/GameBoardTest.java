package Junit5;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import App.*;
public class GameBoardTest {
	
	GameBoard board;
	
	@BeforeEach
	void setUp() throws Exception{
		board = new GameBoard();
	}
	
	@AfterEach
	void tearDown() throws Exception {
		board = null;
	}
	

    
    @Test 
    @DisplayName("Should return true if rows = 6, and columns = 7")
    void testDefaultAmountOfColumnsAndRows() {
    	assertAll(
    			() -> assertEquals(6, board.rows),
    			() -> assertEquals(7, board.columns)
    			);
    }
    
    @ParameterizedTest (name = "{0} and {1}")
	@CsvFileSource(resources = "../testdata.csv", numLinesToSkip = 1, delimiter = '\t')
	@DisplayName("Should return true if rows and columns are correct")
    void testNondefaultAmountOfColumnsAndRows(String inr, String inc) {
    	GameBoard nboard = new GameBoard(Integer.parseInt(inr),Integer.parseInt(inc));
    	assertAll(
    			() -> assertEquals(Integer.parseInt(inr), nboard.rows),
    			() -> assertEquals(Integer.parseInt(inc), nboard.columns)
    			);
    }
   
    
    @Test
    @DisplayName("Should return true if color is RED")
    void testAddingRedDiscToBoard() {
    	board.addDisc(5);
    	assertEquals(GameBoard.Color.RED, board.Board[0][5]);
    }
    
    @Test
    @DisplayName("Should return true if color is GREEN")
    void testAddingGreenDiscToBoard() {
    	board.addDisc(5);
    	board.addDisc(5);
    	assertEquals(GameBoard.Color.GREEN, board.Board[1][5]);
    }
    
    @Test
    @DisplayName("Should return true if list object has correct values")
    void testNextMove() {
    	board.nextMove(1, 2);
    	assertAll(
    			() -> assertEquals(1, board.listOfmoves.get(0).row),
    			() -> assertEquals(2, board.listOfmoves.get(0).column),
    			() -> assertEquals(GameBoard.Color.RED, board.listOfmoves.get(0).color)
    			);
    }
    
    @Test
    @DisplayName("Should return true if both listOfMoves and Board removed move")
    void testUndoMove() {
    	board.addDisc(5);
    	board.undoMove();
    	
    	GameBoard.Color[][] expected1 = new GameBoard.Color[6][7];        
		ArrayList<Moves> expected2 =
				new ArrayList<Moves>();
		
    	assertAll(
    			() -> assertArrayEquals(expected1,board.Board),
    			() -> assertEquals(expected2, board.listOfmoves)
    	);
    	
    }
    
    @Test
    @DisplayName("Should return true if listOfMoves is empty")
    void testUndoMoveEmpty() {
    	board.undoMove();
    	assertEquals(new ArrayList<Moves>(),board.listOfmoves);
    }
    
    
    @Test
    @DisplayName("Should return true if Board has no null")
    void testCheckIfArrayFull() {
    	//Prepare
    	GameBoard.Color[][] actual =  {
    			{GameBoard.Color.RED,GameBoard.Color.GREEN,GameBoard.Color.GREEN},
    			{GameBoard.Color.GREEN,GameBoard.Color.RED,GameBoard.Color.GREEN},
    			{GameBoard.Color.GREEN,GameBoard.Color.GREEN,GameBoard.Color.RED},
    			{GameBoard.Color.GREEN,GameBoard.Color.GREEN,GameBoard.Color.GREEN}
    	};
    	board.Board = actual;
    	board.columns = 4;
    	board.rows = 4;
    	assertTrue(board.checkIfArrayFull());
    }
    
    @Test
    @DisplayName("Should return true if has 4 or more vertically")
    void testCheckIfWinConditionVertical() {
    	GameBoard.Color[][] actual =  {
    			{GameBoard.Color.RED,GameBoard.Color.RED,GameBoard.Color.RED,GameBoard.Color.RED},
    			{null,null,null,null},
    			{null,null,null,null},
    			{null,null,null,null}
    	};
    	board.Board = actual;
    	board.columns = 4;
    	board.rows = 4;
    	
    	
    	//Checking
    	assertTrue(board.checkWinCondition());
    }
    
    @Test
    @DisplayName("Should return true if has 4 or more horizontally")
    void testCheckIfWinConditionHorizontal() {
    	GameBoard.Color[][] actual =  {
    			{GameBoard.Color.RED,null,null,null},
    			{GameBoard.Color.RED,null,null,null},
    			{GameBoard.Color.RED,null,null,null},
    			{GameBoard.Color.RED,null,null,null}
    	};
    	board.Board = actual;
    	board.columns = 4;
    	board.rows = 4;
    	
    	//Checking
    	assertTrue(board.checkWinCondition());
    }
    
    @Test
    @DisplayName("Should return true if draw after move")
    void testCheckIfDrawWithNextMove() {
    	GameBoard.Color[][] actual =  {
    			{null,GameBoard.Color.GREEN},
    			{GameBoard.Color.RED,GameBoard.Color.GREEN}
    	};
    	board.Board = actual;
    	board.columns = 2;
    	board.rows = 2;
    	board.addDisc(0);
    	
    	//Checking
    	assertEquals(GameBoard.Result.DRAW, board.result);
    }
   
    
    @Test
    @DisplayName("Should return true if victory after move")
    void testCheckIfVictoryWithNextMove() {
    	//Backup leaderboards file
    	ArrayList<Score> backup = new ArrayList<Score>();
    	for(Score b : board.leaderboards) {
    		backup.add(b);
    	}    	
    	//Prepare
    	GameBoard.Color[][] actual =  {
    			{null,null,null,null},
    			{GameBoard.Color.RED,null,null,null},
    			{GameBoard.Color.RED,null,null,null},
    			{GameBoard.Color.RED,null,null,null}
    	};
    	board.Board = actual;
    	board.columns = 4;
    	board.rows = 4;
    	board.winner_name = "winner";
    	//Act
    	board.addDisc(0);
    	
    	//Load backup
    	FileOperations.saveFile("leaderboards.txt",backup);
    	
    	//Assert
    	assertEquals(GameBoard.Result.VICTORY, board.result);
    	
    	
    }
    
    @DisplayName("Should return true if has 4 or more diagonally")
	@ParameterizedTest
	@EnumSource(GameBoard.Color.class) 
	void testCheckIfWinConditionDiagonal(GameBoard.Color input) {
    	board.current_color = input;
    	//Prepare
    	GameBoard.Color[][] actual =  {
    			{input,null,null,null},
				{null,input,null,null},
				{null,null,input,null},
				{null,null,null,input}
    	};
    	board.Board = actual;
    	board.columns = 4;
    	board.rows = 4;
    	//Checking
    	assertTrue(board.checkWinCondition());
    }
    
    @ParameterizedTest
    @DisplayName("Should return true if leaderboards score and name is correct")
	@ValueSource(strings = {"Daniel", "John"})
    void testaddToLeaderBoards(String input) {
    	board.score = 120;
    	board.addToLeaderboards(input);
    	Score test = board.leaderboards.get(0);
    	
    	assertAll(
    	() -> assertEquals(120, test.score),
    	() -> assertEquals(input,test.name)
    	);
    	
    }
   
    @DisplayName("Should be true if rotated color is..")
    @ParameterizedTest(name = "{0} loops => {1} ")
    @CsvSource({ "1,GREEN", "2, RED", "3, GREEN" })
    public void testRotateColor(int loops, GameBoard.Color expected) {
    	for(int i = 0; i < loops; i++) {
    		board.rotateColor();
    	}
    	assertEquals(expected, board.current_color);
    }

    @Test
	@DisplayName("Should be true if rows value from saved file is 5")
	public void testSaveGame() {
		GameBoard actual_board = new GameBoard(5,5);
		actual_board.addDisc(0);
		actual_board.addDisc(1);
		actual_board.saveGame();
		ArrayList<String> actual = FileOperations.loadFile("saveconfigfile");

		assertEquals("5",actual.get(0));
	}
    
    @Test
    @DisplayName("Should be true when amount of rows has correctly loaded")
    public void testLoadGame() {
    	GameBoard actual_board = new GameBoard(5,5);
    	actual_board.addDisc(0);
    	actual_board.addDisc(1);
    	actual_board.saveGame();
    	actual_board = new GameBoard(10,10);
    	
    	actual_board.loadGame();
    	assertEquals(5,actual_board.rows);
    }
    
    
    @Test
	@DisplayName("Should be true if rows value from saved file is 5")
	public void testSaveGameNoneDiscs() {
		GameBoard actual_board = new GameBoard(5,5);
		actual_board.saveGame();
		ArrayList<String> actual = FileOperations.loadFile("saveconfigfile");

		assertEquals("5",actual.get(0));
	}

    @Test
	@DisplayName("Should be true when amount of rows has correctly loaded")
    public void testLoadGameNoneDiscs() {
    	GameBoard actual_board = new GameBoard(5,5);
    	actual_board.saveGame();
    	actual_board = new GameBoard(10,10);
    	
    	actual_board.loadGame();
    	assertEquals(5,actual_board.rows);
    }
    
    @ParameterizedTest
	@NullSource
	@DisplayName("Should be true if color is null")
	public void testtoColorNull(String input) {
		GameBoard.Color actual = GameBoard.Color.toColor(input);
		assertNull(actual);
	}


	@ParameterizedTest
	@EmptySource
	@DisplayName("Should be true if color is null")
	public void testtoColorEmpty(String input) {
		GameBoard.Color actual = GameBoard.Color.toColor(input);
		assertNull(actual);
	}
	
	@Test
	@DisplayName("Should return true if movelist has not changed")
	void testAddDiscFull() {
		GameBoard test = new GameBoard(2,2);
		test.addDisc(0);
		test.addDisc(0);
		ArrayList<App.Moves> expected = test.listOfmoves;
		test.addDisc(0);
		assertEquals(expected,test.listOfmoves);
	}
}

