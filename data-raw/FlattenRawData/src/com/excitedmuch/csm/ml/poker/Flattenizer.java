package com.excitedmuch.csm.ml.poker;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Flattenizer extends Thread {
	
	private final List<PlayedHand> handHistory = new ArrayList<PlayedHand>();
	private final String dir;
	
	public Flattenizer(String dir) throws Exception {
		this.dir = dir;
	}
	
	private void loadHandHistory(String directory) throws FileNotFoundException {
		
		Scanner in = new Scanner(new File(directory, "hdb"));
		
		while(in.hasNextLine()) {
			
			String line = in.nextLine();
			Scanner inLine = new Scanner(line);
			
			String id = inLine.next();
			String dealer = inLine.next();
			String handCount = inLine.next();
			String playerCount = inLine.next();
			String potFlop = inLine.next().split("/")[1];
			String potTurn = inLine.next().split("/")[1];
			String potRiver = inLine.next().split("/")[1];
			String potShowdown = inLine.next().split("/")[1];
			List<Card> boardCards = new ArrayList<Card>();
			while(inLine.hasNext()) {
				boardCards.add(new Card(inLine.next()));
			}
			inLine.close();
			
			handHistory.add(new PlayedHand(id, new BigDecimal(potFlop), new BigDecimal(potTurn), new BigDecimal(potRiver), new BigDecimal(potShowdown), 
					boardCards));
			
		}
		in.close();
		
	}
	

	private void loadRosterInformation(String directory) throws Exception {
		
		Scanner in = new Scanner(new File(directory, "hroster"));
		
		Iterator<PlayedHand> iter = handHistory.iterator();
		
		while(in.hasNextLine()) {
			
			String line = in.nextLine();
			Scanner inLine = new Scanner(line);
			
			String id = inLine.next();
			String playerCount = inLine.next();
			List<String> players = new ArrayList<String>();
			while(inLine.hasNext()) {
				players.add(inLine.next());
			}
			inLine.close();
			
			PlayedHand nextHand = iter.next();
			
			if(!id.equals(nextHand.id)) {
				throw new Exception("Missing hand information");
			}
			
			nextHand.playersNameList.addAll(players);
			
		}
		in.close();
		
	}
	
	private void loadPlayerActions(String directory) throws Exception {

		// Player Name -> Id -> Action map
		Map<String, Map<String, PlayerAction>> playerToHandMap = new HashMap<String, Map<String, PlayerAction>>();
		
		String pdbDir = directory + "/pdb/";
		File pDir = new File(pdbDir);
		for(String pFile : pDir.list()) {
			
			Map<String, PlayerAction> handToActionMap = new HashMap<String, PlayerAction>();
			String playerName = null;
			
			Scanner in = new Scanner(new File(pdbDir, pFile));
			while(in.hasNext()) {
				
				String line = in.nextLine();
				Scanner inLine = new Scanner(line);
				
				playerName = inLine.next();
				String id = inLine.next();
				String playerNum = inLine.next();
				String position = inLine.next();
				String flop = inLine.next();
				String turn = inLine.next();
				String river = inLine.next();
				String showdown = inLine.next();
				String bankRoll = inLine.next();
				String betAmount = inLine.next();
				String winAmount = inLine.next();
				
				List<Card> cards = new ArrayList<Card>();
				while(inLine.hasNext()) {
					cards.add(new Card(inLine.next()));
				}
				inLine.close();
				
				handToActionMap.put(id, new PlayerAction(position, playerNum, new BigDecimal(bankRoll), flop, turn, river, showdown, cards));
				
			}
			in.close();
			
			playerToHandMap.put(playerName, handToActionMap);
			
		}
		
		for(PlayedHand ph : handHistory) {
			
			for(String playerName : ph.playersNameList) {
				PlayerAction action = playerToHandMap.get(playerName).get(ph.id);
				if(action != null) {
					ph.players.add(action);
					Collections.sort(ph.players, new Comparator<PlayerAction>() {
						@Override
						public int compare(PlayerAction o1, PlayerAction o2) {
							return o1.position.compareTo(o2.position);
						}
					});
				}
			}
			
		}
		
	}
	
	@Override
	public void run() {
		
		try {
			loadHandHistory(dir);
			loadRosterInformation(dir);
			loadPlayerActions(dir);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.err.println(e);
		}
				
		// For each hand		
		// 	Get player list		
		//	Get the players' actions, in order of position		
		//	While more actions were taken		
		// 		Print row
		
		for(PlayedHand hand : handHistory) {
			
			boolean hadAction;
			
			// Flop actions
			hadAction = true;
			for(int round = 1; round == 1 || hadAction; round++) {
				hadAction = false;
				for(PlayerAction player : hand.players) {
					if(player.flopActions.length() >= round) {
						printRow(player.handCards, hand.boardCards, player.playersLeft, player.position, 
								potNormalize(hand.potFlop, player.chipCount), player.flopActions.substring(round - 1, round));
						hadAction = true;
					}
				}
			}
			
			// Turn actions
			hadAction = true;
			for(int round = 1; round == 1 || hadAction; round++) {
				hadAction = false;
				for(PlayerAction player : hand.players) {
					if(player.turnActions.length() >= round) {
						printRow(player.handCards, hand.boardCards, player.playersLeft, player.position, 
								potNormalize(hand.potTurn, player.chipCount), player.turnActions.substring(round - 1, round));
						hadAction = true;
					}
				}
			}
			
			// River actions
			hadAction = true;
			for(int round = 1; round == 1 || hadAction; round++) {
				hadAction = false;
				for(PlayerAction player : hand.players) {
					if(player.riverActions.length() >= round) {
						printRow(player.handCards, hand.boardCards, player.playersLeft, player.position, 
								potNormalize(hand.potRiver, player.chipCount), player.riverActions.substring(round - 1, round));
						hadAction = true;
					}
				}
			}
			
			// Showdown actions
			hadAction = true;
			for(int round = 1; round == 1 || hadAction; round++) {
				hadAction = false;
				for(PlayerAction player : hand.players) {
					if(player.showdownActions.length() >= round) {
						printRow(player.handCards, hand.boardCards, player.playersLeft, player.position, 
								potNormalize(hand.potShowdown, player.chipCount), player.showdownActions.substring(round - 1, round));
						hadAction = true;
					}
				}
			}

			
		}
		
	}
	
	private void printRow(List<Card> handCards, List<Card> boardCards, String playersLeft, String position, BigDecimal potNormalized, String action) {
		
		String handCard1 = handCards.size() > 1 ? handCards.get(0).toString() : "-";
		String handCard2 = handCards.size() > 1 ? handCards.get(1).toString() : "-";
		
		String poolCard1 = boardCards.size() > 0 ? boardCards.get(0).toString() : "-";
		String poolCard2 = boardCards.size() > 1 ? boardCards.get(1).toString() : "-";
		String poolCard3 = boardCards.size() > 2 ? boardCards.get(2).toString() : "-";
		String poolCard4 = boardCards.size() > 3 ? boardCards.get(3).toString() : "-";
		String poolCard5 = boardCards.size() > 4 ? boardCards.get(4).toString() : "-";
		
		System.out.println(String.format("%s %s %s %s %s %s %s %s %s %s %s", handCard1, handCard2, poolCard1, poolCard2, poolCard3, poolCard4, poolCard5, 
				playersLeft, position, potNormalized, action));
		
	}

	private BigDecimal potNormalize(BigDecimal pot, BigDecimal value) {
		return value.divide(pot.add(BigDecimal.TEN), BigDecimal.ROUND_FLOOR); // Adds ten cause
	}
	
}
