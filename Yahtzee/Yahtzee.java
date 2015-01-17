/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;
import java.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		
		//initializes each player's arrays for keeping track of score
		//only the scoring categories start with -1 to indicate that they are not selected yet
		//the others are initialized as zero
		player = new int[nPlayers + 1][N_CATEGORIES + 1];
		for (int j = 1; j <= nPlayers; j++) {
			for (int i = ONES; i <= SIXES; i++) {
				player[j][i] = -1;
			}
			for (int i = THREE_OF_A_KIND; i <= CHANCE; i++) {
				player[j][i] = -1;
			}
		}
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	private void playGame() {
		/* You fill this in */

		for (int i = ONES; i <= N_SCORING_CATEGORIES; i++) {
			for (int j = 1; j <= nPlayers; j++) {
				int playerNum = j;
				
				display.printMessage(playerNames[j - 1] + "'s turn. Please roll the dice to start!");
				display.waitForPlayerToClickRoll(j); //parameter has to be between 1 and nPlayers
				int[] dice = new int[N_DICE];
				//First roll of each round
				rollDice(dice, N_DICE);
				display.displayDice(dice);
				println(dice[0] + ", " + dice[1] + ", " + dice[2] + ", " + dice[3] + ", " + dice[4] + " --- " + player[1][3]);
				
				//Second roll of each round
				display.printMessage("Please select the dice you wish to reroll and the click on \"Roll Again\"");
				display.waitForPlayerToSelectDice();
				for (int m = 0; m < N_DICE; m++) {
					if (display.isDieSelected(m)) {
						dice[m] = reroll();
					}
				}
				display.displayDice(dice);
				println(dice[0] + ", " + dice[1] + ", " + dice[2] + ", " + dice[3] + ", " + dice[4]);
				//Third roll of each round
				display.printMessage("Please select the dice you wish to reroll and make your final roll.");
				display.waitForPlayerToSelectDice();
				for (int n = 0; n < N_DICE; n++) {
					if (display.isDieSelected(n)) {
						dice[n] = reroll();
					}
				}
				display.displayDice(dice);
				println(dice[0] + ", " + dice[1] + ", " + dice[2] + ", " + dice[3] + ", " + dice[4]);

				//After each round, the player has to choose a new category
				//The player # arrays will be -1 if it is not filled
				display.printMessage("Please choose a category");
				boolean categoryCheck = true;
				int categorySelected = 0;
				while (categoryCheck == true) {
					category = display.waitForPlayerToSelectCategory();
					//get the score for that category
					int score = getScore(dice, category);
					if (player[playerNum][category] == -1) {
						println(player[playerNum][category]);
						player[playerNum][category] = score;
						display.updateScorecard(category, playerNum, score);
						categoryCheck = false;
					}
					else {
						display.printMessage("Category already selected. Please choose another category");
					}
					categorySelected = category;

				}
				println(categorySelected + " is selected");
			}
		}
		TallyScores();
		determineWinner();
		
	}
	
	private int[] rollDice(int[] dice, int nDice) {
		for (int i = 0; i < nDice; i++) {
			dice[i] = rgen.nextInt(DICE_MIN, DICE_MAX);
		}
		return dice;
	}
	
	private int reroll() {
		int die = rgen.nextInt(DICE_MIN, DICE_MAX);
		return die;
	}
	
	private int getScore(int[] dice, int category) {
		int total = 0;
		Arrays.sort(dice);
		println(dice[0] + ", " + dice[1] + ", " + dice[2] + ", " + dice[3] + ", " + dice[4]);
		switch (category) {
			case ONES: 
				for (int i = 0; i < N_DICE; i++) {
					if (dice[i] == ONES) {
						total += dice[i];
					}
				}
				break;
			case TWOS:
				for (int i = 0; i < N_DICE; i++) {
					if (dice[i] == TWOS) {
						total += dice[i];
					}
				}
				break;
			case THREES:
				for (int i = 0; i < N_DICE; i++) {
					if (dice[i] == THREES) {
						total += dice[i];
					}
				}
				break;
			case FOURS:
				for (int i = 0; i < N_DICE; i++) {
					if (dice[i] == FOURS) {
						total += dice[i];
					}
				}
				break;
			case FIVES:
				for (int i = 0; i < N_DICE; i++) {
					if (dice[i] == FIVES) {
						total += dice[i];
					}
				}
				break;
			case SIXES:
				for (int i = 0; i < N_DICE; i++) {
					if (dice[i] == SIXES) {
						total += dice[i];
					}
				}
				break;
			case THREE_OF_A_KIND:
				//if array is sorted, then there's only 3 possibilities: 1) first 3 same;
				//2) middle 3 same; 3) last 3 are the same -- all three awards the player same of all dice values
				boolean firstThreeMatch = dice[0] == dice[1] && dice[1] == dice[2];
				boolean middleThreeMatch = dice[1] == dice[2] && dice[2] == dice[3];
				boolean lastThreeMatch = dice[2] == dice[3] && dice[3] == dice[4];
				if (firstThreeMatch || middleThreeMatch || lastThreeMatch) {
					for (int i = 0; i < N_DICE; i++) {
						total += dice[i];
					}
				}
				
				break;
			case FOUR_OF_A_KIND:
				boolean test1a = dice[0] == dice[1] && dice[1] == dice[2] && dice[2] == dice[3]; //not 5th
				boolean test2a = dice[1] == dice[2] && dice[2] == dice[3] && dice[3] == dice[4]; //not 1st
				boolean test3a = dice[0] == dice[2] && dice[2] == dice[3] && dice[3] == dice[4]; //not 2nd
				boolean test4a = dice[0] == dice[1] && dice[1] == dice[3] && dice[3] == dice[4]; //not 3rd
				boolean test5a = dice[0] == dice[1] && dice[1] == dice[2] && dice[2] == dice[4]; //not 4th
				if (test1a || test2a || test3a || test4a || test5a) {
					for (int i = 0; i < N_DICE; i++) {
						total += dice[i];
					}		
				}
				break;
			case FULL_HOUSE:
				//if the array is sorted, then there are only 2 possibilities; 1) first 3 are the same
				//and the remaining 2 are the same; 2) first 2 are the same and the remaining 3 are the same
				boolean firstThreeSame = dice[0] == dice[1] && dice[1] == dice[2];
				boolean lastTwoSame = dice[3] == dice[4];
				boolean firstTwoSame = dice[0] == dice[1];
				boolean lastThreeSame = dice[2] == dice[3] && dice[3] == dice[4];
				if ((firstThreeSame && lastTwoSame) || (firstTwoSame && lastThreeSame)) {
					total = 25;
				}
				break;
			case SMALL_STRAIGHT:
				//there are only 3 possibilities for a small straight: 1-4; 2-5; 3-6
				int smallStraightBeg1 = 1;
				int smallStraightBeg2 = 2;
				int smallStraightBeg3 = 3;
				int smallStraightMatch1 = 0;
				int smallStraightMatch2 = 0;
				int smallStraightMatch3 = 0;
				
				for (int i = 0; i < N_DICE; i++) {
					if (dice[i] == smallStraightBeg1) {
						smallStraightBeg1++;
						smallStraightMatch1++;
					} 
					if (dice[i] == smallStraightBeg2) {
						smallStraightBeg2++;
						smallStraightMatch2++;
					} 
					if (dice[i] == smallStraightBeg3) {
						smallStraightBeg3++;
						smallStraightMatch3++;
					}
				}
				if (smallStraightMatch1 == 4 || smallStraightMatch2 == 4 || smallStraightMatch3 == 4) {
					total = 30;
				}
				/*if (YahtzeeMagicStub.checkCategory(dice, SMALL_STRAIGHT)) {
					total = 30;
				}*/
				break;
			case LARGE_STRAIGHT:
				//there are only 2 possibilities for a large straight: 1-5 and 2-6
				int largeStraightBeg1 = 1;
				int largeStraightBeg2 = 2;
				int largeStraightMatch1 = 0;
				int largeStraightMatch2 = 0;
				
				for (int i = 0; i < N_DICE; i++) {
					if (dice[i] == largeStraightBeg1++) {
						largeStraightMatch1++;
					} else if (dice[i] == largeStraightBeg2++) {
						largeStraightMatch2++;
					}
				}
				if (largeStraightMatch1 == 5 || largeStraightMatch2 == 5) {
					total = 40;
				}
				
				/*if (YahtzeeMagicStub.checkCategory(dice, LARGE_STRAIGHT)) {
					total = 40;
				}*/
				
				break;
			case YAHTZEE:  
				//since all dice should be the same number, we will use the first die
				//to compare with the rest
				int diceNumForYahtzee = dice[0];
				int numOfDiceMatch = 0;
				for (int i = 0; i < N_DICE; i++) {
					if (dice[i] ==  diceNumForYahtzee) {
						numOfDiceMatch++;
					}
				}
				if (numOfDiceMatch == N_DICE) {
					total = 50;
				}
				break;
			case CHANCE:
				for (int i = 0; i < N_DICE; i++) {
					total += dice[i];
				}
				break;
			default: display.printMessage("Invalid category, please choose again.");
				break;
				
		}
		display.printMessage("Player scored " + total + " more points!" );
		return total;
	}

	private void TallyScores() {
		for (int j = 1; j <= nPlayers; j++) {
			//Upper total
			for (int i = ONES; i <= SIXES; i++) {
				player[j][UPPER_SCORE] += player[j][i];
			}
			//Lower total
			for (int i = THREE_OF_A_KIND; i <= CHANCE; i++) {
				player[j][LOWER_SCORE] += player[j][i];
			}
			//Check for upper bonus
			if (player[j][UPPER_SCORE] >= BONUS_REQUIREMENT) {
				player[j][UPPER_BONUS] = BONUS_POINTS;
			} else {
				player[j][UPPER_BONUS] = NO_BONUS;
			}
			//Tally TOTAL
			player[j][TOTAL] = player[j][UPPER_SCORE] + player[j][UPPER_BONUS] + player[j][LOWER_SCORE];
			//Display score tallys (upper, bonus, lower, total) for each player
			display.updateScorecard(UPPER_SCORE, j, player[j][UPPER_SCORE]);
			display.updateScorecard(UPPER_BONUS, j, player[j][UPPER_BONUS]);
			display.updateScorecard(LOWER_SCORE, j, player[j][LOWER_SCORE]);
			display.updateScorecard(TOTAL, j, player[j][TOTAL]);

		}
	}
	
	private void determineWinner() {
		int winnerPlaceholder = 0;
		int currentHighestScore = 0;
		for (int j = 1; j <= nPlayers; j++) {
			if (player[j][TOTAL] > currentHighestScore) {
				currentHighestScore = player[j][TOTAL]; 
				winnerPlaceholder = j;
			}				
		}
		
		display.printMessage("WINNER! WINNER! CHICKEN DINNER!!! " + playerNames[winnerPlaceholder-1] + " wins with a total score of " + currentHighestScore + "!!!");
	}
		
	
/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	int category;
	private int[][] player;
	
	//Constant variables
	private static final int DICE_MIN = 1;
	private static final int DICE_MAX = 6;
	private static final int BONUS_REQUIREMENT = 63;
	private static final int BONUS_POINTS = 35;
	private static final int NO_BONUS = 0;
}
