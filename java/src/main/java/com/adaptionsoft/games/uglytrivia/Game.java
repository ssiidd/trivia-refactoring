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
	
	public boolean add(String playerName) {
		
	    players.add(new Player(playerName));
	    writeToFile(playerName + " was added");
	    writeToFile("They are player number " + players.size());
		return true;
	}

	// Roll helper methods ----------------------------

	private void adjustPlaces(int roll) {
		((Player) players.get(currentPlayer)).setPlace(((Player) players.get(currentPlayer)).getPlace() + roll);
		if (((Player) players.get(currentPlayer)).getPlace() > 11){
			((Player) players.get(currentPlayer)).setPlace(((Player) players.get(currentPlayer)).getPlace() - 12);
		}
	}
	private void reportPlaces() {
		writeToFile(players.get(currentPlayer) 
				+ "'s new location is " 
				+ ((Player) players.get(currentPlayer)).getPlace());
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
		
		if (((Player) players.get(currentPlayer)).getPenaltyBoxStatus()) {
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
		if (((Player) players.get(currentPlayer)).getPlace() % 4 == 0) return "Pop";
		if ((((Player) players.get(currentPlayer)).getPlace() - 1) % 4 == 0) return "Science";
		if ((((Player) players.get(currentPlayer)).getPlace() - 2) % 4 == 0) return "Sports";
		return "Rock";
	}

	// correctAnswer / wrongAnswer helper methods ----------------

	private void incrementAndDisplayPurse() {
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
	private boolean didPlayerWin() {
		return !(((Player) players.get(currentPlayer)).getPurse() == 6);
	}

	// -----------------------------------------------------------
	
	public boolean correctAnswer() {
		if (((Player) players.get(currentPlayer)).getPenaltyBoxStatus() && !isLeavingPenaltyBox){
			nextPlayer();
			return true;
		}
		else {
			incrementAndDisplayPurse();
			boolean winner = didPlayerWin();
			nextPlayer();
			return winner;
		}
	}

	public boolean wrongAnswer(){
		writeToFile("Question was incorrectly answered");
		writeToFile(players.get(currentPlayer)+ " was sent to the penalty box");
		((Player) players.get(currentPlayer)).setPenaltyBoxStatus(true);
		nextPlayer();
		return true;
	}
	
}