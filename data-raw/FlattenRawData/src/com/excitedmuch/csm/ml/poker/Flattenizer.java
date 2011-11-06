package com.excitedmuch.csm.ml.poker;

import java.util.ArrayList;
import java.util.List;

public class Flattenizer extends Thread {
	
	private final List<PlayedHand> handHistory = new ArrayList<PlayedHand>();
	
	public Flattenizer(String directory) {
		
		loadHandHistory(directory, handHistory);
		loadRosterInformation(directory, handHistory);
		loadPlayerActions(directory, handHistory);
		
	}
	
	@Override
	public void run() {
		
		// For each hand
		
		// 	Get player list
		
		//	Get the players' actions, in order of position
		
		//	While more actions were taken
		
		// 		Print row
		
		
	}

}
