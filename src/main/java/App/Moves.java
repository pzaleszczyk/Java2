package App;
import java.util.ArrayList;

public class Moves{
    public int row;
    public int column;
    public GameBoard.Color color; 
    
    public Moves(int row, int column, GameBoard.Color color) {
    	this.row = row;
    	this.column = column;
    	this.color = color;
    }
    
    public String toString() {
    	return row+"\n"+column+"\n"+color;
    }
    
    public static ArrayList<Moves> toList(ArrayList<String> list){
    	ArrayList<Moves> moves = new ArrayList<Moves>();
    	Moves move;
    	for(int i = 0 ; i < list.size(); i+=3) {
    		move = new Moves(
    				Integer.parseInt(list.get(i)),
    				Integer.parseInt(list.get(i+1)),
    				GameBoard.Color.toColor(list.get(i+2))
    				);
    		moves.add(move);
    	}
    	return moves;
    }
    
}