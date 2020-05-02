package App;
import java.util.ArrayList;

public class Score{
	public double score;
    public String name;
    public double getScore() {
    	return score;
    }
    public Score(double score, String name)
    {
    	this.score = score;
    	this.name = name;
    }
    public String toString() {
    	return name+"\n"+score;
    }
    
	public static ArrayList<Score> toList(ArrayList<String> list) {
		ArrayList<Score> scores = new ArrayList<Score>();
    	Score score;
    	for(int i = 0 ; i < list.size(); i+=2) {
    		score = new Score(
    				Double.parseDouble(list.get(i+1)),
    				list.get(i)
    				);
    		scores.add(score);
    	}
    	return scores;
	}
}
