package backgammon;

import java.util.Scanner;

public class BackGammonMain {
	private BackgammonGameProc game;

	public static void main(String[] args) {
		int gameLength=0;
		Scanner s = new Scanner(System.in);
		char inBuf;
		System.out.println("Welcome to the Backgammon game!\n");
		System.out.println("Type Y to set up your player name, or type Q to quit.\n");
		inBuf= s.next().charAt(0);
		while (!(inBuf == 'Y' || inBuf == 'y' || inBuf == 'Q' || inBuf == 'q')) {
			System.out.println("Bad input. Please try again.\n");
			inBuf = s.next().charAt(0);
		}

		if (inBuf == 'Q' || inBuf == 'q') {
			System.out.println("Byebye\n");
			System.exit(0);
		}
		if (inBuf == 'Y' || inBuf == 'y') {
			System.out.println("Set up your game length(1-5 recommanded): \n");
			gameLength=s.nextInt();
			while(gameLength<1) {
				System.out.println("Game length invalid. \n");
				gameLength=s.nextInt();
			}
		}
		System.out.println("Game starting... \n");
		for (int i=0;i<gameLength;i++) {
			BackgammonGameProc game=new BackgammonGameProc();
			game.welcomeAndInitPlayer();
			game.initGame();
			game.playGame();
		}
	}

}
