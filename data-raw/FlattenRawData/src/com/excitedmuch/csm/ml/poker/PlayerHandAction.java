package com.excitedmuch.csm.ml.poker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PlayerHandAction {
	
	final List<Card> handCards = new ArrayList<Card>();
	final List<Card> boardCards = new ArrayList<Card>();
	final BigDecimal potSize;
	final String numPlayers;
	final String position;
	final String action;
	
	public PlayerHandAction(List<Card> handCards, List<Card> boardCards, BigDecimal potSize, String numPlayers, String position, String action) {
		this.potSize = potSize;
		this.handCards.addAll(handCards);
		this.boardCards.addAll(boardCards);
		this.numPlayers = numPlayers;
		this.position = position;
		this.action = action;
	}
	
}
