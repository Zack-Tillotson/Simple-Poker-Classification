package com.excitedmuch.csm.ml.poker;

import java.util.List;

public class PlayerAction {
	
	public final String position;
	public final String flopActions;
	public final String turnActions;
	public final String riverActions;
	public final String showdownActions;
	public final List<Card> handCards;
	
	public PlayerAction(String position, String flopActions, String turnActions, String riverActions, String showdownActions, List<Card> handCards) {

		this.position = position;
		this.flopActions = flopActions;
		this.turnActions = turnActions;
		this.riverActions = riverActions;
		this.showdownActions = showdownActions;
		this.handCards = handCards;
		
	}
	

}
