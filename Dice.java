package backgammon;

import java.util.ArrayList;
import java.util.List;

/*
 * The class that describes a dice
 * Key function: simulate rolling a dice and return point number between 1 and 6
 */

public class Dice {

	private String DiceName;
	private List<Integer> dicePoints;

	public Dice(String name) {
		this.DiceName = name;
		dicePoints = List.of(1, 2, 3, 4, 5, 6);
	}

//Optional features:get and set dice name
	/*
	 * public void setName(String name){ this.DiceName=name; }
	 */

	public int Roll() {
		int idx = 1 + (int) (Math.random() * 6);
		return dicePoints.get(idx - 1);
	}

	public String getName() {
		return this.DiceName;
	}

}