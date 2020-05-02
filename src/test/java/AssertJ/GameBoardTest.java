package AssertJ;

import java.util.ArrayList;
import org.assertj.core.api.SoftAssertions;
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

import static org.assertj.core.api.Assertions.*;

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
		SoftAssertions softly = new SoftAssertions();
		softly.assertThat(board.rows).isEqualTo(6);
		softly.assertThat(board.columns).isEqualTo(7);
		softly.assertAll();
	}

	@ParameterizedTest (name = "{0} and {1}")
	@CsvFileSource(resources = "../testdata.csv", numLinesToSkip = 1, delimiter = '\t')
	@DisplayName("Should return true if rows and columns are correct")
	void testNondefaultAmountOfColumnsAndRows(String inr, String inc) {
		int row = Integer.parseInt(inr);
		int column = Integer.parseInt(inc);
		GameBoard nboard = new GameBoard(row, column);
		SoftAssertions softly = new SoftAssertions();
		softly.assertThat(nboard.rows).isEqualTo(row);
		softly.assertThat(nboard.columns).isEqualTo(column);
		softly.assertAll();
	}

	@Test
	@DisplayName("Should return true if color is RED")
	void testAddingRedDiscToBoard() {
		board.addDisc(5);
		assertThat(board.Board[0][5]).isEqualTo(GameBoard.Color.RED);
	}

	@Test
	@DisplayName("Should return true if color is GREEN")
	void testAddingGreenDiscToBoard() {
		board.addDisc(5);
		board.addDisc(5);
		assertThat( board.Board[1][5]).isEqualTo(GameBoard.Color.GREEN);
	}


	@DisplayName("Should return true if list object has correct values")
	@Test
	void testNextMove() {
		board.nextMove(1, 2);
		SoftAssertions softly = new SoftAssertions();
		softly.assertThat(board.listOfmoves.get(0).row).isEqualTo(1);
		softly.assertThat(board.listOfmoves.get(0).column).isEqualTo(2);
		softly.assertThat(board.listOfmoves.get(0).color).isEqualTo(GameBoard.Color.RED);
		softly.assertAll();
	}

	@Test
	@DisplayName("Should return true if both listOfMoves and Board removed move")
	void testUndoMove() {
		board.addDisc(5);
		board.undoMove();

		GameBoard.Color[][] expected1 = new GameBoard.Color[6][7];        
		ArrayList<Moves> expected2 =
				new ArrayList<Moves>();
		SoftAssertions softly = new SoftAssertions();
		softly.assertThat(board.Board).isEqualTo(expected1);
		softly.assertThat( board.listOfmoves).isEqualTo(expected2);
		softly.assertAll();

	}

	@Test
	@DisplayName("Should return true if listOfMoves is empty")
	void testUndoMoveEmpty() {
		board.undoMove();
		assertThat(board.listOfmoves).isEqualTo(new ArrayList<Moves>());
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
		assertThat(board.checkIfArrayFull()).isTrue();
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
		assertThat(board.checkWinCondition()).isTrue();
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
		assertThat(board.checkWinCondition()).isTrue();
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
		assertThat( board.result).isEqualTo(GameBoard.Result.DRAW);
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
		assertThat(board.result).isEqualTo(GameBoard.Result.VICTORY);
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
		assertThat(board.checkWinCondition()).isTrue();
	}


	@ParameterizedTest
	@DisplayName("Should return true if leaderboards score and name is correct")
	@ValueSource(strings = {"Daniel", "John"})
	void testaddToLeaderBoards(String string) {
		board.score = 100;
		board.addToLeaderboards(string);
		Score test = board.leaderboards.get(0);

		SoftAssertions softly = new SoftAssertions();
		softly.assertThat( test.getScore()).isEqualTo(100);
		softly.assertThat(test.name).isEqualTo(string);

		softly.assertAll();

	}



	@DisplayName("Should be true if rotated color is..")
	@ParameterizedTest(name = "{0} loops => {1} ")
	@CsvSource({ "1,GREEN", "2, RED", "3, GREEN" })
	public void testRotateColor(int loops, GameBoard.Color expected) {
		for(int i = 0; i < loops; i++) {
			board.rotateColor();
		}
		assertThat(board.current_color).isEqualTo(expected);
	}



	@Test
	@DisplayName("Should be true if rows value from saved file is 5")
	public void testSaveGame() {
		GameBoard actual_board = new GameBoard(5,5);
		actual_board.addDisc(0);
		actual_board.addDisc(1);
		actual_board.saveGame();
		ArrayList<String> actual = FileOperations.loadFile("saveconfigfile");

		assertThat(actual.get(0)).isEqualTo("5");
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
		assertThat(actual_board.rows).isEqualTo(5);
	}  

	@Test
	@DisplayName("Should be true if rows value from saved file is 5")
	public void testSaveGameNoneDiscs() {
		GameBoard actual_board = new GameBoard(5,5);
		actual_board.saveGame();
		ArrayList<String> actual = FileOperations.loadFile("saveconfigfile");

		assertThat(actual.get(0)).isEqualTo("5");
	}

	@Test
	@DisplayName("Should be true when amount of rows has correctly loaded")
	public void testLoadGameNoneDiscs() {
		GameBoard actual_board = new GameBoard(5,5);
		actual_board.saveGame();
		actual_board = new GameBoard(10,10);

		actual_board.loadGame();
		assertThat(actual_board.rows).isEqualTo(5);
	}

	@ParameterizedTest
	@NullSource
	@DisplayName("Should be true if color is null")
	public void testtoColorNull(String input) {
		GameBoard.Color actual = GameBoard.Color.toColor(input);
		assertThat(actual).isNull();
	}


	@ParameterizedTest
	@EmptySource
	@DisplayName("Should be true if color is null")
	public void testtoColorEmpty(String input) {
		GameBoard.Color actual = GameBoard.Color.toColor(input);
		assertThat(actual).isNull();
	}
	
	@Test
	@DisplayName("Should return true if movelist has not changed")
	void testAddDiscFull() {
		GameBoard test = new GameBoard(2,2);
		test.addDisc(0);
		test.addDisc(0);
		ArrayList<App.Moves> expected = test.listOfmoves;
		test.addDisc(0);
		assertThat(test.listOfmoves).isEqualTo(expected);
	}

}

