package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;


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
    boolean isGettingOutOfPenaltyBox;

	try{
		File masterFile = new File("C:/Users/siddharth/Desktop/School/12th Grade/Software Development Lab/trivia-refactoring/masterFile.txt");
		FileWriter Writer = new FileWriter(masterFile, true);

		public  Game(){
			for (int i = 0; i < 50; i++) {
				popQuestions.addLast("Pop Question " + i);
				scienceQuestions.addLast(("Science Question " + i));
				sportsQuestions.addLast(("Sports Question " + i));
				rockQuestions.addLast(createRockQuestion(i));
			}
		}
	
		public String createRockQuestion(int index){
			return "Rock Question " + index;
		}
		
		public boolean isPlayable() {
			return (howManyPlayers() >= 2);
		}
	
		public boolean add(String playerName, FileWriter Writer) {
			
			
			players.add(playerName);
			places[howManyPlayers()] = 0;
			purses[howManyPlayers()] = 0;
			inPenaltyBox[howManyPlayers()] = false;
			
			Writer.write(playerName + " was added");
			Writer.write("They are player number " + players.size());
			return true;
		}
		
		public int howManyPlayers() {
			return players.size();
		}
	
		public void roll(int roll) {
			Writer.write(players.get(currentPlayer) + " is the current player");
			Writer.write("They have rolled a " + roll);
			
			if (inPenaltyBox[currentPlayer]) {
				if (roll % 2 != 0) {
					isGettingOutOfPenaltyBox = true;
					
					Writer.write(players.get(currentPlayer) + " is getting out of the penalty box");
					places[currentPlayer] = places[currentPlayer] + roll;
					if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;
					
					Writer.write(players.get(currentPlayer) 
							+ "'s new location is " 
							+ places[currentPlayer]);
					Writer.write("The category is " + currentCategory());
					askQuestion();
				} else {
					Writer.write(players.get(currentPlayer) + " is not getting out of the penalty box");
					isGettingOutOfPenaltyBox = false;
					}
				
			} else {
			
				places[currentPlayer] = places[currentPlayer] + roll;
				if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;
				
				Writer.write(players.get(currentPlayer) 
						+ "'s new location is " 
						+ places[currentPlayer]);
				Writer.write("The category is " + currentCategory());
				askQuestion();
			}
			
		}
	
		private void askQuestion() {
			if (currentCategory() == "Pop")
				Writer.write(popQuestions.removeFirst());
			if (currentCategory() == "Science")
				Writer.write(scienceQuestions.removeFirst());
			if (currentCategory() == "Sports")
				Writer.write(sportsQuestions.removeFirst());
			if (currentCategory() == "Rock")
				Writer.write(rockQuestions.removeFirst());		
		}
		
		
		private String currentCategory() {
			if (places[currentPlayer] == 0) return "Pop";
			if (places[currentPlayer] == 4) return "Pop";
			if (places[currentPlayer] == 8) return "Pop";
			if (places[currentPlayer] == 1) return "Science";
			if (places[currentPlayer] == 5) return "Science";
			if (places[currentPlayer] == 9) return "Science";
			if (places[currentPlayer] == 2) return "Sports";
			if (places[currentPlayer] == 6) return "Sports";
			if (places[currentPlayer] == 10) return "Sports";
			return "Rock";
		}
	
		public boolean wasCorrectlyAnswered() {
			if (inPenaltyBox[currentPlayer]){
				if (isGettingOutOfPenaltyBox) {
					Writer.write("Answer was correct!!!!");
					purses[currentPlayer]++;
					Writer.write(players.get(currentPlayer) 
							+ " now has "
							+ purses[currentPlayer]
							+ " Gold Coins.");
					
					boolean winner = didPlayerWin();
					currentPlayer++;
					if (currentPlayer == players.size()) currentPlayer = 0;
					
					return winner;
				} else {
					currentPlayer++;
					if (currentPlayer == players.size()) currentPlayer = 0;
					return true;
				}
				
				
				
			} else {
			
				Writer.write("Answer was corrent!!!!");
				purses[currentPlayer]++;
				Writer.write(players.get(currentPlayer) 
						+ " now has "
						+ purses[currentPlayer]
						+ " Gold Coins.");
				
				boolean winner = didPlayerWin();
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				
				return winner;
			}
		}
		
		public boolean wrongAnswer(){
			Writer.write("Question was incorrectly answered");
			Writer.write(players.get(currentPlayer)+ " was sent to the penalty box");
			inPenaltyBox[currentPlayer] = true;
			
			currentPlayer++;
			if (currentPlayer == players.size()) currentPlayer = 0;
			return true;
		}
	
	
		private boolean didPlayerWin() {
			return !(purses[currentPlayer] == 6);
		}
	}
	catch (IOException e){
		Writer.write("error");
	}


    
    
   
}
