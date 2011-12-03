package com.excitedmuch.csm.ml.poker;

import java.util.LinkedList;

public class SimpleCircularBuffer<E> extends LinkedList<E> {

	public final int MAX_HAND_HISTORY = 10;
	
	public void addNewItem(E item) {

		this.add(item);

		if (this.size() > MAX_HAND_HISTORY) {
			this.remove(0);
		}

	}

}
