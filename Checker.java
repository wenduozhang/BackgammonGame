package backgammon;

public class Checker {

	private String checkerColor;

	public Checker(Color color) {
		// this.Index=idx;
		this.checkerColor = color.toString();
	}

	public String getColor() {
		return this.checkerColor;
	}

}
