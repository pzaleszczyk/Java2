package App;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class GameBoard {

	public Color Board[][];
	public ArrayList<Moves> listOfmoves;
	public ArrayList<Score> leaderboards = new ArrayList<Score>(); 

	public int rows;
	public int columns;
	public double score;
	public String winner_name;
	public Color current_color;
	public Result result;

	public enum Color {
		RED, GREEN;

		public static Color toColor(String color) {
			if(color != null)
				switch(color) {
				case "RED" : return RED;
				case "GREEN" : return GREEN;
				}
			return null;
		}
	}


	public enum Result {
		NONE, DRAW, VICTORY;
	}

	public GameBoard() {
		this.rows = 6;
		this.columns = 7;
		createGameboard();
	}

	private void createGameboard() {
		result = Result.NONE;
		ArrayList<String> list = FileOperations.loadFile("leaderboards.txt");
		leaderboards = Score.toList(list);
		listOfmoves = new ArrayList<Moves>();
		current_color = Color.RED;
		Board = new Color[rows][columns];
	}

	public GameBoard(int rows, int columns) {
		if(rows < 1 || columns < 1)
			throw new IllegalArgumentException();
		this.rows = rows;
		this.columns = columns;
		createGameboard();
	}

	public void addDisc(int column){
		if(column < 0) {
			throw new IllegalArgumentException();
		}
		for(int i = 0 ; i < rows; i ++) {
			if(Board[i][column] == null) {
				Board[i][column] = current_color;
				nextMove(i,column);
				return;
			}
		}
		System.out.println("Column is full");
	}

	public void rotateColor() {
		if(current_color == Color.RED)
			current_color = Color.GREEN;
		else
			current_color = Color.RED;
	}

	public void nextMove(int row, int column) {
		if(column < 0 || row < 0) {
			throw new IllegalArgumentException();
		}
		saveMove(row, column);
		if(checkWinCondition()) {
			result = Result.VICTORY;
			showBoard();
			System.out.println(current_color.toString()+" won!");
			calculateScore();
			addToLeaderboards(winner_name);
			FileOperations.saveFile("leaderboards.txt", leaderboards);
		}
		if(checkIfArrayFull()){
			result = Result.DRAW;
			System.out.println("Draw!");
		}
		showBoard();
		rotateColor();
	}

	private void saveMove(int row, int column) {
		Moves move =
				new Moves(row,column,current_color);
		listOfmoves.add(move);
	}

	public void undoMove() {
		if(listOfmoves.isEmpty()) {
			System.out.println("List of moves is empty");
			return;
		}		
		int row = listOfmoves.get(0).row;
		int column = listOfmoves.get(0).column;
		listOfmoves.remove(0);
		Board[row][column] = null;
		rotateColor();
	}

	private double calculateScore() {
		int score = 0;
		for(Color[] a : Board)
			for(Color b : a)
			{
				if(b==current_color)
					score++;
			}
		return score/(rows*columns);
	}

	private boolean checkWinDiag() {
		int count = 0;
		for(int col = 0; col < this.columns ; col++)
			for(int row = 0; row < this.rows; row++) {

				count = 0;
				for(int i = 0; i < 5; i ++)
					//Bounds
					if(row+i >= 0 && row+i < rows && col+i >= 0 && col+i < columns) {
						if(Board[row+i][col+i] == current_color) {
							count++;
							if(count >= 4)
								return true;
						}
						else
							count = 0;
					}
			}
		return false;
	}

	private boolean checkWinVert() {
		int count = 0;
		for(int col = 0; col < this.columns ; col++) {
			count = 0;
			for(int row = 0; row < this.rows; row++) {
				if(Board[row][col] == current_color) {
					count++;
					if(count >= 4)
						return true;
				}
				else {
					count = 0;
				}
			}
		}
		return false;
	}

	private boolean checkWinHoriz() {
		int count = 0;
		for(int row = 0; row < this.rows; row++) {
			count = 0;
			for(int col = 0; col < this.columns ; col++)
				if(Board[row][col] == current_color) {
					count++;
					if(count >= 4)
						return true;
				}
				else {		
					count=0;
				}
		}
		return false;
	}

	public boolean checkWinCondition() {
		if(checkWinHoriz())
			return true;
		if(checkWinVert())
			return true;
		if(checkWinDiag())
			return true;
		return false;
	}

	public boolean checkIfArrayFull() {
		for(Color[] a : Arrays.asList(Board)) {
			for(Color b : a) {
				if(b==null)
					return false;
			}
		}
		return true;
	}

	public void addToLeaderboards(String name) {
		if(name == null || name == "")
			throw new IllegalArgumentException();
		Score record = new Score(score,name);
		leaderboards.add(record);
		leaderboards.sort(Comparator.comparing(Score::getScore).reversed());

	}

	public void saveGame() {
		FileOperations.saveFile("savefile", listOfmoves);
		ArrayList<Integer> config = new ArrayList<Integer>();
		config.add(rows);
		config.add(columns);
		FileOperations.saveFile("saveconfigfile", config);
	}

	public void loadGame() {
		ArrayList<String> config = FileOperations.loadFile("saveconfigfile");
		ArrayList<String> list = FileOperations.loadFile("savefile");
		listOfmoves = Moves.toList(list);

		int rows = Integer.parseInt(config.get(0));
		int columns = Integer.parseInt(config.get(1));

		Color new_Board[][] = new Color[rows][columns];

		for(Moves move : listOfmoves) {
			new_Board[move.row][move.column] = move.color; 
		}
		Board = new_Board;
		this.rows = rows;
		this.columns = columns;
	}


	public void showLeaderboards() {
		for(Score a : leaderboards) {
			System.out.println(a.name + " : " + a.score);
		}
	}

	public void showBoard() {
		System.out.println("BOARD: ");
		for(Color[] a : Board) {
			for(Color b : a) {
				if(b == null)
					System.out.print(" []");
				else
					System.out.print(" "+b);
			}
			System.out.println();
		}
	}

	//	public static void main(String args[]) {
	//		GameBoard start = new GameBoard(2,2);
	//
	//		start.addDisc(0);
	//		start.addDisc(0);
	//		start.addDisc(1);
	//		start.addDisc(1);
	//
	//		start.addDisc(0);
	//		
	//		start.saveGame();
	//
	//	}

}
