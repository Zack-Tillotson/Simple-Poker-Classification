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
	private final Map<String, PlayerHistory> playerHistory = new HashMap<String, PlayerHistory>();
	private final String dir;

	public Flattenizer(String dir) throws Exception {
		this.dir = dir;
	}

	private void loadHandHistory(String directory) throws FileNotFoundException {

		Scanner in = new Scanner(new File(directory, "hdb"));

		while (in.hasNextLine()) {

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
			while (inLine.hasNext()) {
				boardCards.add(new Card(inLine.next()));
			}
			inLine.close();

			handHistory.add(new PlayedHand(id, new BigDecimal(potFlop),
					new BigDecimal(potTurn), new BigDecimal(potRiver),
					new BigDecimal(potShowdown), boardCards));

		}
		in.close();

	}

	private void loadRosterInformation(String directory) throws Exception {

		Scanner in = new Scanner(new File(directory, "hroster"));

		Iterator<PlayedHand> iter = handHistory.iterator();

		while (in.hasNextLine()) {

			String line = in.nextLine();
			Scanner inLine = new Scanner(line);

			String id = inLine.next();
			String playerCount = inLine.next();
			List<String> players = new ArrayList<String>();
			while (inLine.hasNext()) {
				players.add(inLine.next());
			}
			inLine.close();

			PlayedHand nextHand = iter.next();

			if (!id.equals(nextHand.id)) {
				throw new Exception("Missing hand information");
			}

			nextHand.playersNameList.addAll(players);

		}
		in.close();

	}

	private void loadPlayerActions(String directory) throws Exception {

		// Player Name -> Id -> Action map
		Map<String, Map<String, PlayedHandPlayer>> playerToHandMap = new HashMap<String, Map<String, PlayedHandPlayer>>();

		String pdbDir = directory + "/pdb/";
		File pDir = new File(pdbDir);
		for (String pFile : pDir.list()) {

			Map<String, PlayedHandPlayer> handToActionMap = new HashMap<String, PlayedHandPlayer>();
			String playerName = null;

			Scanner in = new Scanner(new File(pdbDir, pFile));
			while (in.hasNext()) {

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
				while (inLine.hasNext()) {
					cards.add(new Card(inLine.next()));
				}
				inLine.close();

				handToActionMap.put(id, new PlayedHandPlayer(playerName,
						position, playerNum, new BigDecimal(bankRoll), flop,
						turn, river, showdown, cards));

			}
			in.close();

			playerToHandMap.put(playerName, handToActionMap);

		}

		for (PlayedHand ph : handHistory) {

			for (String playerName : ph.playersNameList) {
				PlayedHandPlayer action = playerToHandMap.get(playerName).get(
						ph.id);
				if (action != null) {
					ph.players.add(action);
					Collections.sort(ph.players,
							new Comparator<PlayedHandPlayer>() {
								@Override
								public int compare(PlayedHandPlayer o1,
										PlayedHandPlayer o2) {
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
		// Get player list
		// Get the players' actions, in order of position
		// While more actions were taken
		// Print row

		for (PlayedHand hand : handHistory) {

			// Update the player stats
			for (PlayedHandPlayer player : hand.players) {
				PlayerHistory hist = playerHistory.get(player.name);
				if (hist == null) {
					hist = new PlayerHistory();
					playerHistory.put(player.name, hist);
				}

				hist.totHands++;
				if (!player.flopActions.equals("f")) {
					hist.playedHands++;
				}

			}

			boolean hadAction;

			// Flop actions
			hadAction = true;
			for (int round = 1; round == 1 || hadAction; round++) {
				hadAction = false;
				for (PlayedHandPlayer player : hand.players) {
					if (player.flopActions.length() >= round) {

						String action = player.flopActions.substring(round - 1,
								round);

						PlayerHistory h = playerHistory.get(player.name);
						h.totActions++;
						if (action.equals("f"))
							h.foldActions++;
						if (action.equals("r") || action.equals("B"))
							h.raiseActions++;
						
						// If we know his cards
						if(player.handCards.size() > 0) {
							
							// If he has a pocket pair
							if((player.handCards.get(0).rank.equals(player.handCards.get(1).rank))) {
								h.hasPairTotActions++;
								if (action.equals("f"))
									h.hasPairFoldActions++;
								if (action.equals("r") || action.equals("B"))
									h.hasPairRaiseActions++;								
							} else {
								h.noPairTotActions++;
								if (action.equals("f"))
									h.noPairFoldActions++;
								if (action.equals("r") || action.equals("B"))
									h.noPairRaiseActions++;
							}
														
						}

						List<Card> cards = new ArrayList<Card>();
						PlayerHandAction a = new PlayerHandAction(
								player.handCards, cards, potNormalize(
										hand.potFlop, player.chipCount),
								player.playersLeft, player.position, action);
						printRow(h, a);

						h.lastNActions.addNewItem(a);
						hadAction = true;

					}
				}
			}

		

		}

	}

	private void printRow(PlayerHistory h, PlayerHandAction a) {
		
		if (a.handCards.size() == 0) {
			return;
		} else {

			StringBuilder output = new StringBuilder();

			output.append(String.format("%s,%s,%s", h.getPlayRate(), h
					.getRaiseRate(), h.getFoldRate()));

			// For hands with pocket pairs
			output.append(String.format(",%s", h.getHasPairRaiseRate()));
			
			// For hands without pocket pairs
			output.append(String.format(",%s", h.getNoPairRaiseRate()));

			output.append("," + buildOutputString(a));

			System.out.println(output);

		}

	}

	private String buildOutputString(PlayerHandAction a) {

		String handCard1 = a.handCards.size() > 1 ? a.handCards.get(0)
				.toString() : "";
		String handCard2 = a.handCards.size() > 1 ? a.handCards.get(1)
				.toString() : "";
				
		String holePair = a.handCards.size() > 1 ? (handCard1.substring(0,1).equals(handCard2.substring(0,1)) ? "1" : "0") : "-";

		return String.format("%s,%s,%s,%s,%s", a.numPlayers, a.position, a.potSize, a.action, holePair);

	}

	private BigDecimal potNormalize(BigDecimal pot, BigDecimal value) {
		return value.divide(pot.add(BigDecimal.TEN), BigDecimal.ROUND_FLOOR); // Adds ten just cause
	}

}
