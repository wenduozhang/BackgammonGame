package backgammon;

import java.util.Scanner;

public class BackgammonGameProc {
	private GameBoard board;
	private Player PlayerA;
	private Player PlayerB;

	private String nameA;
	private Color colorA;
	private String nameB;
	private Color colorB;
	private int order;
	private int doubleValue;
	private boolean isDoublingRefused;

	private char inBuf;// input char receiver
	
	public BackgammonGameProc() {
		
	}

	public void welcomeAndInitPlayer() {
		// String Win;
		Scanner s = new Scanner(System.in);
		char temp = 0;//temp char to record necessary item

		System.out.println("Welcome to this Backgammon game!\n");
		System.out.println("Type Y to set up your player name, or type Q to quit.\n");
		inBuf = s.next().charAt(0);
		while (!(inBuf == 'Y' || inBuf == 'y' || inBuf == 'Q' || inBuf == 'q')) {
			System.out.println("Bad input. Please try again.\n");
			inBuf = s.next().charAt(0);
		}

		if (inBuf == 'Q' || inBuf == 'q') {
			System.out.println("Byebye\n");
			System.exit(0);
		}
		if (inBuf == 'Y' || inBuf == 'y') {
			System.out.println("Setting up player 1 out of 2\n");
			System.out.println("Input a name for player 1:\n");
			s.nextLine();// clear input
			if (s.hasNext()) {
				nameA = s.next();
			} else {
				nameA = "PlayerA";// If no input, set as default value
			}
			System.out.println("Select from black or white to represent you(B or b for black, W or w for white):\n");
			s.nextLine();// clear input
			if (s.hasNext()) {
				inBuf = s.next().charAt(0);
				while (!(inBuf == 'B' || inBuf == 'b' || inBuf == 'W' || inBuf == 'w')) {
					System.out.println("Bad choice. Wanna try again(B or b for black, W or w for white)?\n");
					inBuf = s.next().charAt(0);
				}
				if (inBuf == 'B' || inBuf == 'b') {
					colorA = Color.BLACK;// Color identfier is limited to W/w(for white)/B/b(for black)
				} else {
					colorA = Color.WHITE;
				}
				temp=inBuf;//record current color choice
			}
			s.nextLine();// clear input

			System.out.println("Setting up player 2 out of 2\n");
			System.out.println("Input a name for player 2:\n");
			if (s.hasNext()) {
				String nameBuf = s.next();
				while (nameBuf.equals(nameA)) {
					System.out.println("You cannot have the same name with player 1! Select another name.\n");
					nameBuf = s.next();
				}
				nameB = nameBuf;
			} else {
				nameB = "PlayerB";// If no input, set as default value
			}
			System.out.println("Select from black or white to represent you(B or b for black, W or w for white):\n");
			s.nextLine();// clear input
			if (s.hasNext()) {
				inBuf = s.next().charAt(0);
				while (!(inBuf == 'B' || inBuf == 'b' || inBuf == 'W' || inBuf == 'w')) {
					System.out.println("Bad choice. Wanna try again(B or b for black, W or w for white)?\n");
					inBuf = s.next().charAt(0);

				}
				while (inBuf == temp) {
					System.out.println("You cannot choose the same color as player 1, try to select the other one.\n");
					inBuf = s.next().charAt(0);
				}
				if (inBuf == 'B' || inBuf == 'b') {
					colorB = Color.BLACK;
				} else {
					colorB = Color.WHITE;
				}
			}
		}
	}

	public void initGame() {
		PlayerA = new Player(1, nameA, colorA);
		PlayerB = new Player(2, nameB, colorB);
		System.out.println("Players recorded.\n");

		board = new GameBoard();
		board.initChecker(colorA, colorB);
		board.showBoard(PlayerA, PlayerB);
	}

	public void playGame() {
		String Win;//Gaming state on winning
		// Add decision on whether game is over
		DecideToGo();
		// Double=Doubling(order);
		Doubling(order);
		// For test ONLY, decision on whether all checkers at player's home to be added
		do {// ->isAllCheckersAtHome
			Move(order, PlayerA, PlayerB);
			Win = checkWinning(PlayerA, PlayerB, order);
		} while (Win.equals("NoWin"));
		showResult(PlayerA, PlayerB);
	}
	/*
	 * Give out information on decide moving order, call Decide function for action,
	 * and make decision.
	 * 
	 * @param Global scanner s The scanner to receive user input.
	 *
	 * @return {@code order} order flag for who to go first, 0 for player 1 go first
	 * and otherwise 1.
	 */

	public void DecideToGo() {// switch order: 0-> A first then B; 1-> B first then A
		
		System.out.println("Now decide who is the first to go.\n");
		System.out.println("You need to roll your dice, the one with smaller points go first.\n");
		int desicionOrder = decideOrder(PlayerA, PlayerB);
		if (desicionOrder == 1) {
			System.out.println(PlayerB.getPlayerName() + " go first.\n");
			order = 1;// B first then A
		}
		if (desicionOrder == 0) {
			System.out.println(PlayerA.getPlayerName() + " go first.\n");
			order = 0;// A first then B
		}
	}

	/*
	 * Function that performs decision action, including a feature that let players
	 * roll their dice once, decide who to go first depending on whose point is
	 * smaller.
	 * 
	 * @param Global scanner s, 2 player object The scanner to receive user input,
	 * player objects that perform dice rolling action and return points.
	 *
	 * @return {@code compare} order flag for who to go first, 0 for player 1 go
	 * first and otherwise 1.
	 */
	public int decideOrder(Player p1, Player p2) {
		int pointA = 0;
		int pointB = 0;
		int compare = 0;
		String cmdl;
		Scanner s = new Scanner(System.in);
		do {
			if (pointA == 0 || pointB == 0) {
				System.out.println("Now decide your playing order..\n");
			} else {
				System.out.println(
						"You 2 have the same points. You need to roll dice once more to determine your moving order.\n");
			}

			System.out.println("It's " + PlayerA.getPlayerName() + "'s turn. Type 'dice' to roll your dice.\n");
			cmdl = s.next();
			while (!cmdl.equals("dice")) {
				System.out.println("Invalid input. Try again?\n");
				cmdl = s.next();
			}
			pointA = PlayerA.RollDice(1)[0];
			System.out.println("It's " + PlayerB.getPlayerName() + "'s turn. Type 'dice' to roll your dice.\n");
			cmdl = s.next();
			while (!cmdl.equals("dice")) {
				System.out.println("Invalid input. Try again?\n");
				cmdl = s.next();
			}
			pointB = PlayerB.RollDice(1)[0];
		} while (pointA == pointB);

		if (pointA > pointB) {
			compare = 1;// B smaller, B go first
		} else if (pointA < pointB) {
			compare = 0;// A smaller, A go first
		}
		return compare;
		
	}

	/*
	 * Function that prepares for moving checkers, including a feature that let
	 * players roll their dice twice, decide moving step(s) of each player, Also let
	 * players select 2 lanes at most as mocing start point.
	 * 
	 * @param Global scanner s, 2 player object, order flag The scanner to receive
	 * user input, player objects that perform dice rolling, and order flag that
	 * decide players' moving order.
	 *
	 * @return No return
	 */
	public void Move(int turn, Player p1, Player p2) {
		Player[] moveTurn = new Player[2];
		int diceNum[] = new int[2];
		char moveWay = 0;
		//int moveFrom2 = 0;
		String cmd;
		Scanner s = new Scanner(System.in);
		if (turn == 0) {
			moveTurn[0] = p1;// A first, then B
			moveTurn[1] = p2;
		}
		if (turn == 1) {
			moveTurn[1] = p1;
			moveTurn[0] = p2;// B first, then A
		}
		for (int i=0;i<moveTurn.length;i++){
			int j=moveTurn.length-1-i;
			Player curPlayer=moveTurn[i];
			Player opponent=moveTurn[j];
			System.out.println(
					"------------------------------------------------------------------------------------------------------");
			System.out.println(
					"Current player: " + curPlayer.getPlayerName() + ", Current doubling: x" + doubleValue + "\n");
			System.out.println(
					"------------------------------------------------------------------------------------------------------");
			board.showBoard(p1, p2);
			System.out.println(curPlayer.getPlayerName() + ": It's your turn. You may:\n");
			while(true) {
			System.out.println("Type 'pip' to check how many steps you've moved\n");
			System.out.println("or type 'roll' to roll your dices and decide your move steps.\n");
			// s.nextLine();
			
				cmd = s.next();
				while (!(cmd.equals("ROLL") ||cmd.equals( "roll") || cmd.equals("PIP") ||cmd.equals("pip"))) {
					System.out.println("Bad command. Please try again \n");
					cmd = s.next();
				}
				
				if(cmd.equals("PIP")||cmd.equals("pip")){
					int pipval = curPlayer.getPipNum();
					System.out.println("Your current pip number: " + pipval + "\n");
					//break;
				}
				if(cmd.equals("ROLL")||cmd.equals("roll")){
					diceNum = curPlayer.RollDice(2);
					System.out.println(curPlayer.getPlayerName() + ": Play " + diceNum[0] + " - " + diceNum[1] + "\n");
					break;
				}
				}
			
			while (board.needMoveBack(curPlayer)) {
				System.out.println(curPlayer.getPlayerName()+ ": You have checker(s) been attacked. Move them back before your other movement.\n");
				showHint_moveBack(curPlayer, diceNum[0], diceNum[1], opponent);
			}
			System.out.println(curPlayer.getPlayerName()+ ": Select how you want to move:\n");
			System.out.println("A) Move one checker using all 2 dice points \n");
			System.out.println("B) Move 2 checkers with 2 dice points respectively \n");
			if (s.hasNext()) {
				moveWay = s.next().charAt(0);
				//moveFrom1 += 1;
				
			}
			while(!(moveWay=='A'||moveWay=='B')) {
				System.out.println("Invalid choice. Select again.\n");
				System.out.println(curPlayer.getPlayerName()+ ": Select how you want to move:\n");
				System.out.println("A) Move one checker using all 2 dice points \n");
				System.out.println("B) Move 2 checkers with 2 dice points respectively \n");
				if (s.hasNext()) {
					moveWay = s.next().charAt(0);
				}
			}
			System.out.println("Recorded your idea.\n");
			System.out.println("To make movement, you can type 'hint' for movement choices.\n");
			switch(moveWay){
				case 'A'->{
					cmd = s.next();
					while (!cmd.equals("hint")) {
						System.out.println("Bad command. Please try again \n");
						cmd = s.next();
					}
					showHint(curPlayer, opponent, diceNum[0], diceNum[1], "Singlechecker");
					break;
				}
				case 'B'->{
					cmd = s.next();
					while (!cmd.equals("hint")) {
						System.out.println("Bad command. Please try again \n");
						cmd = s.next();
					}
					showHint(curPlayer, opponent, diceNum[0], diceNum[1], "Multichecker");
					break;
				}
			}		
			System.out.println("Current doubling: x" + doubleValue + "\n");
			board.showBoard(p1, p2);
	}
		s.close();
	}

	public int pip(Player p) {
		int pipval = p.getPipNum();
		return pipval;
	}

	/*
	 * Function that lets players to choose a doubling within 1-4, by default the
	 * who goes first asks for doubling. Also lets players decide whether to accept
	 * the doubling points.
	 * 
	 * @param Global scanner s, order flag The scanner to receive user input, order
	 * flag that indicates who to ask for doubling.
	 * 
	 * 
	 *
	 * @return Doubling parameter if doubling point is accepted, or gaming result on
	 * screen if not
	 * 
	 * 
	 * 
	 */
	public void Doubling(int turn){
        int times=1;//default doubling=1
        char inBuf;
        this.isDoublingRefused=false;
        Scanner s = new Scanner(System.in);
        if(turn==0){//A decide first
            System.out.println( PlayerA.getPlayerName() + ": which doubling number you prefer(must be integer, no more than 4)? \n");
            times= s.nextInt();
            while(times<1||times>4){
                System.out.println(PlayerA.getPlayerName()+ ": Bad choice. Please try again \n");
                times = s.nextInt();
            }
            System.out.println(PlayerB.getPlayerName()+ ": Current doubling: x"+times+" Accept?(Y/N)");  
            s.nextLine();   
            inBuf= s.nextLine().charAt(0);
            while (!(inBuf == 'Y' || inBuf == 'N')) {
                System.out.println("Bad input. Please try again.\n");
                inBuf = s.nextLine().charAt(0);
            }
            if(inBuf=='Y'){
                doubleValue=times;
            }
            else{
                System.out.println(PlayerB.getPlayerName()+ ": Lost your game because of not accept the doubling number.\n");
                this.isDoublingRefused=true;
                checkWinning(PlayerA, PlayerB, turn);
                showResult(PlayerA, PlayerB);
            }
        }
        if (turn == 1) {//B decide first
            System.out.println(PlayerB.getPlayerName()+ ": which doubling number you prefer(must be integer, no more than 4)? \n");
            times = s.nextInt();
            while (times < 1 || times > 4) {
                System.out.println(PlayerB.getPlayerName()+ ": bad choice. Please try again \n");
                times = s.nextInt();
            }
            System.out.println(PlayerA.getPlayerName()+ ": Current doubling: x" + times + " Accept?(Y/N)");
             s.nextLine();
            inBuf = s.nextLine().charAt(0);
            while(!(inBuf =='Y'||inBuf =='y'||inBuf =='N'||inBuf =='n')){
                System.out.println("Bad input. Please try again.\n");
                inBuf = s.nextLine().charAt(0);
            }
            if (inBuf == 'Y') {
                doubleValue= times;
            } else {
                System.out.println(PlayerA.getPlayerName()+ ": Lost your game because of not accept the doubling number.\n");
                this.isDoublingRefused=true;
                checkWinning(PlayerA,  PlayerB, turn);
                showResult(PlayerA, PlayerB);
            }
        }
        System.out.println("Confirmed doubling: x"+ doubleValue+"\n");
        //return times;
    }

	public String checkWinning(Player p1, Player p2, int playOrder) {// By default, player A goes 24-1 and player B goes
																		// 1-24
		String WinType = null;
		// winning justification
		if (board.checkMovedOffChecker(p1.getPlayerColor()) < 15 && board.checkMovedOffChecker(p2.getPlayerColor()) < 15) {// Both players have not moved all checkers
			WinType = WinningType.NoWin.toString();
		}
		if (board.checkMovedOffChecker(p1.getPlayerColor()) == 15) {
			if (board.checkMovedOffChecker(p2.getPlayerColor()) > 0) {
				WinType = WinningType.Lost.toString();
				calcWinningPoints(p2, WinType);
			}
			if (board.checkMovedOffChecker(p2.getPlayerColor()) == 0
					&& board.checkAttackedChecker(p2.getPlayerColor()) == 0) {
				WinType = WinningType.Gammoned.toString();
				calcWinningPoints(p2, WinType);
			}
			if (board.checkMovedOffChecker(p2.getPlayerColor()) == 0
					&& board.checkAttackedChecker(p2.getPlayerColor()) > 0) {
				WinType = WinningType.Backgammoned.toString();
				calcWinningPoints(p2, WinType);
			}

		}

		if (board.checkMovedOffChecker(p2.getPlayerColor()) == 15) {
			if (board.checkMovedOffChecker(p1.getPlayerColor()) > 0) {
				WinType = WinningType.Lost.toString();
				calcWinningPoints(p1, WinType);
			}
			if (board.checkMovedOffChecker(p1.getPlayerColor()) == 0
					&& board.checkAttackedChecker(p2.getPlayerColor()) == 0) {
				WinType = WinningType.Gammoned.toString();
				calcWinningPoints(p1, WinType);
			}
			if (board.checkMovedOffChecker(p2.getPlayerColor()) == 0
					&& board.checkAttackedChecker(p1.getPlayerColor()) > 0) {
				WinType = WinningType.Backgammoned.toString();
				calcWinningPoints(p1, WinType);
			}

		}

		if (this.isDoublingRefused == true) {
			if (playOrder == 0) {
				// A play first, A propose doubling, only B would refuse
				WinType = WinningType.RefuseDouble.toString();
				calcWinningPoints(p2, WinType);
			} else {// playOrder can only be 1(B play first), B propose doubling, only A would
					// refuse
				WinType = WinningType.RefuseDouble.toString();
				calcWinningPoints(p1, WinType);
			}
		}
		return WinType;
	}

	public void calcWinningPoints(Player p, String type) {// Lose points check out
		switch (type) {
		case "Lost" -> {
			p.checkPoints(1 * doubleValue);
			break;
		}
		case "Gammoned" -> {
			p.checkPoints(2 * doubleValue);
			break;
		}
		case "Backgammoned" -> {
			p.checkPoints(3 * doubleValue);
			break;
		}
		case "RefuseDouble" -> {
			p.checkPoints(1 * doubleValue);
			break;
		}
		}
	}

	public int showResult(Player p1, Player p2) {
		Scanner c = new Scanner(System.in);
		System.out.println("Game Over!\n");
		System.out.println("-----------------------------------------------------------------\n");
		System.out.println("Winning points result:\n");
		System.out.println(p1.getPlayerName() + ": " + p1.getPoint() + " points. \n");
		System.out.println(p2.getPlayerName() + ": " + p2.getPoint() + " points.\n");
		if(p1.getPoint() > p2.getPoint()) {
			System.out.println("Winner for current game:" + p1.getPlayerName()+'\n');
		}
		if(p1.getPoint() < p2.getPoint()) {
			System.out.println("Winner for current game:" + p2.getPlayerName()+'\n');
		}
		else {
			System.out.println("No Winner in current game \n");
		}
		System.out.println("Press any key to leave the game.\n");
		if (c.hasNext()) {
			System.out.println("Game End.\n");
			c.close();
		}
		
		return 0;
	}

	/*
	 * Function that calculates players' moving steps and take out different cases
	 * for listing legal hints
	 * 
	 * @param Player object, Index 2 lanes that move checkers from, moving steps
	 * from rolling dice.
	 *
	 * 
	 * 
	 *
	 * @return -
	 * 
	 * @outputs: Strings on screen in format: Play x-x, shows hint on movement starting point and destination
	 * 
	 */
	public void showHint(Player p, Player opponent, int step1, int step2, String moveMethod) {
		// By default, player A goes 24-1 and player B goes 1-24
		int Begin1;// Lane that player move from
		int Begin2;// Lane that player move from
		int moveLength1;
		int moveLength2;
		int moveLength3;

		Scanner c = new Scanner(System.in);
		char Choice=0;
		int choiceStep=0;

		switch (p.getId()) {
		case 1 -> {// player A goes lane index 24-1
			if (!board.canMoveoff(p)) {
				if (moveMethod.equals("Singlechecker")) {// Only move from one lane
					System.out.println(p.getPlayerName()+ ": Select a lane you want to move from:\n");
					Begin1=c.nextInt();
					while(!(Begin1>0&&Begin1<25)) {
						System.out.println("Invalid place. Select again.\n");
						Begin1=c.nextInt();
					}
					while(!(board.countCheckerOnLane(Begin1, p.getPlayerColor()) >= 1)) {
						System.out.println("Invalid place. Select again.\n");
						Begin1=c.nextInt();
					}// check if has enough number of self-side checkers to move

						if (step1 != step2) {// 2 steps inequal: by default consider the larger step
							System.out.println("You may want:\n");
							System.out.println("1) Move steps of dice 1 points, then dice 2 \n");
							System.out.println("2) Move steps of dice 2 points, then dice 1 \n");
							if (c.hasNext()) {
								choiceStep = c.nextInt();
							}
							while(!(choiceStep==1||choiceStep==2)) {
								System.out.println("Invalid choice. Select again.\n");
								System.out.println("1) Move steps of dice 1 points, then dice 2 \n");
								System.out.println("2) Move steps of dice 2 points, then dice 1 \n");
								if (c.hasNext()) {
									choiceStep = c.nextInt();
								}
							}
							switch(choiceStep) {
							case 1->{
								moveLength1 = step1;
								moveLength2=step2;
								listHintAndMove_StartWithSingleLane(Begin1, moveLength1, moveLength2, p, opponent, 1, false);
								break;
						}
							case 2->{
								moveLength1=step2;
								moveLength2=step1;
								listHintAndMove_StartWithSingleLane(Begin1, moveLength1, moveLength2, p, opponent, 1, false);
								break;
							}
						}	
						}
						if (step1 == step2) {
							System.out.println("You have same dice points and can move twice of bonus steps.\n");
							moveLength1 = 2 * (step1 + step2);// check 3 cases: 1->sum of dice numbers
							moveLength2=step1;
							listHintAndMove_StartWithSingleLane(Begin1, moveLength1, moveLength2, p, opponent, 1, true);
						}
				}
				if (moveMethod.equals("Multichecker")) {// Only move from lane 2
					System.out.println(p.getPlayerName()+ ": Select how you want to move:\n");
					System.out.println("A) Move 2 checkers from one lane \n");
					System.out.println("B) Move 2 checkers from 2 different lanes \n");
					if (c.hasNext()) {
						Choice = c.next().charAt(0);
					}
					while(Choice!='A'||Choice!='B') {
						System.out.println("Invalid choice. Select again.\n");
						System.out.println(p.getPlayerName()+ ": Select how you want to move:\n");
						System.out.println("A) Move 2 checkers from one lane \n");
						System.out.println("B) Move 2 checkers from 2 different lanes. \n");
						if (c.hasNext()) {
							Choice = c.next().charAt(0);
						}
					}
					switch(Choice) {
					case 'A'->{
						System.out.println(p.getPlayerName()+ ": Select a lane you want to move from:\n");
						Begin1=c.nextInt();
						while(Begin1<=0||Begin1>=25) {
							System.out.println("Invalid place. Select again.\n");
							Begin1=c.nextInt();
						}
						if (board.countCheckerOnLane(Begin1, p.getPlayerColor()) >= 2) {// has enough number of self-side checkers to move
							if (step1 != step2) {// 2 steps inequal: by default consider the larger step
								moveLength1 = step1;
								moveLength2 = step2;
								listHintAndMove_StartWithSingleLane(Begin1, moveLength1, moveLength2, p, opponent, 2, false);
							}
							if (step1 == step2) {
								System.out.println("You have same dice points and can move twice of bonus steps.\n");
								moveLength1 = 3 * step1;// check 3 cases: 1->sum of dice numbers
								moveLength2 = 2 * step1;// check 3 cases: 2->bigger one of dice numbers
								moveLength3 = step1;// check 3 cases: 3->smaller one of dice numbers
								System.out.println(p.getPlayerName()+ ": Select how you want to move:\n");
								System.out.println("1) Move 3 times of dice point for first checker and once of dice point of second checker \n");
								System.out.println("2) Move 2 times of dice point for both checkers \n");
								System.out.println("3) Move once of dice point for first checker and 3 times of dice point of second checker \n");
								if (c.hasNext()) {
									choiceStep = c.nextInt();
								}
								while(choiceStep!=1||choiceStep!=2||choiceStep!=3) {
									System.out.println("Invalid choice. Select again.\n");
									System.out.println(p.getPlayerName()+ ": Select how you want to move:\n");
									System.out.println("1) Move 3 times of dice point for first checker and once of dice point of second checker \n");
									System.out.println("2) Move 2 times of dice point for both checkers \n");
									System.out.println("3) Move once of dice point for first checker and 3 times of dice point of second checker \n");
									if (c.hasNext()) {
										choiceStep = c.nextInt();
									}
								}
								switch(choiceStep) {
									case 1->{
										listHintAndMove_StartWithSingleLane(Begin1, moveLength1, moveLength3, p, opponent, 2, true);
										break;
								}
									case 2->{
										listHintAndMove_StartWithSingleLane(Begin1, moveLength2, moveLength1, p, opponent, 2, true);
										break;
									}
									case 3->{
										listHintAndMove_StartWithSingleLane(Begin1, moveLength3, moveLength1, p, opponent, 2, true);
										break;
									}
								}
								
						}
					}
					}	
					case 'B'->{ // Move from both lane 1 and lane 2
						System.out.println(p.getPlayerName()+ ": Select lane 1 you want to move from:\n");
						Begin1=c.nextInt();
						System.out.println(p.getPlayerName()+ ": Select lane 2 you want to move from:\n");
						Begin2=c.nextInt();
						while(!((Begin1>0&&Begin1<25)&&(Begin2>0&&Begin2<25))) {
							System.out.println("Invalid place. Select again.\n");
							System.out.println(p.getPlayerName()+ ": Select lane 1 you want to move from:\n");
							Begin1=c.nextInt();
							System.out.println(p.getPlayerName()+ ": Select lane 2 you want to move from:\n");
							Begin2=c.nextInt();
						}
						while(!(board.countCheckerOnLane(Begin1, p.getPlayerColor()) >= 1&& board.countCheckerOnLane(Begin2, p.getPlayerColor()) >= 1)) {
							System.out.println("Invalid place. Select again.\n");
							System.out.println(p.getPlayerName()+ ": Select lane 1 you want to move from:\n");
							Begin1=c.nextInt();
							System.out.println(p.getPlayerName()+ ": Select lane 2 you want to move from:\n");
							Begin2=c.nextInt();
						}
						// CHeck: The lane to move from has at least 1 checker for moving
						if (step1 != step2) {
							moveLength1 = step1;// check 3 cases: 1->sum of dice numbers: ignored here
							moveLength2 = step2;// check 3 cases: 2->bigger one of dice numbers
							listHintAndMove_StartWithMultiLane(Begin1, Begin2, moveLength1, moveLength2, p, opponent);
						}

						if (step1 == step2) {
							System.out.println("You have same dice points and can move twice of bonus steps.\n");
							moveLength1 = 3 * step1;// check 3 cases: 1->3*dice numbers
							moveLength2 = 2 * step1;// check 3 cases: 2->2*dice number
							moveLength3 = step1;// check 3 cases: 3->1*dice number
							System.out.println(p.getPlayerName()+ ": Select how you want to move:\n");
							System.out.println("1) Move 3 times of dice point for one checker and once of dice point of another checker \n");
							System.out.println("2) Move 2 times of dice point for both checkers \n");
							if (c.hasNext()) {
								choiceStep = c.nextInt();
							}
							while(choiceStep!=1||choiceStep!=2) {
								System.out.println("Invalid choice. Select again.\n");
								System.out.println(p.getPlayerName()+ ": Select how you want to move:\n");
								System.out.println("1) Move 3 times of dice point for one checker and once of dice point of another checker \n");
								System.out.println("2) Move 2 times of dice point for both checkers \n");
								if (c.hasNext()) {
									choiceStep = c.nextInt();
								}
							}
							switch(choiceStep) {
							case 1->{
								listHintAndMove_StartWithMultiLane(Begin1, Begin2, moveLength1, moveLength3, p, opponent);
								break;
						}
							case 2->{
								listHintAndMove_StartWithMultiLane(Begin1, Begin2, moveLength2, moveLength2, p, opponent);
								break;
							}
						}
						}
				}
					}

			}
			if (board.canMoveoff(p)) {
				int place1, place2;
				System.out.println("You now can move off your checkers.\n");
				System.out.println("Select 2 lanes you want to look at.\n");
				place1=c.nextInt();
				place2=c.nextInt();
				Begin1 = Math.max(place1, place2);
				Begin2 = Math.min(place1, place2);
				moveLength2 = Math.max(step1, step2);// check 3 cases: 2->bigger one of dice numbers
				moveLength3 = Math.min(step1, step2);// check 3 cases: 3->smaller one of dice numbers
				while (Begin1 > 6 || Begin2 > 6) {
					System.out.println("You can not move from outside of your home area. Please try again.\n");
					if (Begin1 > 6) {
						Begin1 = c.nextInt();
					}
					if (Begin2 > 6) {
						Begin2 = c.nextInt();
					}
				}
				listHint_MoveOff(Begin1, Begin2, moveLength2, moveLength3, p, opponent);

			}
		}
		}
		case 2 -> {
			if (!board.canMoveoff(p)) {
				if (moveMethod.equals("Singlechecker")) {// Only move from one lane
					System.out.println(p.getPlayerName()+ ": Select a lane you want to move from:\n");
					Begin1=c.nextInt();
					while(!(Begin1>0&&Begin1<25)) {
						System.out.println("Invalid place. Select again.\n");
						Begin1=c.nextInt();
					}
					while(!(board.countCheckerOnLane(Begin1, p.getPlayerColor()) >= 1)) {
						System.out.println("Invalid place. Select again.\n");
						Begin1=c.nextInt();
					}// check if has enough number of self-side checkers to move

						if (step1 != step2) {// 2 steps inequal: by default consider the larger step
							
							System.out.println("You may want:\n");
							System.out.println("1) Move steps of dice 1 points, then dice 2 \n");
							System.out.println("2) Move steps of dice 2 points, then dice 1 \n");
							if (c.hasNext()) {
								choiceStep = c.nextInt();
							}
							while(choiceStep!=1||choiceStep!=2) {
								System.out.println("Invalid choice. Select again.\n");
								System.out.println("1) Move steps of dice 1 points, then dice 2 \n");
								System.out.println("2) Move steps of dice 2 points, then dice 1 \n");
								if (c.hasNext()) {
									choiceStep = c.nextInt();
								}
							}
							switch(choiceStep) {
							case 1->{
								moveLength1 = step1;
								moveLength2=step2;
								listHintAndMove_StartWithSingleLane(Begin1, moveLength1, moveLength2, p, opponent, 1, false);
								break;
						}
							case 2->{
								moveLength1=step2;
								moveLength2=step1;
								listHintAndMove_StartWithSingleLane(Begin1, moveLength1, moveLength2, p, opponent, 1, false);
								break;
							}
						}
						}
						if (step1 == step2) {
							System.out.println("You have same dice points and can move twice of bonus steps.\n");
							moveLength1 = 2 * (step1 + step2);// check 3 cases: 1->sum of dice numbers
							moveLength2=step1;
							listHintAndMove_StartWithSingleLane(Begin1, moveLength1, moveLength2, p, opponent, 1, true);
						}
				}
				if (moveMethod.equals("Multichecker")) {// Only move from lane 2
					System.out.println(p.getPlayerName()+ ": Select how you want to move:\n");
					System.out.println("A) Move 2 checkers from one lane \n");
					System.out.println("B) Move 2 checkers from 2 different lanes \n");
					if (c.hasNext()) {
						Choice = c.next().charAt(0);
					}
					while(Choice!='A'||Choice!='B') {
						System.out.println("Invalid choice. Select again.\n");
						System.out.println(p.getPlayerName()+ ": Select how you want to move:\n");
						System.out.println("A) Move 2 checkers from one lane \n");
						System.out.println("B) Move 2 checkers from 2 different lanes. \n");
						if (c.hasNext()) {
							Choice = c.next().charAt(0);
						}
					}
					switch(Choice) {
					case 'A'->{
						System.out.println(p.getPlayerName()+ ": Select a lane you want to move from:\n");
						Begin1=c.nextInt();
						while(!(Begin1>0&&Begin1<25)) {
							System.out.println("Invalid place. Select again.\n");
							Begin1=c.nextInt();
						}
						if (board.countCheckerOnLane(Begin1, p.getPlayerColor()) >= 2) {// has enough number of self-side checkers to move
							if (step1 != step2) {// 2 steps inequal: by default consider the larger step
								moveLength1 = step1;
								moveLength2 = step2;
								listHintAndMove_StartWithSingleLane(Begin1, moveLength1, moveLength2, p, opponent, 2, false);
							}
							if (step1 == step2) {
								System.out.println("You have same dice points and can move twice of bonus steps.\n");
								moveLength1 = 3 * step1;// check 3 cases: 1->sum of dice numbers
								moveLength2 = 2 * step1;// check 3 cases: 2->bigger one of dice numbers
								moveLength3 = step1;// check 3 cases: 3->smaller one of dice numbers
								System.out.println(p.getPlayerName()+ ": Select how you want to move:\n");
								System.out.println("1) Move 3 times of dice point for first checker and once of dice point of second checker \n");
								System.out.println("2) Move 2 times of dice point for both checkers \n");
								System.out.println("3) Move once of dice point for first checker and 3 times of dice point of second checker \n");
								if (c.hasNext()) {
									choiceStep = c.nextInt();
								}
								while(choiceStep!=1||choiceStep!=2||choiceStep!=3) {
									System.out.println("Invalid choice. Select again.\n");
									System.out.println(p.getPlayerName()+ ": Select how you want to move:\n");
									System.out.println("1) Move 3 times of dice point for first checker and once of dice point of second checker \n");
									System.out.println("2) Move 2 times of dice point for both checkers \n");
									System.out.println("3) Move once of dice point for first checker and 3 times of dice point of second checker \n");
									if (c.hasNext()) {
										choiceStep = c.nextInt();
									}
								}
								switch(choiceStep) {
									case 1->{
										listHintAndMove_StartWithSingleLane(Begin1, moveLength1, moveLength3, p, opponent, 2, true);
										break;
								}
									case 2->{
										listHintAndMove_StartWithSingleLane(Begin1, moveLength2, moveLength2, p, opponent, 2, true);
										break;
									}
									case 3->{
										listHintAndMove_StartWithSingleLane(Begin1, moveLength3, moveLength1, p, opponent, 2, true);
										break;
									}
								}
								
						}
					}
					}	
					case 'B'->{ // Move from both lane 1 and lane 2
						System.out.println(p.getPlayerName()+ ": Select lane 1 you want to move from:\n");
						Begin1=c.nextInt();
						System.out.println(p.getPlayerName()+ ": Select lane 2 you want to move from:\n");
						Begin2=c.nextInt();
						while(!((Begin1>0&&Begin1<25)&&(Begin2>0&&Begin2<25))) {
							System.out.println("Invalid place. Select again.\n");
							System.out.println(p.getPlayerName()+ ": Select lane 1 you want to move from:\n");
							Begin1=c.nextInt();
							System.out.println(p.getPlayerName()+ ": Select lane 2 you want to move from:\n");
							Begin2=c.nextInt();
						}
						while(!(board.countCheckerOnLane(Begin1, p.getPlayerColor()) >= 1&& board.countCheckerOnLane(Begin2, p.getPlayerColor()) >= 1)) {
							System.out.println("Invalid place. Select again.\n");
							System.out.println(p.getPlayerName()+ ": Select lane 1 you want to move from:\n");
							Begin1=c.nextInt();
							System.out.println(p.getPlayerName()+ ": Select lane 2 you want to move from:\n");
							Begin2=c.nextInt();
						}
						// CHeck: The lane to move from has at least 1 checker for moving
						if (step1 != step2) {
							moveLength1 = step1;// check 3 cases: 1->sum of dice numbers: ignored here
							moveLength2 = step2;// check 3 cases: 2->bigger one of dice numbers
							listHintAndMove_StartWithMultiLane(Begin1, Begin2, moveLength1, moveLength2, p, opponent);
						}

						if (step1 == step2) {
							System.out.println("You have same dice points and can move twice of bonus steps.\n");
							moveLength1 = 3 * step1;// check 3 cases: 1->3*dice numbers
							moveLength2 = 2 * step1;// check 3 cases: 2->2*dice number
							moveLength3 = step1;// check 3 cases: 3->1*dice number
							System.out.println(p.getPlayerName()+ ": Select how you want to move:\n");
							System.out.println("1) Move 3 times of dice point for one checker and once of dice point of another checker \n");
							System.out.println("2) Move 2 times of dice point for both checkers \n");
							if (c.hasNext()) {
								choiceStep = c.nextInt();
							}
							while(choiceStep!=1||choiceStep!=2) {
								System.out.println("Invalid choice. Select again.\n");
								System.out.println(p.getPlayerName()+ ": Select how you want to move:\n");
								System.out.println("1) Move 3 times of dice point for one checker and once of dice point of another checker \n");
								System.out.println("2) Move 2 times of dice point for both checkers \n");
								if (c.hasNext()) {
									choiceStep = c.nextInt();
								}
							}
							switch(choiceStep) {
							case 1->{
								listHintAndMove_StartWithMultiLane(Begin1, Begin2, moveLength1, moveLength3, p, opponent);
								break;
						}
							case 2->{
								listHintAndMove_StartWithMultiLane(Begin1, Begin2, moveLength2, moveLength2, p, opponent);
								break;
							}
						}
						}
				}
					}
				}
			}
			if (board.canMoveoff(p)) {
				int place1, place2;
				System.out.println("You now can move off your checkers.\n");
				System.out.println("Select 2 lanes you want to look at.\n");
				place1=c.nextInt();
				place2=c.nextInt();
				Begin1 = Math.max(place1, place2);
				Begin2 = Math.min(place1, place2);
				moveLength2 = Math.max(step1, step2);// check 3 cases: 2->bigger one of dice numbers
				moveLength3 = Math.min(step1, step2);// check 3 cases: 3->smaller one of dice numbers
				while (Begin1 > 6 || Begin2 > 6) {
					System.out.println("You can not move from outside of your home area. Please try again.\n");
					if (Begin1 > 6) {
						Begin1 = c.nextInt();
					}
					if (Begin2 > 6) {
						Begin2 = c.nextInt();
					}
				}
				listHint_MoveOff(Begin1, Begin2, moveLength2, moveLength3, p, opponent);
			}		
		}
		}
		c.close();
		}

	/*
	 * Functions that list all potential legal moving hints according to different
	 * starting lanes and moving steps.
	 * 
	 * @param Player object, Index of 2 lanes that move checkers from, moving steps
	 * from rolling dice.
	 *
	 * 
	 * 
	 *
	 * @return Moving hint choices for player to choose
	 * 
	 * 
	 * 
	 */
	public void listHintAndMove_StartWithSingleLane(int Begin, int moveLength1, int moveLength2, Player p, Player opponent, int checkerMoveNum, boolean isBonus) {
		Scanner c = new Scanner(System.in);
		char moveChoice;
		switch (p.getId()) {
		case 1 -> {
			if(checkerMoveNum==1&&isBonus==false) {
				if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("MOVE")&& board.checkPlayable(Begin, moveLength1+moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->" + (Begin - moveLength1) + "->" + (Begin - moveLength1-moveLength2) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin, Begin - moveLength1-moveLength2);
						p.setPipNum(1);
						break;
					}
					}
				}
				else if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("MOVE")&& board.checkPlayable(Begin, moveLength1+moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->" + (Begin - moveLength1) + "->" + " attack: " + (Begin - moveLength1-moveLength2)+ " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin - moveLength1-moveLength2);
						board.makeMove(Begin, Begin - moveLength1-moveLength2);
						p.setPipNum(1);
						break;
					}
					}
				}
				else if(board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->" + " attack: " + (Begin - moveLength1) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin - moveLength1);
						board.makeMove(Begin, Begin - moveLength1);
						p.setPipNum(1);
						break;
					}
					}
				}
				else {
					System.out.println(p.getPlayerName()+": You cannot move in this turn\n");
				}
			}
			if(checkerMoveNum==1&&isBonus==true) {
				if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("MOVE")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("MOVE")&&board.checkPlayable(Begin, 2*moveLength2, p, opponent).equals("MOVE")&&board.checkPlayable(Begin, 3*moveLength2, p, opponent).equals("MOVE")){
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->"  + (Begin - moveLength1) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin, Begin - moveLength1);
						p.setPipNum(1);
						break;
					}
					}
				}
				else if(board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("MOVE")&&board.checkPlayable(Begin, 2*moveLength2, p, opponent).equals("MOVE")&&board.checkPlayable(Begin, 3*moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->" + " attack: " + (Begin - moveLength1) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.Attack( Begin - moveLength1);
						board.makeMove(Begin, Begin - moveLength1);
						p.setPipNum(1);
						break;
					}
					}
				}
				else {
					System.out.println(p.getPlayerName()+": You cannot move in this turn\n");
				}
			}
			if(checkerMoveNum==2&&isBonus==false) {
				if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("MOVE")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->" + (Begin - moveLength1) + ", " + Begin + "->" + (Begin-moveLength2) + " \n");
					System.out.println("B) Checker: " + Begin + "->" + (Begin - moveLength2) + ", " + Begin + "->" + (Begin-moveLength1) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A'||moveChoice!='B') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin, Begin - moveLength1);
						board.makeMove(Begin, Begin - moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin, Begin - moveLength2);
						board.makeMove(Begin, Begin - moveLength1);
						p.setPipNum(2);
						break;
					}
					}
				}
				if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin - moveLength1) + ", " + Begin + "->"+ " attack: " + (Begin-moveLength2) + " \n");
					System.out.println("B) Checker: " + Begin + "->"+ " attack: " + (Begin - moveLength2) + ", " + Begin + "->"+ " attack: " + (Begin-moveLength1) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A'||moveChoice!='B') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin - moveLength1);
						board.makeMove(Begin, Begin - moveLength1);
						board.Attack(Begin - moveLength2);
						board.makeMove(Begin, Begin - moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.Attack(Begin - moveLength2);
						board.makeMove(Begin, Begin - moveLength2);
						board.Attack(Begin - moveLength1);
						board.makeMove(Begin, Begin - moveLength1);
						p.setPipNum(2);
						break;
					}
					}
				}
				if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin - moveLength1) + ", " + Begin + "->" + (Begin-moveLength2) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin - moveLength1);
						board.makeMove(Begin, Begin - moveLength1);
						board.makeMove(Begin, Begin - moveLength2);
						p.setPipNum(2);
						break;
					}
					}
				}
				if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("MOVE")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->" + (Begin - moveLength1) + ", " + Begin + "->" + " attack: " + (Begin-moveLength2) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin, Begin - moveLength1);
						board.Attack(Begin - moveLength2);
						board.makeMove(Begin, Begin - moveLength2);
						p.setPipNum(2);
						break;
					}
					}
				}
				if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin - moveLength1) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin - moveLength1);
						board.makeMove(Begin, Begin - moveLength1);
						p.setPipNum(1);
						break;
					}
					}
				}
				if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("NONE")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin - moveLength2) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin - moveLength2);
						board.makeMove(Begin, Begin - moveLength2);
						p.setPipNum(1);
						break;
					}
					}
				}
				else {
					System.out.println(p.getPlayerName()+": You cannot move in this turn\n");
				}
			}
				if(checkerMoveNum==2&&isBonus==true) {
					if(moveLength1!=moveLength2) {
						if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("MOVE")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("MOVE")) {
							System.out.println("Choose from these options:\n");
							System.out.println("A) Checker: " + Begin + "->" + (Begin - moveLength1) + ", " + Begin + "->" + (Begin-moveLength2) + " \n");
							System.out.println("Your choice:\n");
							moveChoice = c.nextLine().charAt(0);
							while(moveChoice!='A'||moveChoice!='B') {
								System.out.println("Sorry, please try again.\n");
								moveChoice = c.nextLine().charAt(0);
							}
							switch (moveChoice) {
							case 'A' -> {
								board.makeMove(Begin, Begin - moveLength1);
								board.makeMove(Begin, Begin - moveLength2);
								p.setPipNum(2);
								break;
							}
							}
						}
						if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("ATTACK")) {
							System.out.println("Choose from these options:\n");
							System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin - moveLength1) + ", " + Begin + "->"+ " attack: " + (Begin-moveLength2) + " \n");
							System.out.println("Your choice:\n");
							moveChoice = c.nextLine().charAt(0);
							while(moveChoice!='A'||moveChoice!='B') {
								System.out.println("Sorry, please try again.\n");
								moveChoice = c.nextLine().charAt(0);
							}
							switch (moveChoice) {
							case 'A' -> {
								board.Attack(Begin - moveLength1);
								board.makeMove(Begin, Begin - moveLength1);
								board.Attack(Begin - moveLength2);
								board.makeMove(Begin, Begin - moveLength2);
								p.setPipNum(2);
								break;
							}
							}
						}
						if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("MOVE")) {
							System.out.println("Choose from these options:\n");
							System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin - moveLength1) + ", " + Begin + "->" + (Begin-moveLength2) + " \n");
							System.out.println("Your choice:\n");
							moveChoice = c.nextLine().charAt(0);
							while(moveChoice!='A') {
								System.out.println("Sorry, please try again.\n");
								moveChoice = c.nextLine().charAt(0);
							}
							switch (moveChoice) {
							case 'A' -> {
								board.Attack(Begin - moveLength1);
								board.makeMove(Begin, Begin - moveLength1);
								board.makeMove(Begin, Begin - moveLength2);
								p.setPipNum(2);
								break;
							}
							}
						}
						if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("MOVE")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("ATTACK")) {
							System.out.println("Choose from these options:\n");
							System.out.println("A) Checker: " + Begin + "->" + (Begin - moveLength1) + ", " + Begin + "->"+ " attack: " + (Begin -moveLength2) + " \n");
							System.out.println("Your choice:\n");
							moveChoice = c.nextLine().charAt(0);
							while(moveChoice!='A') {
								System.out.println("Sorry, please try again.\n");
								moveChoice = c.nextLine().charAt(0);
							}
							switch (moveChoice) {
							case 'A' -> {
								board.makeMove(Begin, Begin - moveLength1);
								board.Attack(Begin - moveLength2);
								board.makeMove(Begin, Begin - moveLength2);
								p.setPipNum(2);
								break;
							}
							}
						}
						if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("NONE")) {
							System.out.println("Choose from these options:\n");
							System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin - moveLength1) + " \n");
							System.out.println("Your choice:\n");
							moveChoice = c.nextLine().charAt(0);
							while(moveChoice!='A') {
								System.out.println("Sorry, please try again.\n");
								moveChoice = c.nextLine().charAt(0);
							}
							switch (moveChoice) {
							case 'A' -> {
								board.Attack(Begin - moveLength1);
								board.makeMove(Begin, Begin - moveLength1);
								p.setPipNum(1);
								break;
							}
							}
						}
						if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("NONE")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("ATTACK")) {
							System.out.println("Choose from these options:\n");
							System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin - moveLength2) + " \n");
							System.out.println("Your choice:\n");
							moveChoice = c.nextLine().charAt(0);
							while(moveChoice!='A') {
								System.out.println("Sorry, please try again.\n");
								moveChoice = c.nextLine().charAt(0);
							}
							switch (moveChoice) {
							case 'A' -> {
								board.Attack(Begin - moveLength2);
								board.makeMove(Begin, Begin - moveLength2);
								p.setPipNum(1);
								break;
							}
							}
					}
						else {
							System.out.println(p.getPlayerName()+": You cannot move in this turn\n");
						}
				}
					if(moveLength1==moveLength2) {
						if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("MOVE")) {
							System.out.println("Choose from these options:\n");
							System.out.println("A) Checker: " + Begin + "->" + (Begin - moveLength1) + ", " + Begin + "->" + (Begin-moveLength2) + " \n");
							System.out.println("Your choice:\n");
							moveChoice = c.nextLine().charAt(0);
							while(moveChoice!='A'||moveChoice!='B') {
								System.out.println("Sorry, please try again.\n");
								moveChoice = c.nextLine().charAt(0);
							}
							switch (moveChoice) {
							case 'A' -> {
								board.makeMove(Begin, Begin - moveLength1);
								board.makeMove(Begin, Begin - moveLength2);
								p.setPipNum(2);
								break;
							}
							}
						}
						if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")) {
							System.out.println("Choose from these options:\n");
							System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin - moveLength1) + ", " + Begin + "->" + (Begin-moveLength2) + " \n");
							System.out.println("Your choice:\n");
							moveChoice = c.nextLine().charAt(0);
							while(moveChoice!='A'||moveChoice!='B') {
								System.out.println("Sorry, please try again.\n");
								moveChoice = c.nextLine().charAt(0);
							}
							switch (moveChoice) {
							case 'A' -> {
								board.Attack(Begin - moveLength1);
								board.makeMove(Begin, Begin - moveLength1);
								board.makeMove(Begin, Begin - moveLength2);
								p.setPipNum(2);
								break;
							}
							}
						}
						else {
							System.out.println(p.getPlayerName()+": You cannot move in this turn\n");
						}
					}
			}
		}

		case 2 -> {
			if(checkerMoveNum==1&&isBonus==false) {
				if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("MOVE")&& board.checkPlayable(Begin, moveLength1+moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->" + (Begin + moveLength1) + "->" + (Begin + moveLength1+moveLength2) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin, Begin + moveLength1+moveLength2);
						p.setPipNum(1);
						break;
					}
					}
				}
				else if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("MOVE")&& board.checkPlayable(Begin, moveLength1+moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->" + (Begin + moveLength1) + "->" + " attack: " + (Begin + moveLength1+moveLength2)+ " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin + moveLength1+moveLength2);
						board.makeMove(Begin, Begin + moveLength1+moveLength2);
						p.setPipNum(1);
						break;
					}
					}
				}
				else if(board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->" + " attack: " + (Begin + moveLength1) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin + moveLength1);
						board.makeMove(Begin, Begin + moveLength1);
						p.setPipNum(1);
						break;
					}
					}
				}
				else {
					System.out.println(p.getPlayerName()+": You cannot move in this turn\n");
				}
			}
			if(checkerMoveNum==1&&isBonus==true) {
				if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("MOVE")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("MOVE")&&board.checkPlayable(Begin, 2*moveLength2, p, opponent).equals("MOVE")&&board.checkPlayable(Begin, 3*moveLength2, p, opponent).equals("MOVE")){
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->"  + (Begin + moveLength1) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin, Begin + moveLength1);
						p.setPipNum(1);
						break;
					}
					}
				}
				else if(board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("MOVE")&&board.checkPlayable(Begin, 2*moveLength2, p, opponent).equals("MOVE")&&board.checkPlayable(Begin, 3*moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->" + " attack: " + (Begin + moveLength1) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.Attack( Begin +moveLength1);
						board.makeMove(Begin, Begin + moveLength1);
						p.setPipNum(1);
						break;
					}
					}
				}
				else {
					System.out.println(p.getPlayerName()+": You cannot move in this turn\n");
				}
			}
			if(checkerMoveNum==2&&isBonus==false) {
				if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("MOVE")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->" + (Begin + moveLength1) + ", " + Begin + "->" + (Begin+moveLength2) + " \n");
					System.out.println("B) Checker: " + Begin + "->" + (Begin + moveLength2) + ", " + Begin + "->" + (Begin+moveLength1) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A'||moveChoice!='B') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin, Begin+ moveLength1);
						board.makeMove(Begin, Begin+ moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin, Begin+ moveLength2);
						board.makeMove(Begin, Begin+ moveLength1);
						p.setPipNum(2);
						break;
					}
					}
				}
				if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin+ moveLength1) + ", " + Begin + "->"+ " attack: " + (Begin+moveLength2) + " \n");
					System.out.println("B) Checker: " + Begin + "->"+ " attack: " + (Begin + moveLength2) + ", " + Begin + "->"+ " attack: " + (Begin+moveLength1) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A'||moveChoice!='B') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin +moveLength1);
						board.makeMove(Begin, Begin + moveLength1);
						board.Attack(Begin + moveLength2);
						board.makeMove(Begin, Begin + moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.Attack(Begin + moveLength2);
						board.makeMove(Begin, Begin + moveLength2);
						board.Attack(Begin + moveLength1);
						board.makeMove(Begin, Begin + moveLength1);
						p.setPipNum(2);
						break;
					}
					}
				}
				if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin+ moveLength1) + ", " + Begin + "->" + (Begin+moveLength2) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin + moveLength1);
						board.makeMove(Begin, Begin +moveLength1);
						board.makeMove(Begin, Begin+moveLength2);
						p.setPipNum(2);
						break;
					}
					}
				}
				if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("MOVE")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->" + (Begin + moveLength1) + ", " + Begin + "->" + " attack: " + (Begin+moveLength2) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin, Begin + moveLength1);
						board.Attack(Begin + moveLength2);
						board.makeMove(Begin, Begin + moveLength2);
						p.setPipNum(2);
						break;
					}
					}
				}
				if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin + moveLength1) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin + moveLength1);
						board.makeMove(Begin, Begin + moveLength1);
						p.setPipNum(1);
						break;
					}
					}
				}
				if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("NONE")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin+ moveLength2) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Sorry, please try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin +moveLength2);
						board.makeMove(Begin, Begin+moveLength2);
						p.setPipNum(1);
						break;
					}
					}
				}
				else {
					System.out.println(p.getPlayerName()+": You cannot move in this turn\n");
				}
			}
				if(checkerMoveNum==2&&isBonus==true) {
					if(moveLength1!=moveLength2) {
						if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("MOVE")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("MOVE")) {
							System.out.println("Choose from these options:\n");
							System.out.println("A) Checker: " + Begin + "->" + (Begin + moveLength1) + ", " + Begin + "->" + (Begin+moveLength2) + " \n");
							System.out.println("Your choice:\n");
							moveChoice = c.nextLine().charAt(0);
							while(moveChoice!='A'||moveChoice!='B') {
								System.out.println("Sorry, please try again.\n");
								moveChoice = c.nextLine().charAt(0);
							}
							switch (moveChoice) {
							case 'A' -> {
								board.makeMove(Begin, Begin + moveLength1);
								board.makeMove(Begin, Begin + moveLength2);
								p.setPipNum(2);
								break;
							}
							}
						}
						if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("ATTACK")) {
							System.out.println("Choose from these options:\n");
							System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin + moveLength1) + ", " + Begin + "->"+ " attack: " + (Begin+moveLength2) + " \n");
							System.out.println("Your choice:\n");
							moveChoice = c.nextLine().charAt(0);
							while(moveChoice!='A'||moveChoice!='B') {
								System.out.println("Sorry, please try again.\n");
								moveChoice = c.nextLine().charAt(0);
							}
							switch (moveChoice) {
							case 'A' -> {
								board.Attack(Begin + moveLength1);
								board.makeMove(Begin, Begin+ moveLength1);
								board.Attack(Begin+moveLength2);
								board.makeMove(Begin, Begin+ moveLength2);
								p.setPipNum(2);
								break;
							}
							}
						}
						if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("MOVE")) {
							System.out.println("Choose from these options:\n");
							System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin+ moveLength1) + ", " + Begin + "->" + (Begin+moveLength2) + " \n");
							System.out.println("Your choice:\n");
							moveChoice = c.nextLine().charAt(0);
							while(moveChoice!='A') {
								System.out.println("Sorry, please try again.\n");
								moveChoice = c.nextLine().charAt(0);
							}
							switch (moveChoice) {
							case 'A' -> {
								board.Attack(Begin+ moveLength1);
								board.makeMove(Begin, Begin+ moveLength1);
								board.makeMove(Begin, Begin+ moveLength2);
								p.setPipNum(2);
								break;
							}
							}
						}
						if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("MOVE")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("ATTACK")) {
							System.out.println("Choose from these options:\n");
							System.out.println("A) Checker: " + Begin + "->" + (Begin + moveLength1) + ", " + Begin + "->"+ " attack: " + (Begin+moveLength2) + " \n");
							System.out.println("Your choice:\n");
							moveChoice = c.nextLine().charAt(0);
							while(moveChoice!='A') {
								System.out.println("Sorry, please try again.\n");
								moveChoice = c.nextLine().charAt(0);
							}
							switch (moveChoice) {
							case 'A' -> {
								board.makeMove(Begin, Begin + moveLength1);
								board.Attack(Begin + moveLength2);
								board.makeMove(Begin, Begin + moveLength2);
								p.setPipNum(2);
								break;
							}
							}
						}
						if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("NONE")) {
							System.out.println("Choose from these options:\n");
							System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin + moveLength1) + " \n");
							System.out.println("Your choice:\n");
							moveChoice = c.nextLine().charAt(0);
							while(moveChoice!='A') {
								System.out.println("Sorry, please try again.\n");
								moveChoice = c.nextLine().charAt(0);
							}
							switch (moveChoice) {
							case 'A' -> {
								board.Attack(Begin + moveLength1);
								board.makeMove(Begin, Begin + moveLength1);
								p.setPipNum(1);
								break;
							}
							}
						}
						if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("NONE")&& board.checkPlayable(Begin, moveLength2, p, opponent).equals("ATTACK")) {
							System.out.println("Choose from these options:\n");
							System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin + moveLength2) + " \n");
							System.out.println("Your choice:\n");
							moveChoice = c.nextLine().charAt(0);
							while(moveChoice!='A') {
								System.out.println("Sorry, please try again.\n");
								moveChoice = c.nextLine().charAt(0);
							}
							switch (moveChoice) {
							case 'A' -> {
								board.Attack(Begin + moveLength2);
								board.makeMove(Begin, Begin + moveLength2);
								p.setPipNum(1);
								break;
							}
							}
					}
						else {
							System.out.println(p.getPlayerName()+": You cannot move in this turn\n");
						}
				}
					if(moveLength1==moveLength2) {
						if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("MOVE")) {
							System.out.println("Choose from these options:\n");
							System.out.println("A) Checker: " + Begin + "->" + (Begin + moveLength1) + ", " + Begin + "->" + (Begin+moveLength2) + " \n");
							System.out.println("Your choice:\n");
							moveChoice = c.nextLine().charAt(0);
							while(moveChoice!='A'||moveChoice!='B') {
								System.out.println("Sorry, please try again.\n");
								moveChoice = c.nextLine().charAt(0);
							}
							switch (moveChoice) {
							case 'A' -> {
								board.makeMove(Begin, Begin + moveLength1);
								board.makeMove(Begin, Begin + moveLength2);
								p.setPipNum(2);
								break;
							}
							}
						}
						if (board.checkPlayable(Begin, moveLength1, p, opponent).equals("ATTACK")) {
							System.out.println("Choose from these options:\n");
							System.out.println("A) Checker: " + Begin + "->"+ " attack: " + (Begin + moveLength1) + ", " + Begin + "->" + (Begin+moveLength2) + " \n");
							System.out.println("Your choice:\n");
							moveChoice = c.nextLine().charAt(0);
							while(moveChoice!='A'||moveChoice!='B') {
								System.out.println("Sorry, please try again.\n");
								moveChoice = c.nextLine().charAt(0);
							}
							switch (moveChoice) {
							case 'A' -> {
								board.Attack(Begin + moveLength1);
								board.makeMove(Begin, Begin + moveLength1);
								board.makeMove(Begin, Begin + moveLength2);
								p.setPipNum(2);
								break;
							}
							}
						}
						else {
							System.out.println(p.getPlayerName()+": You cannot move in this turn\n");
						}
					}
			}
		}
		}
		c.close();

	}

	public void listHintAndMove_StartWithMultiLane(int Begin1, int Begin2, int moveLength1, int moveLength2, Player p, Player opponent) {
		Scanner c = new Scanner(System.in);
		char moveChoice;
		switch (p.getId()) {
		case 1 -> {
				if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A'||moveChoice!='B') {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1)
							+ " attack opponent checker at " + (Begin1 - moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2)
							+ " attack opponent checker at " + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A'||moveChoice!='B') {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength1);
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.Attack(Begin2 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.Attack(Begin1 - moveLength2);
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.Attack(Begin2 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					}
				}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("MOVE")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("MOVE")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.Attack(Begin2 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}

				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					System.out.println(
							"B) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + " attack opponent checker at "
									+ (Begin1 - moveLength2) + ", " + Begin2 + "->" + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.Attack(Begin2 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.Attack(Begin1 - moveLength2);
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.Attack(Begin2 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin2 + "->" + (Begin2 - moveLength1) + ", " + Begin1 + "->"
							+ (Begin1 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin2, Begin2 - moveLength1);
						board.makeMove(Begin1, Begin1 - moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin2 + "->" + (Begin2 - moveLength1) + ", " + Begin1 + "->"
							+ (Begin1 - moveLength2) + " attack opponent checker at " + (Begin1 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin2, Begin2 - moveLength1);
						board.Attack(Begin1 - moveLength2);
						board.makeMove(Begin1, Begin1 - moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin1 + "->" + (Begin1 - moveLength1) + " \n");
					System.out.println("B) Checker: " + Begin2 + "->" + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength1);
						p.setPipNum(1);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}

			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("ATTACK")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("MOVE")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1)
							+ " attack opponent checker at " + (Begin1 - moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					System.out.println(
							"B) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + " attack opponent checker at "
									+ (Begin1 - moveLength2) + ", " + Begin2 + "->" + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength1);
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.Attack(Begin2 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.Attack(Begin1 - moveLength2);
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + " attack opponent checker at "
									+ (Begin1 - moveLength1) + ", " + Begin2 + "->" + (Begin2 - moveLength2) + " \n");
					System.out.println(
							"B) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + " attack opponent checker at "
									+ (Begin1 - moveLength2) + ", " + Begin2 + "->" + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength1);
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.Attack(Begin1 - moveLength2);
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1)
							+ " attack opponent checker at " + (Begin1 - moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength1);
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.Attack(Begin2 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + " attack opponent checker at "
									+ (Begin1 - moveLength1) + ", " + Begin2 + "->" + (Begin2 - moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength1);
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1)
							+ " attack opponent checker at " + (Begin1 - moveLength1) + " \n");
					System.out.println("B) Checkers: " + Begin2 + "->" + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength1);
						board.makeMove(Begin1, Begin1 - moveLength1);
						p.setPipNum(1);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("MOVE")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("ATTACK")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2)
							+ " attack opponent checker at " + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.Attack(Begin2 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.Attack(Begin1 - moveLength2);
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.Attack(Begin2 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2)
							+ " attack opponent checker at " + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.Attack(Begin1 - moveLength2);
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.Attack(Begin2 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.Attack(Begin2 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.Attack(Begin2 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.Attack(Begin2 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + " \n");
					System.out.println("B) Checkers: " + Begin2 + "->" + (Begin2 - moveLength1)
							+ " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength1);
						p.setPipNum(1);
						break;
					}
					case 'B' -> {
						board.Attack(Begin2 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("MOVE")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("NONE")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.Attack(Begin2 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.Attack(Begin2 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength1);
						p.setPipNum(1);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("NONE")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("MOVE")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + " attack opponent checker at "
									+ (Begin1 - moveLength2) + ", " + Begin2 + "->" + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength2);
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + " attack opponent checker at "
									+ (Begin1 - moveLength2) + ", " + Begin2 + "->" + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength2);
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin2 + "->" + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin2, Begin2 - moveLength1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("ATTACK")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("NONE")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1)
							+ " attack opponent checker at " + (Begin1 - moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin2 - moveLength1);
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.Attack(Begin2 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + " attack opponent checker at "
									+ (Begin1 - moveLength1) + ", " + Begin2 + "->" + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength1);
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1)
							+ " attack opponent checker at " + (Begin1 - moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength1);
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.Attack(Begin2 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + " attack opponent checker at "
									+ (Begin1 - moveLength1) + ", " + Begin2 + "->" + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength1);
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1)
							+ " attack opponent checker at " + (Begin1 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength1);
						board.makeMove(Begin1, Begin1 - moveLength1);
						p.setPipNum(1);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("NONE")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("ATTACK")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2)
							+ " attack opponent checker at " + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength2);
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.Attack(Begin2 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2)
							+ " attack opponent checker at " + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength2);
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.Attack(Begin2 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.Attack(Begin2 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.Attack(Begin2 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin2 + "->" + (Begin2 - moveLength1)
							+ " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin2 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("ATTACK")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("ATTACK")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + " attack opponent checker at "
									+ (Begin1 - moveLength1) + ", " + Begin2 + "->" + (Begin2 - moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength1);
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.Attack(Begin2 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}

				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.Attack(Begin2 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + " attack opponent checker at "
									+ (Begin1 - moveLength1) + ", " + Begin2 + "->" + (Begin2 - moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2)
							+ " attack opponent checker at " + (Begin1 - moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 - moveLength1) + " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength1);
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.Attack(Begin1 - moveLength2);
						board.makeMove(Begin1, Begin1 - moveLength2);
						board.Attack(Begin2 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin2 + "->" + (Begin2 - moveLength1)
							+ " attack opponent checker at " + (Begin2 - moveLength1) + ", " + Begin1 + "->"
							+ (Begin1 - moveLength2) + " attack opponent checker at " + (Begin1 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin2 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength1);
						board.Attack(Begin1 - moveLength2);
						board.makeMove(Begin1, Begin1 - moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength1) + " attack opponent checker at "
									+ (Begin1 - moveLength1) + ", " + Begin2 + "->" + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength1);
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin1 + "->" + (Begin1 - moveLength1)
							+ " attack opponent checker at " + (Begin1 - moveLength1) + " \n");
					System.out.println("B) Checker: " + Begin2 + "->" + (Begin2 - moveLength2)
							+ " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength1);
						board.makeMove(Begin1, Begin1 - moveLength1);
						p.setPipNum(1);
						break;
					}
					case 'B' -> {
						board.Attack(Begin2 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}

			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("NONE")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("NONE")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin2 + "->" + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength2);
						p.setPipNum(1);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}

				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin2 + "->" + (Begin2 - moveLength2)
							+ " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength2);
						p.setPipNum(1);
						break;
					}
					case 'B' -> {
						board.Attack(Begin2 - moveLength2);
						board.makeMove(Begin2, Begin1 - moveLength2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2)
							+ " attack opponent checker at " + (Begin1 - moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin2 + "->" + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength2);
						board.makeMove(Begin1, Begin1 - moveLength2);
						p.setPipNum(1);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin2, Begin1 - moveLength2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 - moveLength2)
							+ " attack opponent checker at " + (Begin1 - moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin2 + "->" + (Begin2 - moveLength2)
							+ " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 - moveLength2);
						board.makeMove(Begin1, Begin1 - moveLength2);
						p.setPipNum(1);
						break;
					}
					case 'B' -> {
						board.Attack(Begin2 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin2 + "->" + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin2 + "->" + (Begin2 - moveLength2)
							+ " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin2 - moveLength2);
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}
		}
		case 2 -> {
			if (Begin1 == 25) {
				// Move back from bar: mathematically equivalent to start move from lane 0
				Begin1 -= 25;
			}
			if (Begin2 == 25) {
				// Move back from bar: mathematically equivalent to start move from lane 0
				Begin2 -= 25;
			}
			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("MOVE")
					&& board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("MOVE")
					&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
				System.out.println("Choose from these options:\n");
				System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + ", " + Begin2 + "->"
						+ (Begin2 + moveLength2) + " \n");
				System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + ", " + Begin2 + "->"
						+ (Begin2 + moveLength1) + " \n");
				moveChoice = c.nextLine().charAt(0);
				switch (moveChoice) {
				case 'A' -> {
					board.makeMove(Begin1, Begin1 + moveLength1);
					board.makeMove(Begin2, Begin2 + moveLength2);
					p.setPipNum(2);
					break;
				}
				case 'B' -> {
					board.makeMove(Begin1, Begin1 + moveLength2);
					board.makeMove(Begin2, Begin2 + moveLength1);
					p.setPipNum(2);
					break;
				}
				default -> {
					System.out.println("Wrong input. Try again.\n");
					moveChoice = c.nextLine().charAt(0);
				}
				}
			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("ATTACK")
					&& board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("ATTACK")
					&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
				System.out.println("Choose from these options:\n");
				System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1)
						+ " attack opponent checker at " + (Begin1 + moveLength1) + ", " + Begin2 + "->"
						+ (Begin2 + moveLength2) + " attack opponent checker at " + (Begin2 + moveLength2) + " \n");
				System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2)
						+ " attack opponent checker at " + (Begin1 + moveLength2) + ", " + Begin2 + "->"
						+ (Begin2 + moveLength1) + " attack opponent checker at " + (Begin2 + moveLength1) + " \n");
				moveChoice = c.nextLine().charAt(0);
				switch (moveChoice) {
				case 'A' -> {
					board.Attack(Begin1 + moveLength1);
					board.makeMove(Begin1, Begin1 + moveLength1);
					board.Attack(Begin2 + moveLength2);
					board.makeMove(Begin2, Begin2 + moveLength2);
					p.setPipNum(2);
					break;
				}
				case 'B' -> {
					board.Attack(Begin1 + moveLength2);
					board.makeMove(Begin1, Begin1 + moveLength2);
					board.Attack(Begin2 + moveLength1);
					board.makeMove(Begin2, Begin2 + moveLength1);
					p.setPipNum(2);
					break;
				}
				default -> {
					System.out.println("Wrong input. Try again.\n");
					moveChoice = c.nextLine().charAt(0);
				}
				}
			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("MOVE")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("MOVE")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength2) + " attack opponent checker at " + (Begin2 + moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.Attack(Begin2 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}

				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength2) + " attack opponent checker at " + (Begin2 + moveLength2) + " \n");
					System.out.println(
							"B) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + " attack opponent checker at "
									+ (Begin1 + moveLength2) + ", " + Begin2 + "->" + (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.Attack(Begin2 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength2);
						break;
					}
					case 'B' -> {
						board.Attack(Begin1 + moveLength2);
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength2) + " attack opponent checker at " + (Begin2 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.Attack(Begin2 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin2 + "->" + (Begin2 + moveLength1) + ", " + Begin1 + "->"
							+ (Begin1 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin2, Begin2 + moveLength1);
						board.makeMove(Begin1, Begin1 + moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin2 + "->" + (Begin2 + moveLength1) + ", " + Begin1 + "->"
							+ (Begin1 + moveLength2) + " attack opponent checker at " + (Begin1 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin2, Begin2 + moveLength1);
						board.Attack(Begin1 + moveLength2);
						board.makeMove(Begin1, Begin1 + moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin1 + "->" + (Begin1 + moveLength1) + " \n");
					System.out.println("B) Checker: " + Begin2 + "->" + (Begin2 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength1);
						p.setPipNum(1);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("ATTACK")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("MOVE")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1)
							+ " attack opponent checker at " + (Begin1 + moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength2) + " attack opponent checker at " + (Begin2 + moveLength2) + " \n");
					System.out.println(
							"B) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + " attack opponent checker at "
									+ (Begin1 + moveLength2) + ", " + Begin2 + "->" + (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength1);
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.Attack(Begin2 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.Attack(Begin1 + moveLength2);
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + " attack opponent checker at "
									+ (Begin1 + moveLength1) + ", " + Begin2 + "->" + (Begin2 + moveLength2) + " \n");
					System.out.println(
							"B) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + " attack opponent checker at "
									+ (Begin1 + moveLength2) + ", " + Begin2 + "->" + (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength1);
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.Attack(Begin1 + moveLength2);
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1)
							+ " attack opponent checker at " + (Begin1 + moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength2) + " attack opponent checker at " + (Begin2 + moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength1);
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.Attack(Begin2 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + " attack opponent checker at "
									+ (Begin1 + moveLength1) + ", " + Begin2 + "->" + (Begin2 + moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength1);
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1)
							+ " attack opponent checker at " + (Begin1 + moveLength1) + " \n");
					System.out.println("B) Checkers: " + Begin2 + "->" + (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength1);
						board.makeMove(Begin1, Begin1 + moveLength1);
						p.setPipNum(1);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("MOVE")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("ATTACK")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength2) + " attack opponent checker at " + (Begin2 + moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2)
							+ " attack opponent checker at " + (Begin1 + moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength1) + " attack opponent checker at " + (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.Attack(Begin2 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.Attack(Begin1 + moveLength2);
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.Attack(Begin2 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2)
							+ " attack opponent checker at " + (Begin1 + moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength1) + " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.Attack(Begin1 + moveLength2);
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.Attack(Begin2 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength2) + " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength1) + " attack opponent checker at " + (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.Attack(Begin2 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.Attack(Begin2 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength1) + " attack opponent checker at " + (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.Attack(Begin2 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + " \n");
					System.out.println("B) Checkers: " + Begin2 + "->" + (Begin2 + moveLength1)
							+ " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength1);
						p.setPipNum(1);
						break;
					}
					case 'B' -> {
						board.Attack(Begin2 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("MOVE")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("NONE")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength2) + " attack opponent checker at " + (Begin2 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.Attack(Begin2 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength2) + " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.Attack(Begin2 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength1);
						p.setPipNum(1);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("NONE")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("MOVE")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + " attack opponent checker at "
									+ (Begin1 + moveLength2) + ", " + Begin2 + "->" + (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength2);
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + " attack opponent checker at "
									+ (Begin1 + moveLength2) + ", " + Begin2 + "->" + (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength2);
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin2 + "->" + (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("ATTACK")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("NONE")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1)
							+ " attack opponent checker at " + (Begin1 + moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength2) + " attack opponent checker at " + (Begin2 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin2 + moveLength1);
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.Attack(Begin2 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + " attack opponent checker at "
									+ (Begin1 + moveLength1) + ", " + Begin2 + "->" + (Begin2 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength1);
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1)
							+ " attack opponent checker at " + (Begin1 + moveLength1) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength2) + " attack opponent checker at " + (Begin2 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength1);
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.Attack(Begin2 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + " attack opponent checker at "
									+ (Begin1 + moveLength1) + ", " + Begin2 + "->" + (Begin2 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength1);
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1)
							+ " attack opponent checker at " + (Begin1 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength1);
						board.makeMove(Begin1, Begin1 + moveLength1);
						p.setPipNum(1);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("NONE")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("ATTACK")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2)
							+ " attack opponent checker at " + (Begin1 + moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength1) + " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength2);
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.Attack(Begin2 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2)
							+ " attack opponent checker at " + (Begin1 + moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength1) + " attack opponent checker at " + (Begin2 - moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength2);
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.Attack(Begin2 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength1) + " attack opponent checker at " + (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.Attack(Begin2 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength1) + " attack opponent checker at " + (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.Attack(Begin2 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin2 + "->" + (Begin2 + moveLength1)
							+ " attack opponent checker at " + (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin2 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("ATTACK")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("ATTACK")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + " attack opponent checker at "
									+ (Begin1 + moveLength1) + ", " + Begin2 + "->" + (Begin2 + moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength1) + " attack opponent checker at " + (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength1);
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.Attack(Begin2 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}

				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength1) + " attack opponent checker at " + (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.Attack(Begin2 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}

					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + " attack opponent checker at "
									+ (Begin1 + moveLength1) + ", " + Begin2 + "->" + (Begin2 + moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2)
							+ " attack opponent checker at " + (Begin1 + moveLength2) + ", " + Begin2 + "->"
							+ (Begin2 + moveLength1) + " attack opponent checker at " + (Begin2 + moveLength1) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength1);
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					case 'B' -> {
						board.Attack(Begin1 + moveLength2);
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.Attack(Begin2 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin2 + "->" + (Begin2 + moveLength1)
							+ " attack opponent checker at " + (Begin2 + moveLength1) + ", " + Begin1 + "->"
							+ (Begin1 + moveLength2) + " attack opponent checker at " + (Begin1 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin2 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength1);
						board.Attack(Begin1 + moveLength2);
						board.makeMove(Begin1, Begin1 + moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println(
							"A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength1) + " attack opponent checker at "
									+ (Begin1 + moveLength1) + ", " + Begin2 + "->" + (Begin2 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength1);
						board.makeMove(Begin1, Begin1 + moveLength1);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin1 + "->" + (Begin1 + moveLength1)
							+ " attack opponent checker at " + (Begin1 + moveLength1) + " \n");
					System.out.println("B) Checker: " + Begin2 + "->" + (Begin2 + moveLength2)
							+ " attack opponent checker at " + (Begin2 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength1);
						board.makeMove(Begin1, Begin1 + moveLength1);
						p.setPipNum(1);
						break;
					}
					case 'B' -> {
						board.Attack(Begin2 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}

			}

			if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("NONE")
					&& board.checkPlayable(Begin2, moveLength1, p, opponent).equals("NONE")) {
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin2 + "->" + (Begin2 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength2);
						p.setPipNum(1);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}

				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin2 + "->" + (Begin2 + moveLength2)
							+ " attack opponent checker at " + (Begin2 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength2);
						p.setPipNum(1);
						break;
					}
					case 'B' -> {
						board.Attack(Begin2 + moveLength2);
						board.makeMove(Begin2, Begin1 + moveLength2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2)
							+ " attack opponent checker at " + (Begin1 + moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin2 + "->" + (Begin2 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength2);
						board.makeMove(Begin1, Begin1 + moveLength2);
						p.setPipNum(1);
						break;
					}
					case 'B' -> {
						board.makeMove(Begin2, Begin1 + moveLength2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("ATTACK")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin1 + "->" + (Begin1 + moveLength2)
							+ " attack opponent checker at " + (Begin1 + moveLength2) + " \n");
					System.out.println("B) Checkers: " + Begin2 + "->" + (Begin2 + moveLength2)
							+ " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin1 + moveLength2);
						board.makeMove(Begin1, Begin1 + moveLength2);
						p.setPipNum(1);
						break;
					}
					case 'B' -> {
						board.Attack(Begin2 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin2 + "->" + (Begin2 + moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (board.checkPlayable(Begin1, moveLength2, p, opponent).equals("NONE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("ATTACK")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checkers: " + Begin2 + "->" + (Begin2 + moveLength2)
							+ " attack opponent checker at " + (Begin2 - moveLength2) + " \n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.Attack(Begin2 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again.\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}

		}
		}
		c.close();
	}

	public void listHint_MoveOff(int Begin1, int Begin2, int moveLength1, int moveLength2, Player p, Player opponent) {

		Scanner c = new Scanner(System.in);
		char moveChoice;

		switch (p.getId()) {
		case 1 -> {// Player A: 24-1
			if (moveLength1 >= Begin1 && moveLength2 >= Begin2) {
				System.out.println("Choose from these options:\n");
				System.out.println("A) Checkers: " + Begin1 + "->" + "OFF" + "  " + Begin2 + "->" + "OFF" + " \n");
				System.out.println("Your choice:\n");
				moveChoice = c.nextLine().charAt(0);
				switch (moveChoice) {
				case 'A' -> {
					board.moveOff(Begin1);
					board.moveOff(Begin2);
					p.setPipNum(2);
					break;
				}
				default -> {
					System.out.println("Wrong input. Try again?\n");
					moveChoice = c.nextLine().charAt(0);
				}
				}
			}

			if (moveLength1 < Begin1 && moveLength2 >= Begin2) {
				if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin1 + "->" + (Begin1 - moveLength1) + "  " + Begin2 + "->"
							+ "OFF" + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.moveOff(Begin2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again?\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				} else {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin2 + "->" + "OFF" + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.moveOff(Begin2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again?\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}
			if (moveLength1 < Begin1 && moveLength2 < Begin2) {
				if (board.checkPlayable(Begin1, moveLength1, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin1 + "->" + (Begin1 - moveLength1) + "  " + Begin2 + "->"
							+ (Begin2 - moveLength2) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 - moveLength1);
						board.makeMove(Begin2, Begin1 - moveLength2);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again?\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (!board.checkPlayable(Begin1, moveLength1, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin2, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) " + Begin2 + "->" + (Begin2 - moveLength2) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin2, Begin2 - moveLength2);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again?\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}
		}
		case 2 -> {// Player A: 1-24
			if (moveLength1 + Begin2 > 24 && moveLength2 + Begin1 > 24) {
				System.out.println("Choose from these options:\n");
				System.out.println("A) Checkers: " + Begin1 + "->" + "OFF" + "  " + Begin2 + "->" + "OFF" + " \n");
				System.out.println("Your choice:\n");
				moveChoice = c.nextLine().charAt(0);
				switch (moveChoice) {
				case 'A' -> {
					board.moveOff(Begin1);
					p.setPipNum(1);
					break;
				}
				default -> {
					System.out.println("Wrong input. Try again?\n");
					moveChoice = c.nextLine().charAt(0);
				}
				}
			}

			if (moveLength1 + Begin2 <= 24 && moveLength2 + Begin1 > 24) {
				if (board.checkPlayable(Begin2, moveLength1, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin2 + "->" + (Begin2 + moveLength1) + "  " + Begin1 + "->"
							+ "OFF" + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin2, Begin2 + moveLength1);
						board.moveOff(Begin1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again?\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				} else {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin1 + "->" + "OFF" + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.moveOff(Begin1);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again?\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}
			if (moveLength1 + Begin2 <= 24 && moveLength2 + Begin1 <= 24) {
				if (board.checkPlayable(Begin2, moveLength1, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) Checker: " + Begin2 + "->" + (Begin2 + moveLength1) + "  " + Begin1 + "->"
							+ (Begin1 + moveLength2) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin1, Begin1 + moveLength2);
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(2);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again?\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
				if (!board.checkPlayable(Begin2, moveLength1, p, opponent).equals("MOVE")
						&& board.checkPlayable(Begin1, moveLength2, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) " + Begin2 + "->" + (Begin2 + moveLength1) + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(Begin2, Begin2 + moveLength1);
						p.setPipNum(1);
						break;
					}
					default -> {
						System.out.println("Wrong input. Try again?\n");
						moveChoice = c.nextLine().charAt(0);
					}
					}
				}
			}
		}

		}
		c.close();
	}
	public void showHint_moveBack(Player p, int step1, int step2, Player opponent) {
		Scanner c = new Scanner(System.in);
		char moveChoice;
		while(board.checkAttackedChecker(p.getPlayerColor())>0) {
			switch(p.getId()) {
			case 1->{
				if(board.checkPlayable(24, step1-1, p, opponent).equals("MOVE")&&board.checkPlayable(24, step2-1, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) " + "Move back 2 checkers from bar" + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Please choose again:\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(25, 25 - step1);
						board.makeMove(25, 25 - step2);
						p.setPipNum(2);
						break;
					}
					}
				}
				if(board.checkPlayable(24, step1-1, p, opponent).equals("MOVE")&&board.checkPlayable(24, step2-1, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) " + "Move back 1 checkers from bar" + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Please choose again:\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(25, 25 - step1);
						p.setPipNum(1);
						break;
					}
					}
				}
				if(board.checkPlayable(24, step1-1, p, opponent).equals("NONE")&&board.checkPlayable(24, step2-1, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) " + "Move back 1 checkers from bar" + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Please choose again:\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(25, 25 - step2);
						p.setPipNum(1);
						break;
					}
					}
				}
				else {
					System.out.println("You cannot move now.\n");
				}
			}
			case 2->{
				if(board.checkPlayable(1, step1-1, p, opponent).equals("MOVE")&&board.checkPlayable(1, step2-1, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) " + "Move back 2 checkers from bar" + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Please choose again:\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(1, 1 + step1-1);
						board.makeMove(1, 1 + step2-1);
						p.setPipNum(2);
						break;
					}
					}
				}
				if(board.checkPlayable(1, step1-1, p, opponent).equals("MOVE")&&board.checkPlayable(1, step2-1, p, opponent).equals("NONE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) " + "Move back 1 checkers from bar" + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Please choose again:\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(1, 1 + step1-1);
						p.setPipNum(1);
						break;
					}
					}
				}
				if(board.checkPlayable(1, step1-1, p, opponent).equals("NONE")&&board.checkPlayable(1, step2-1, p, opponent).equals("MOVE")) {
					System.out.println("Choose from these options:\n");
					System.out.println("A) " + "Move back 1 checkers from bar" + " \n");
					System.out.println("Your choice:\n");
					moveChoice = c.nextLine().charAt(0);
					while(moveChoice!='A') {
						System.out.println("Please choose again:\n");
						moveChoice = c.nextLine().charAt(0);
					}
					switch (moveChoice) {
					case 'A' -> {
						board.makeMove(1, 1 + step2-1);
						p.setPipNum(1);
						break;
					}
					}
				}
				else {
					System.out.println("You cannot move now.\n");
				}
			}
			}
		}
		c.close();
	}
}
