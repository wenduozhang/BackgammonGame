package backgammon;

import java.util.ArrayList;

public class Lane {
	private int LaneIndex;// Index of checker lane(1-24)
	// private String LaneColor;//Lane color(black or white)
	// private String Identfier;//Indetify lane type
	private int numBlack;// number of black checkers on one lane
	private int numWhite;// number of white checkers on one lane
	private ArrayList<Checker> checkersOnLane;

	public Lane(int idx) {
		this.LaneIndex = idx;
		// this.Identfier=flag;
		this.checkersOnLane = new ArrayList<Checker>();

	}

	public int getLaneIndex() {
		return this.LaneIndex;
	}

	public void putChecker(Checker c) {
		checkersOnLane.add(c);
	}

	public Checker getChecker(int index) {
		return checkersOnLane.get(index);
	}

	public void putChecker(Color color) {// One checker can be out into one lane for once
		Checker c = new Checker(color);
		checkersOnLane.add(c);
	}
	
	public void putChecker(Color color, int chkckerNum) {// put multiple checkers into one lane(for init only)
		for(int i=0;i<chkckerNum;i++) {
			Checker c = new Checker(color);
			checkersOnLane.add(c);
		}
		
	}

	public void removeChecker(Checker c) {
		checkersOnLane.remove(c);
	}
	
	

	public int[] countChecker() {
		numBlack = 0;
		numWhite = 0;
		int checkerCount[] = new int[2];
		if (this.checkersOnLane.size() > 0) {
			for (int i = 0; i < this.checkersOnLane.size(); i++) {
				Checker c = checkersOnLane.get(i);
				if (c.getColor().equals("BLACK")) {
					numBlack++;
				} else {
					numWhite++;// Checkers are either black or white
				}
			}
		}
		checkerCount[0] = numBlack;
		checkerCount[1] = numWhite;

		return checkerCount;

	}
	
	public int countCheckerPerLane() {//count the number of checkers on single lane(does not care color of checker)
		return this.checkersOnLane.size();
	}
}
	/**public String checkerColor(){
        //Color of last checker in a lane stands for all checkers' color
    	Checker lastChecker=this.checkersOnLane.get(this.checkersOnLane.size()-1);
    	String lastCheckerColor=lastChecker.getColor();
        return lastCheckerColor;
    }
}**/
