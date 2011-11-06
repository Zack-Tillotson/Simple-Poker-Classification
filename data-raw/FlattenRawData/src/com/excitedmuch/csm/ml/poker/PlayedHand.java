package com.excitedmuch.csm.ml.poker;

import java.util.List;

/**
 * All of the information associated with a hand of poker. The id is the unique 
 * time stamp for this hand.
 * 
 * @author Zack Tillotson
 *
 */
public class PlayedHand {
	
	private String id;
	private List<Card> boardCards;
	private List<PlayerAction> players;
	
	public PlayedHand(String id, List<Card> boardCards, List<PlayerAction> players) {
		this.id = id;
		this.boardCards = boardCards;
		this.players = players;
	}

}
