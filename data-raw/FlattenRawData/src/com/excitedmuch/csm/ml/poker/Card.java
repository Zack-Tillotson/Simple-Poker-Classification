package com.excitedmuch.csm.ml.poker;

public class Card {
	
	public final String rank;
	public final String suit;
	
	public Card(String val) {
		this.rank = val.substring(0,1);
		this.suit = val.substring(1,2);
	}
	
	@Override
	public String toString() {
		return rank + suit;
	}

}
