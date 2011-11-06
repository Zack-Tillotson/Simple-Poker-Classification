package com.excitedmuch.csm.ml.poker;

import java.math.BigDecimal;
import java.util.List;

public class PlayerAction {
	
	public final String position;
	public final String playersLeft;
	public final BigDecimal chipCount;
	public final String flopActions;
	public final String turnActions;
	public final String riverActions;
	public final String showdownActions;
	public final List<Card> handCards;
	
	public PlayerAction(String position, String playersLeft, BigDecimal chipCount, String flopActions, String turnActions, String riverActions, String showdownActions, List<Card> handCards) {

		this.position = position;
		this.playersLeft = playersLeft;
		this.chipCount = chipCount;
		this.flopActions = flopActions;
		this.turnActions = turnActions;
		this.riverActions = riverActions;
		this.showdownActions = showdownActions;
		this.handCards = handCards;
		
	}
	

}
