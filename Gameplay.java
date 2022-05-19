import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Gameplay {
	// stores the row of blank spaces
	static ArrayList<Integer> rowArray = new ArrayList<Integer>();
	// stores the column of blank spaces
	static ArrayList<Integer> columnArray = new ArrayList<Integer>();
	static boolean isValid;
	// used for resetting the board
	static int[][] baseArray = {
			{0, 0, 0, 0}, // row 0
			{0, 0, 0, 0}, //  row 1
			{0, 0, 0, 0}, // row 2
			{0, 0, 0, 0} // row 3
	};
	// the array that represents the board
	static int[][] userArray = {
			{0, 0, 0, 0}, // row 0
			{0, 0, 0, 0}, //  row 1
			{0, 0, 0, 0}, // row 2
			{0, 0, 0, 0} // row 3
	};
	// used for testing if movement options are valid
	static int[][] tempArray;	
	// highest number on the board
	static int maxNum = 0;
	static int validMoves = 0;
	static boolean gameRunning = true;
	
	// prints out the board for the game, displaying '0's as '*'s
	private static void print2DArray(int[][] ary) {
		System.out.println("-\t-\t-\t-\t-\t-\n");
		for (int row = 0; row < ary.length; ++row) {
			System.out.print("|\t");
			for (int column = 0; column < ary.length; ++column) {
				if (ary[row][column] == 0)
					System.out.print('*' + "\t");
				else
					System.out.print(ary[row][column] + "\t");
				if (ary[row][column] > maxNum) {
					maxNum = ary[row][column];
                }
				
			}			
			System.out.println("|\n");
		}
		System.out.println("-\t-\t-\t-\t-\t-");
	}
	
	// populates a new board with either a 2 or a 4 in 2 random spots on the board
	private static void initialArrayPopulation() {
		int row, row2, column, column2, value, value2, temp1, temp2 = 0;
		
		row  = randGen(1);
		column = randGen(1);
		value = randGen(2);
		value2 = randGen(2);
		
		userArray[row][column] = value;
		
		do {
			row2 = randGen(1);
			column2 = randGen(1);
			if ((row2 == row) && (column2 == column)) {
				continue;
			}
			else
				break;
		} while ((row2 == row) && (column2 == column));
		
		userArray[row2][column2] = value2;
		
	}
	
	// stores the indexes of blank spaces on the board into arrays
	private static void detectBlankSpaces() {
		rowArray.clear();
		columnArray.clear();
		for (int row = 0; row < 4; ++row) {
			for (int column = 0; column < 4; ++column) {
				if (userArray[row][column] == 0) {
					rowArray.add((Integer)row);
					columnArray.add((Integer)column);
				}
			}			
		}
	}
	
	// depending on type input, generates either a number 1-4 to be used for random index, or a number 2 or 4 to be used for random value
	private static int randGen(int type) {
		Random rand = new Random();
		int randNum, randNum2 = 0;
		
		if (type == 2) {
			randNum = rand.nextInt(10);
			if (randNum < 8) 
				return 2;
			else 
				return 4;
		}
		else {
			return rand.nextInt(4);
		}
	}
	
	// adds a random number, 2 or 4, to a blank space of the board 
	private static void newRand() {
		Random rand = new Random();
		int randIndex1, randIndex2, randRow, randColumn;
		
		detectBlankSpaces();
		
		randIndex1 = rand.nextInt(rowArray.size());
		randRow = rowArray.get(randIndex1);
		randColumn = columnArray.get(randIndex1);
		
		userArray[randRow][randColumn] = randGen(2);
		if (gameRunning) {
			System.out.print("\n\n\n\n\n\n\n\n\n\n");
			print2DArray(userArray);
		}
	}
	
	// restarts the game, maxNum, and number of validMoves
	private static void restart() {
        int[][] newArray = new int[4][4];
        userArray = newArray;
    }
	
	// checks if all spaces on board have been filled and no valid moves can be made
	private static boolean loseCheck() {
		int numFilled = 0;
		
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				if (userArray[i][j] != 0) 
					++numFilled;
			}
		}
		
		if (numFilled == 16) {
			if ((!validLeft()) && (!validRight()) && (!validUp()) && (!validDown()))
				return true;
		}
		return false;
	}
	
	// checks if a space has the value of 2048 by tracking if maxNum is 2048
	private static boolean winCheck() {
		if (maxNum == 2048)
			return true;
		else
			return false;
	}
	
	// method that calls either shift, restart, or quits the game depending on user input 
	private static void movement() {
		Scanner scnr = new Scanner(System.in);
		char dir = scnr.next().charAt(0);
		tempArray = userArray;
		if (dir == 'w') {
			if(validUp()) {
				validMoves++;
				shift('w');
			}
			else {
				System.out.println("Invalid move.");
				movement();
			}
		}
		else if (dir == 'a') 
			if(validLeft()) {
				validMoves++;
				shift('a');
			}
			else {
				System.out.println("Invalid move.");
				movement();
			}
		else if (dir == 's') 
			if(validDown()) {
				validMoves++;
				shift('s');
			}
			else {
				System.out.println("Invalid move.");
				movement();
			}
		else if (dir == 'd') 
			if(validRight()) {
				validMoves++;
				shift('d');
			}
			else {
				System.out.println("Invalid move.");
				movement();
			}
		else if (dir == 'q') { 
			System.out.println("Press 'q' again to confirm your restart.");
            dir = scnr.next().charAt(0);
            if(dir == 'q') {
            	System.out.println("Thanks for playing!");
            	gameRunning = false;
            }
		}
		else if (dir ==  'r') {
			System.out.println("Press 'r' again to confirm your restart.");
            dir = scnr.next().charAt(0);
            if(dir == 'r') {
                System.out.println("Restarting...");
                restart();
                validMoves = 0;
                maxNum = 0;
                game();
            }
		}
		else {
			System.out.println("Please enter a valid input");
			movement();
			
		}
			
	}
	
	
	// calls methods that perform the main gameplay functionality of 2048: shifting in different directions and adding like numbers
	private static void shift(char dir) {
		//up
		if (dir == 'w') {
			for (int j = 0; j < userArray.length; j++){
	            int zeroCount = 0;
	            for (int i = 0; i < userArray.length; i++){
		            if (userArray[i][j] == 0){
		                zeroCount++;
		            }
		            else{
		            	userArray[i-zeroCount][j] = userArray[i][j];
		                if (zeroCount!=0){
		                    userArray[i][j] = 0;
		                }
		            }
		        }
		    }
			for (int j = 0; j < userArray.length; j++){
	            for (int i = 1; i < userArray.length; i++){
	            	if (userArray[i][j] == userArray[i-1][j]){
	                    userArray[i-1][j] += userArray[i][j];
	                    userArray[i][j] = 0;
	                }
				}
			}
			for (int j = 0; j < userArray.length; j++){
	            int zeroCount = 0;
	            for (int i = 0; i < userArray.length; i++){
		            if (userArray[i][j] == 0){
		                zeroCount++;
		            }
		            else{
		            	userArray[i-zeroCount][j] = userArray[i][j];
		                if (zeroCount!=0){
		                    userArray[i][j] = 0;
		                }
		            }
		        }
		    }
		}
		//left
		else if (dir == 'a') {
			for (int i = 0; i < userArray.length; i++){
		        int zeroCount = 0;
		        for (int j = 0; j < userArray[i].length; j++){
		            if (userArray[i][j] == 0){
		                zeroCount++;
		            }
		            else{
		                userArray[i][j-zeroCount] = userArray[i][j];
		                if (zeroCount!=0){
		                    userArray[i][j] = 0;
		                }
		            }
		        }
		    }
			for(int i= 0; i < userArray.length - 1; i++) {
				for(int j = 1; j < userArray.length; j++) {
					if(userArray[i][j] == userArray[i][j - 1]) {
	                	userArray[i][j - 1] += userArray[i][j];
	                	userArray[i][j] = 0;
					}
				}
			}
			for (int i = 0; i < userArray.length; i++){
		        int zeroCount = 0;
		        for (int j = 0; j < userArray[i].length; j++){
		            if (userArray[i][j] == 0){
		                zeroCount++;
		            }
		            else{
		                userArray[i][j-zeroCount] = userArray[i][j];
		                if (zeroCount!=0){
		                    userArray[i][j] = 0;
		                }
		            }
		        }
		    }
		}
		//down
		else if (dir == 's') {
			for (int j = 0; j < userArray.length; j++){
		        int zeroCount = 0;
		        for (int i = userArray.length - 1; i >= 0; i--){
		            if (userArray[i][j] == 0){
		                zeroCount++;
		            }
		            else{
		                userArray[i + zeroCount][j] = userArray[i][j];
		                if (zeroCount!=0){
		                    userArray[i][j] = 0;
		                }
		            }
		        }
		    }
			for (int j = 0; j < userArray.length; j++){
	            for (int i = userArray.length-1; i > 0; i--) {
					if (userArray[i][j] == userArray[i-1][j]){
	                    userArray[i][j] += userArray[i-1][j];
	                    userArray[i-1][j] = 0;
	                } 
				}
			}
			for (int j = 0; j < userArray.length; j++){
		        int zeroCount = 0;
		        for (int i = userArray.length - 1; i >= 0; i--){
		            if (userArray[i][j] == 0){
		                zeroCount++;
		            }
		            else{
		                userArray[i + zeroCount][j] = userArray[i][j];
		                if (zeroCount!=0){
		                    userArray[i][j] = 0;
		                }
		            }
		        }
		    }
		}
		//right
		else if (dir == 'd') {
			for (int i = 0; i < userArray.length; i++){
		        int zeroCount = 0;
		        for (int j = userArray[i].length - 1; j >= 0; j--){
		            if (userArray[i][j] == 0){
		                zeroCount++;
		            }
		            else{
		                userArray[i][j + zeroCount] = userArray[i][j];
		                if (zeroCount != 0){
		                    userArray[i][j] = 0;
		                }
		            }
		        }
		    }
			for(int i= 0; i < userArray.length - 1; i++) {
				for (int j = userArray[i].length - 1; j > 0; j--){
					if (userArray[i][j - 1] == userArray[i][j]) {
	                	userArray[i][j] += userArray[i][j - 1];
	                	userArray[i][j-1] = 0;
	                }
				}
			}
			for (int i = 0; i < userArray.length; i++){
		        int zeroCount = 0;
		        for (int j = userArray[i].length - 1; j >= 0; j--){
		            if (userArray[i][j] == 0){
		                zeroCount++;
		            }
		            else{
		                userArray[i][j + zeroCount] = userArray[i][j];
		                if (zeroCount != 0){
		                    userArray[i][j] = 0;
		                }
		            }
		        }
		    }
		}          
     }
	
	
	// checks if a shift to the left will result in a valid move
	private static boolean validLeft()	{
		for (int i = 0; i <= 3; ++i) {
			for (int j = 3; j >= 1; --j) {
            	if(userArray[i][j] != 0 && userArray[i][j - 1] == 0) {
            		return true;
            	}
            	if(userArray[i][j] != 0 && userArray[i][j - 1] == userArray[i][j]) {
            		return true;
            	}
            }
		}
		return false;
	}
	
	
	// checks if a shift to the right will result in a valid move
	private static boolean validRight() {
		for (int i = 0; i <= 3; ++i) {
            for (int j = 0; j <= 2; ++j) {
            	if(userArray[i][j] != 0 && userArray[i][j + 1] == 0) {
            		return true;
            	}
            	if(userArray[i][j] != 0 && userArray[i][j] == userArray[i][j + 1]) {
            		return true;
            	}
            }
		}
		return false;
	}
	
	
	// checks if a shift up will result in a valid move
	private static boolean validUp() {
		for (int i = 3; i >= 1; --i) {
            for (int j = 0; j <= 3; ++j) {
            	if(userArray[i][j] != 0 && userArray[i - 1][j] == 0) {
            		return true;
            	}
            	if(userArray[i][j] != 0 && userArray[i][j] == userArray[i - 1][j]) {
            		return true;
            	}
            }
		}
		return false;
	}
	

	// checks if a shift down will result in a valid move
	private static boolean validDown() {
		for (int i = 0; i <= 2; ++i) {
            for (int j = 0; j <= 3; ++j) {
            	if(userArray[i][j] != 0 && userArray[i + 1][j] == 0) {
            		return true;
            	}
            	if(userArray[i][j] != 0 && userArray[i][j] == userArray[i + 1][j]) {
            		return true;
            	}
            }
		}
		return false;
	}
	

	// implements the methods above 
	private static void game() {
		initialArrayPopulation();
		print2DArray(userArray);
		System.out.println(" ");
		while (gameRunning) {
			movement();
			if (winCheck()) {
				gameRunning = false;
				System.out.println("Congratulations! You beat the game!");
				System.out.println("Highest number: " + maxNum);
				System.out.println("Valid moves: " + validMoves);
				break;
			}
			if (loseCheck()) {
				System.out.println("You lost the game!");
				System.out.println("Highest number: " + maxNum);
				System.out.println("Valid moves: " + validMoves);
				gameRunning  = false;
				break;
			}
			newRand();
			System.out.println("Highest number: " + maxNum);
			System.out.println("Valid moves: " + validMoves);
		}		
	}
	
	// implements the game method
	public static void main(String[] args) {
		game();
		
	}
}
