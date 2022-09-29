package com.adaptionsoft.games.uglytrivia;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.*;
public class Game {
    ArrayList players = new ArrayList();
    int[] places = new int[6];
    int[] purses  = new int[6];
    boolean[] inPenaltyBox  = new boolean[6];
    
    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();
    LinkedList rockQuestions = new LinkedList();
    
    int currentPlayer = 0;
    boolean isLeavingPenaltyBox;

	boolean needToClear = true;

	public void writeToFile(String input){
		try{
			if (needToClear == true){
				File output = new File("/Users/siddharth/Desktop/School/12th Grade/Software Development Lab/trivia-refactoring/testFile.txt");
				FileWriter Writer = new FileWriter(output, false);
				Writer.write("");
				Writer.close();
				needToClear = false;
			}
			File output = new File("/Users/siddharth/Desktop/School/12th Grade/Software Development Lab/trivia-refactoring/testFile.txt");
			FileWriter Writer = new FileWriter(output, true);
			Writer.write("\n" + input);
			Writer.close();
		}
		catch (IOException e){
			System.out.println("error");
		}
	}
	
    
    public  Game(){
    	for (int i = 0; i < 50; i++) {
			popQuestions.addLast("Pop Question " + i);
			scienceQuestions.addLast(("Science Question " + i));
			sportsQuestions.addLast(("Sports Question " + i));
			rockQuestions.addLast("Rock Question " + i);
    	}
    }
	
	public boolean add(String playerName) {
		
	    players.add(playerName);
	    places[players.size()] = 0;
	    purses[players.size()] = 0;
	    inPenaltyBox[players.size()] = false;
	    
	    writeToFile(playerName + " was added");
	    writeToFile("They are player number " + players.size());
		return true;
	}

	// Roll helper methods ----------------------------

	private void adjustPlaces(int roll) {
		places[currentPlayer] = places[currentPlayer] + roll;
		if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;
	}
	private void reportPlaces() {
		writeToFile(players.get(currentPlayer) 
				+ "'s new location is " 
				+ places[currentPlayer]);
		writeToFile("The category is " + currentCategory());
	}
	
	public boolean leavingPenaltyBox(int roll){
		if (roll % 2 == 1) {
			writeToFile(players.get(currentPlayer) + " is getting out of the penalty box");
			return true;
		}
		else{
			writeToFile(players.get(currentPlayer) + " is not getting out of the penalty box");
			return false;
		}
	}

	// ------------------------------------------------

	public void roll(int roll) {
		
		writeToFile(players.get(currentPlayer) + " is the current player");
		writeToFile("They have rolled a " + roll);
		
		if (inPenaltyBox[currentPlayer]) {
			isLeavingPenaltyBox = leavingPenaltyBox(roll);
			if (isLeavingPenaltyBox) {
				adjustPlaces(roll);
				reportPlaces();
				askQuestion();
			}
		}
		else {
			adjustPlaces(roll);
			reportPlaces();
			askQuestion();
		}
		
	}

	
	private void askQuestion() {
		if (currentCategory() == "Pop")
			writeToFile(popQuestions.removeFirst().toString());
		if (currentCategory() == "Science")
			writeToFile(scienceQuestions.removeFirst().toString());
		if (currentCategory() == "Sports")
			writeToFile(sportsQuestions.removeFirst().toString());
		if (currentCategory() == "Rock")
			writeToFile(rockQuestions.removeFirst().toString());		
	}
	
	
	private String currentCategory() {
		if (places[currentPlayer] % 4 == 0) return "Pop";
		if ((places[currentPlayer] - 1) % 4 == 0) return "Science";
		if ((places[currentPlayer] - 2) % 4 == 0) return "Sports";
		return "Rock";
	}

	// wasCorrectlyAnswered / wrongAnswer helper methods ----------------

	private void incrementAndDisplayPurse() {
		writeToFile("Answer was correct!!!!");
		purses[currentPlayer]++;
		writeToFile(players.get(currentPlayer) 
				+ " now has "
				+ purses[currentPlayer]
				+ " Gold Coins.");
	}
	private void nextPlayer() {
		currentPlayer++;
		if (currentPlayer == players.size()) currentPlayer = 0;
	}
	private boolean didPlayerWin() {
		return !(purses[currentPlayer] == 6);
	}

	// ------------------------------------------------------------------
	
	public boolean wasCorrectlyAnswered() {
		if (inPenaltyBox[currentPlayer]){
			if (isLeavingPenaltyBox) {
				
				incrementAndDisplayPurse();
				boolean winner = didPlayerWin();
				nextPlayer();
				return winner;
			} else {
				nextPlayer();
				return true;
			}
		} else {
			incrementAndDisplayPurse();
			boolean winner = didPlayerWin();
			nextPlayer();
			return winner;
		}
	}

	public boolean wrongAnswer(){
		writeToFile("Question was incorrectly answered");
		writeToFile(players.get(currentPlayer)+ " was sent to the penalty box");
		inPenaltyBox[currentPlayer] = true;
		nextPlayer();
		return true;
	}
	
}