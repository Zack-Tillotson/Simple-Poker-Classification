package com.excitedmuch.csm.ml.poker;

import java.util.ArrayList;
import java.util.List;

/**
 * All of the information associated with a hand of poker. The id is the unique 
 * time stamp for this hand.
 * 
 * @author Zack Tillotson
 *
 */
public class PlayedHand {
	
	public final String id;
	public final List<Card> boardCards;
	public final List<PlayerAction> players;
	public final List<String> playersNameList;
	
	public PlayedHand(String id, List<Card> boardCards) {
		this.id = id;
		this.boardCards = boardCards;
		this.players = new ArrayList<PlayerAction>();
		this.playersNameList = new ArrayList<String>();
	}

}
