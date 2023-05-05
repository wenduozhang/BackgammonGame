package backgammon;


import java.util.ArrayList;
import java.util.List;

public class GameBoard {

	private List<Lane> CheckerLanes;
	private Lane Bar;// Places for placing attacked checkers
	private Lane MoveOff;// Place for placing moved out checkers

	public GameBoard() {
		CheckerLanes = new ArrayList<Lane>();
		Bar = new Lane(25);
		MoveOff = new Lane(0);
		int idx = 0;
		CheckerLanes.add(idx, MoveOff);
		idx = 1;

		while (idx <= 24) {
			switch (idx) {
			case 1, 3, 5, 7, 9, 11 -> {// lower side of board: by default for player A, 1-6: home, 7-12:outer
				Lane playerLane = new Lane(idx);// by default: odd lane->black, even lane->white
				CheckerLanes.add(idx, playerLane);
			}
			case 2, 4, 6, 8, 10, 12 -> {
				Lane playerLane = new Lane(idx);// by default: odd lane->black, even lane->white
				CheckerLanes.add(idx, playerLane);
			}
			case 13, 15, 17, 19, 21, 23 -> {// upper side of board: by default for player B, 19-24: home, 13-18:outer
				Lane playerLane = new Lane(idx);// by default: odd lane->black, even lane->white
				CheckerLanes.add(idx, playerLane);
			}
			case 14, 16, 18, 20, 22, 24 -> {
				Lane playerLane = new Lane(idx);// by default: odd lane->black, even lane->white
				CheckerLanes.add(idx, playerLane);
			}
			}
			idx++;

		}
		CheckerLanes.add(25, Bar);
	}

	public void initChecker(Color refColorA, Color refColorB) {// i is lane index for player A, then lane index for player
																// B is 24-(i-1)
		for (int i = 1; i <= 24; i++) {
			if (i == 6) {
				Lane laneA = CheckerLanes.get(i);
				Lane laneB = CheckerLanes.get(24 + 1 - i);
				laneA.putChecker(refColorA, 5);
				laneB.putChecker(refColorB, 5);
				CheckerLanes.set(i, laneA);
				CheckerLanes.set(24 + 1 - i, laneB);
			}
			if (i == 8) {
				Lane laneA = CheckerLanes.get(i);
				Lane laneB = CheckerLanes.get(24 + 1 - i);
				laneA.putChecker(refColorA, 3);
				laneB.putChecker(refColorB, 3);
				CheckerLanes.set(i, laneA);
				CheckerLanes.set(24 + 1 - i, laneB);
			}
			if (i == 13) {
				Lane laneA = CheckerLanes.get(i);
				Lane laneB = CheckerLanes.get(24 + 1 - i);
				laneA.putChecker(refColorA, 5);
				laneB.putChecker(refColorB, 5);
				CheckerLanes.set(i, laneA);
				CheckerLanes.set(24 + 1 - i, laneB);
			}
			if (i == 24) {
				Lane laneA = CheckerLanes.get(i);
				Lane laneB = CheckerLanes.get(24 + 1 - i);
				laneA.putChecker(refColorA, 2);
				laneB.putChecker(refColorB, 2);
				CheckerLanes.set(i, laneA);
				CheckerLanes.set(24 + 1 - i, laneB);
			}
		}
	}

	public int countCheckerOnLane(int idx, String targetColor) {
		// Lane lane=CheckerLanes.get(idx);
		int checkerCount=0;
		int[] CheckerCount= CheckerLanes.get(idx).countChecker();
		if(targetColor.equals("BLACK")) {
			checkerCount=CheckerCount[0];
		}
		if(targetColor.equals("WHITE")) {
			checkerCount=CheckerCount[1];
		}
		return checkerCount;
	}

	/*public String LaneCheckerColor(int idx) {
		// Lane lane = CheckerLanes.get(idx);
		return CheckerLanes.get(idx).checkerColor();
	}*/

	public String checkPlayable(int Starter, int Step, Player player, Player opponent) {
		String playType = MoveType.NONE.toString();
		int moveTar;
		if (player.getId()==1&&opponent.getId()==2) {
			// player A goes lane index 24-1
			moveTar = Starter - Step;
			if (moveTar > 0&&moveTar<25) {
				for (int i = Starter; i >= moveTar; i--) {
					if (i > moveTar) {
						continue;
					}
					if (i == moveTar) {
						//Lane lane=CheckerLanes.get(i);
						if ((this.countCheckerOnLane(i,player.getPlayerColor()) == 0&&this.countCheckerOnLane(i, opponent.getPlayerColor()) ==0)||(this.countCheckerOnLane(i,player.getPlayerColor()) > 0)) {//lane is empty or has at least 1 player's checker(s)
							playType = MoveType.MOVE.toString();
						} else if (this.countCheckerOnLane(i, opponent.getPlayerColor()) == 1) {
							playType = MoveType.ATTACK.toString();
						} else {
							playType = MoveType.NONE.toString();
						}
					}

				}
			}

		if(player.getId()==2&&opponent.getId()==1) {
			// player B goes lane index 1-24
			moveTar = Starter + Step;
			if (moveTar>0&&moveTar <= 24) {
				for (int i = Starter; i <= moveTar; i++) {
					if (i < moveTar) {
						continue;
					}
					if (i == moveTar) {
						if ((this.countCheckerOnLane(i,player.getPlayerColor()) == 0&&this.countCheckerOnLane(i, opponent.getPlayerColor()) ==0)||(this.countCheckerOnLane(i,player.getPlayerColor()) > 0)){
							playType = MoveType.MOVE.toString();
						} else if (this.countCheckerOnLane(i, opponent.getPlayerColor()) == 1) {
							playType = MoveType.ATTACK.toString();
						} else {
							playType = MoveType.NONE.toString();
						}
					}

				}
			}
		}
		}
		return playType;
	}

	public void makeMove(int laneFrom, int laneTo) {
		Lane laneOut = CheckerLanes.get(laneFrom);
		Lane laneIn = CheckerLanes.get(laneTo);
		Checker c = laneOut.getChecker(laneOut.countCheckerPerLane() - 1);
		laneOut.removeChecker(c);
		laneIn.putChecker(c);
		CheckerLanes.set(laneFrom, laneOut);
		CheckerLanes.set(laneTo, laneIn);

	}

	public void Attack(int Target) {
		makeMove(Target, 25);
	}

	public void moveOff(int Target) {
		makeMove(Target, 0);
	}
	
	public void moveBack(int Target) {
		makeMove(25, Target);
	}

	/**
	 * public boolean canAttack( int target, String color_ref){ boolean
	 * canATK=false;
	 * if(CheckerLanes.get(target).countChecker()==1&&(!CheckerLanes.get(target).checkerColor().equals(color_ref))){
	 * canATK=true; } return canATK; }
	 **/

	public boolean canMoveoff(Player p) {
		boolean canOFF = false;
		switch (p.getId()) {
		case 1 -> {
			int cnt = checkerNumbers(7, 24, p.getPlayerColor());
			if (cnt == 0 && checkAttackedChecker(p.getPlayerColor()) == 0) {
				canOFF = true;
			}
		}
		case 2 -> {
			int cnt = checkerNumbers(1, 18, p.getPlayerColor());
			if (cnt == 0 && checkAttackedChecker(p.getPlayerColor()) == 0) {
				canOFF = true;
			}
		}
		}
		return canOFF;
	}
	
	public boolean needMoveBack(Player p) {
		boolean needBack=false;
		if(this.checkAttackedChecker(p.getPlayerColor())!=0) {
			needBack=true;
		}
		else {
			needBack=false;
		}
		return needBack;
	}


	/**public int checkerNumbers(int intervalBegin, int intervalEnd) {// Count total number of checkers between cetrtain
																	// range of lanes, as basis for actions
		int count = 0;
		for (int i = intervalBegin; i < intervalEnd; i++) {
			count += CheckerLanes.get(i).countChecker();
		}
		return count;
	}**/

	public int checkerNumbers(int intervalBegin, int intervalEnd, String targetColor) {// Count total number of checkers with certain color between certain lanes
		// range of lanes, as basis for actions
		int count = 0;
		int[] checkers=new int[2];
		for (int i = intervalBegin; i < intervalEnd; i++) {
			Lane lane=CheckerLanes.get(i);
			checkers=lane.countChecker();
			if(targetColor.equals("BLACK")) {
				count+=checkers[0];
			}
			if(targetColor.equals("WHITE")) {
				count+=checkers[1];
			}
		}
		return count;
	}

	public int checkAttackedChecker(String color) {
		int num = 0;
		int count[] = new int[2];
		Lane bar = CheckerLanes.get(25);
		count = bar.countChecker();
		if (color.equals("BLACK")) {
			num = count[0];
		} else {
			num = count[1];
		}
		return num;
	}

	public int checkMovedOffChecker(String color) {
		int num = 0;
		int count[] = new int[2];
		Lane moveoff = CheckerLanes.get(0);
		count = moveoff.countChecker();
		if (color.equals("BLACK")) {
			num = count[0];
		} else {
			num = count[1];
		}
		return num;
	}

	public void showBoard(Player p1, Player p2) {
		int indexI = 12, indexII = 13;
        int bar_w=0;//white checkers on bar(get attacked)
        int bar_b = 0;// black checkers on bar(get attacked)
        System.out.print("\n                                                   " + "|       |"
                + "                              ");
        System.out.print("\n   " + indexII + "      " + ++indexII + "      " + ++indexII + "      " + ++indexII
                + "      " + ++indexII + "      " + ++indexII + "      " + "|       |      " + ++indexII + "      "
                + ++indexII + "      " + ++indexII + "      " + ++indexII + "      " + ++indexII + "      " + ++indexII
                + "      \n");
        for (int j = 12; j <= 24; j++) {
            if (this.countCheckerOnLane(j+1, p1.getPlayerColor()) == 0&&this.countCheckerOnLane(j+1, p2.getPlayerColor()) == 0) {
                System.out.print("        ");
            } else if(this.countCheckerOnLane(j+1, p1.getPlayerColor()) != 0&&this.countCheckerOnLane(j+1, p2.getPlayerColor()) == 0) {
                System.out.print(p1.getPlayerColor() + "-" + this.countCheckerOnLane(j+1, p1.getPlayerColor()) + " ");
            }else if(this.countCheckerOnLane(j+1, p1.getPlayerColor()) == 0&&this.countCheckerOnLane(j+1, p2.getPlayerColor()) != 0) {
                System.out.print(p2.getPlayerColor() + "-" + this.countCheckerOnLane(j+1, p2.getPlayerColor()) + " ");
            }
            if (j == 17) {
                System.out.print("   |       |   ");
            }
        }
        System.out.print("\n                                                   " + "|       |"
                + "                                          ");

        if (this.checkAttackedChecker(p1.getPlayerColor()) == 0&&this.checkAttackedChecker(p2.getPlayerColor())==0 ) {
            System.out.print(
                    "\n                                                   " + "|       |" + "                  ");
        } 
         if (this.checkAttackedChecker(p1.getPlayerColor()) != 0) {
            System.out.print("\n                  " + "|" + p1.getPlayerColor()+ " * " + CheckerLanes.get(25).countChecker() + "|"
                    + "                  ");
        } 
         if (this.checkAttackedChecker(p2.getPlayerColor()) != 0) {
            System.out.print("\n                  " + "|" + p2.getPlayerColor()+ " * " + CheckerLanes.get(25).countChecker() + "|"
                    + "                  ");
        }
        System.out.print("\n                                                   " + "|       |"
                + "                              ");

        System.out.print("\n   " + indexI + "      " + --indexI + "      " + --indexI + "       " + --indexI + "       "
                + --indexI + "       " + --indexI + "      " + "|       |     " + --indexI + "       " + --indexI
                + "       " + --indexI + "       " + --indexI + "       " + --indexI + "       " + --indexI
                + "       \n");
        for (int i = 11; i >= 0; i--) {
        	if (this.countCheckerOnLane(i+1, p1.getPlayerColor()) == 0&&this.countCheckerOnLane(i+1, p2.getPlayerColor()) == 0) {
                System.out.print("        ");
            } else if(this.countCheckerOnLane(i+1, p1.getPlayerColor()) != 0&&this.countCheckerOnLane(i+1, p2.getPlayerColor()) == 0) {
                System.out.print(p1.getPlayerColor() + "-" + this.countCheckerOnLane(i+1, p1.getPlayerColor()) + " ");
            }else if(this.countCheckerOnLane(i+1, p1.getPlayerColor()) == 0&&this.countCheckerOnLane(i+1, p2.getPlayerColor()) != 0) {
                System.out.print(p2.getPlayerColor() + "-" + this.countCheckerOnLane(i+1, p2.getPlayerColor()) + " ");
            }
            if (i == 6) {
                System.out.print("   |       |  ");
            }
        }
        System.out.print("\n                                                   " + "|       |"
                + "                              ");
        System.out.print("\n                                                   " + "|       |"
                + "                              ");

    }
}
	
