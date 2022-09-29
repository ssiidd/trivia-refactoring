package com.adaptionsoft.games.uglytrivia;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.*;

public class Game {
    ArrayList players = new ArrayList();
    
    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();
    LinkedList rockQuestions = new LinkedList();
    
    int currentPlayer = 0;
    boolean isLeavingPenaltyBox;

	// [FILE WRITER] ---------------------------
	boolean needToClear = true;
	public void writeToFile(String input){
		try{
			if (needToClear == true){
				File output = new File("/Users/testFile.txt"); // *** SELECT LOCATION FOR FILES TO BE CREATED ***
				FileWriter Writer = new FileWriter(output, false);
				Writer.write("");
				Writer.close();
				needToClear = false;
			}
			File output = new File("/Users/testFile.txt"); // *** SELECT LOCATION FOR FILES TO BE CREATED ***
			FileWriter Writer = new FileWriter(output, true);
			Writer.write("\n" + input);
			Writer.close();
		} catch (IOException e){
			System.out.println("error");
		}
	}
	// -----------------------------------------

	public class Player{
		int place;
		int purse;
		boolean inPenaltyBox;
		String name;
		
		public Player(String n){
			place = 0;
			purse = 0;
			inPenaltyBox = false;
			name = n;
		}

		public int getPlace(){
			return place;
		}
		public int getPurse(){
			return purse;
		}
		public boolean getPenaltyBoxStatus(){
			return inPenaltyBox;
		}

		public void setPlace(int pl){
			place = pl;
		}
		public void setPurse(int pu){
			purse = pu;
		}
		public void setPenaltyBoxStatus(boolean p){
			inPenaltyBox = p;
		}

		public String toString(){
			return name;
		}
	}
	
	public boolean add(String playerName) { // Adds players to the game
		
	    players.add(new Player(playerName));
	    writeToFile(playerName + " was added");
	    writeToFile("They are player number " + players.size());
		return true;
	}

	public  Game(){ // Adds questions
    	for (int i = 0; i < 50; i++) {
			popQuestions.addLast("Pop Question " + i);
			scienceQuestions.addLast(("Science Question " + i));
			sportsQuestions.addLast(("Sports Question " + i));
			rockQuestions.addLast("Rock Question " + i);
    	}
    }

	private void askQuestion() { // Prints and removes a question
		if (currentCategory() == "Pop")
			writeToFile(popQuestions.removeFirst().toString());
		if (currentCategory() == "Science")
			writeToFile(scienceQuestions.removeFirst().toString());
		if (currentCategory() == "Sports")
			writeToFile(sportsQuestions.removeFirst().toString());
		if (currentCategory() == "Rock")
			writeToFile(rockQuestions.removeFirst().toString());		
	}
	
	private String currentCategory() { // Determines category based on place
		if (((Player) players.get(currentPlayer)).getPlace() % 4 == 0) return "Pop";
		if ((((Player) players.get(currentPlayer)).getPlace() - 1) % 4 == 0) return "Science";
		if ((((Player) players.get(currentPlayer)).getPlace() - 2) % 4 == 0) return "Sports";
		return "Rock";
	}

	// Roll helper methods ----------------------------

	private void adjustPlaces(int roll) { // Adds roll to place and resets place to 0 if it exceeds 11
		((Player) players.get(currentPlayer)).setPlace(((Player) players.get(currentPlayer)).getPlace() + roll);
		if (((Player) players.get(currentPlayer)).getPlace() > 11){
			((Player) players.get(currentPlayer)).setPlace(((Player) players.get(currentPlayer)).getPlace() - 12);
		}
	}
	private void reportPlaces() { // Prints player's place
		writeToFile(players.get(currentPlayer) 
				+ "'s new location is " 
				+ ((Player) players.get(currentPlayer)).getPlace());
		writeToFile("The category is " + currentCategory());
	}
	
	public boolean leavingPenaltyBox(int roll){ // Checks if player can leave penalty box
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
		
		if (((Player) players.get(currentPlayer)).getPenaltyBoxStatus()) {
			isLeavingPenaltyBox = leavingPenaltyBox(roll);
			if (isLeavingPenaltyBox) {
				adjustPlaces(roll);
				reportPlaces();
				askQuestion();
			}
		} else {
			adjustPlaces(roll);
			reportPlaces();
			askQuestion();
		}
	}

	// correctAnswer / wrongAnswer helper methods ----------------

	private void incrementAndDisplayPurse() { // Prints correct answer message, increments purse, prints purse
		writeToFile("Answer was correct!!!!");
		((Player) players.get(currentPlayer)).setPurse(((Player) players.get(currentPlayer)).getPurse() + 1);
		writeToFile(players.get(currentPlayer) 
				+ " now has "
				+ ((Player) players.get(currentPlayer)).getPurse()
				+ " Gold Coins.");
	}
	private void nextPlayer() {
		currentPlayer++;
		if (currentPlayer == players.size()) currentPlayer = 0;
	}
	private boolean didPlayerWin() { // Player wins if they reach 6 corrects answers
		return !(((Player) players.get(currentPlayer)).getPurse() == 6);
	}

	// -----------------------------------------------------------
	
	public boolean correctAnswer() {
		if (((Player) players.get(currentPlayer)).getPenaltyBoxStatus() && !isLeavingPenaltyBox){ // Can't win if player is in penalty box and isn't leaving penalty box
			nextPlayer();
			return true;
		} else {
			incrementAndDisplayPurse();
			boolean winner = didPlayerWin(); // GameRunner class ends game if this is true
			nextPlayer();
			return winner;
		}
	}

	public boolean wrongAnswer(){
		writeToFile("Question was incorrectly answered");
		writeToFile(players.get(currentPlayer)+ " was sent to the penalty box");
		((Player) players.get(currentPlayer)).setPenaltyBoxStatus(true); // Puts player in penalty box
		nextPlayer();
		return true;
	}
}