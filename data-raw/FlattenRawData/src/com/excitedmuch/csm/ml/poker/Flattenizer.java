package com.excitedmuch.csm.ml.poker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
			String potFlop = inLine.next();
			String potTurn = inLine.next();
			String potRiver = inLine.next();
			String potShowdown = inLine.next();
			List<Card> boardCards = new ArrayList<Card>();
			while(inLine.hasNext()) {
				boardCards.add(new Card(inLine.next()));
			}
			inLine.close();
			
			handHistory.add(new PlayedHand(id, boardCards));
			
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
				
				handToActionMap.put(id, new PlayerAction(position, flop, turn, river, showdown, cards));
				
			}
			in.close();
			
			playerToHandMap.put(playerName, handToActionMap);
			
		}
		
		for(PlayedHand ph : handHistory) {
			
			for(String playerName : ph.playersNameList) {
				PlayerAction action = playerToHandMap.get(playerName).get(ph.id);
				if(action != null) {
					ph.players.add(action);
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
		
		
		
		
	}

}
