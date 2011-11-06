package com.excitedmuch.csm.ml.poker;

import java.math.BigDecimal;
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
	public final BigDecimal potFlop;
	public final BigDecimal potTurn;
	public final BigDecimal potRiver;
	public final BigDecimal potShowdown;
	
	public PlayedHand(String id, BigDecimal potFlop, BigDecimal potTurn, BigDecimal potRiver, BigDecimal potShowdown, List<Card> boardCards) {
		this.id = id;
		this.potFlop = potFlop;
		this.potTurn = potTurn;
		this.potRiver = potRiver;
		this.potShowdown = potShowdown;
		this.boardCards = boardCards;
		this.players = new ArrayList<PlayerAction>();
		this.playersNameList = new ArrayList<String>();
	}
	
}
