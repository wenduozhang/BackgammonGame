package backgammon;
public class Player {

	private int Id;
	private String PlayerName;
	private String refColor;
	private int Points;
	private int pipNum;
	// private ArrayList<Checker> PlayerCheckers;
	// 2 dices for each player
	private Dice DiceA;
	private Dice DiceB;

	public Player(int id, String name, Color color) {
		this.PlayerName = name;
		this.Id = id;
		this.Points = 100;
		this.pipNum = 0;

		this.refColor = color.toString();

		DiceA = new Dice("DiceA");
		DiceB = new Dice("DiceB");
		/*
		 * PlayerCheckers=new ArrayList<Checker>(); for(int i=0; i<15;i++){ Checker
		 * chk=new Checker(color); PlayerCheckers.add(chk); }
		 * 
		 */
	}

	public String getPlayerName() {
		return this.PlayerName;
	}

	public String getPlayerColor() {
		return this.refColor;
	}

	public void checkPoints(int vol) {
		Points -= vol;
	}

	public int getPoint() {
		return this.Points;
	}

	public void setPipNum(int num) {
		this.pipNum += num;
	}

	public int getPipNum() {
		return this.pipNum;
	}
	
	public int getId() {
		return this.Id;
	}

	public int[] RollDice(int times) {// Roll once: return result, roll twice: return sum of 2 results
		int re[] = new int[2];
		switch (times) {
		case 1 -> {// Rolling once: By default use dice A of each player
			re[0] = DiceA.Roll();
			re[1] = 0;
			System.out.println("Player \"" + this.PlayerName + "\": " + "Rolling result of "
					+ DiceA.getName().toString() + ": " + re[0] + " points\n");

		}
		case 2 -> {
			// Rolling twice: Use both dices of each player
			int reA = DiceA.Roll();
			int reB = DiceB.Roll();
			re[0] = reA;
			re[1] = reB;
			System.out.println("Player \"" + this.PlayerName + "\": " + "Rolling result of "
					+ DiceA.getName().toString() + ": " + re[0] + " points\n");
			System.out.println("Player \"" + this.PlayerName + "\": " + "Rolling result of "
					+ DiceB.getName().toString() + ": " + re[1] + " points\n");

		}
		default -> {
			break;
		}

		}
		return re;
	}

	public int[] Dice(int expA, int expB) {// ->The “dice <int> <int>” command which cause the subsequent dice roll to
											// equal the given numbers.
		int reA = 0;
		int reB = 0;
		int re[] = new int[2];
		while (true) {
			reA = DiceA.Roll();
			reB = DiceB.Roll();
			if (reA == expA && reB == expB) {
				break;
			}
		}
		System.out.println("Player \"" + this.PlayerName + "\": " + "Rolling result of " + DiceA.getName().toString()
				+ ": " + re[0] + " points\n");
		System.out.println("Player \"" + this.PlayerName + "\": " + "Rolling result of " + DiceB.getName().toString()
				+ ": " + re[1] + " points\n");
		re[0] = reA;
		re[1] = reB;
		return re;
	}
}
