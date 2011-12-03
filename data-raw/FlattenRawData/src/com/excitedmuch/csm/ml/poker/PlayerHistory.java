package com.excitedmuch.csm.ml.poker;

import java.math.BigDecimal;

@SuppressWarnings("serial")
public class PlayerHistory {
	
	public int totHands;
	public int playedHands;
	public int totActions;
	public int raiseActions;
	public int foldActions;
	
	public SimpleCircularBuffer<PlayerHandAction> lastNActions = new SimpleCircularBuffer<PlayerHandAction>();
	
	public String getPlayRate() {	
		BigDecimal ret = new BigDecimal(playedHands).setScale(5);
		return ret.divide(totHands == 0? BigDecimal.ONE : new BigDecimal(totHands), BigDecimal.ROUND_HALF_UP).toPlainString();
	}
	
	public String getRaiseRate() {	
		BigDecimal ret = new BigDecimal(raiseActions).setScale(5);
		return ret.divide(totActions == 0? BigDecimal.ONE : new BigDecimal(totActions), BigDecimal.ROUND_HALF_UP).toPlainString();
	}
	
	public String getFoldRate() {	
		BigDecimal ret = new BigDecimal(foldActions).setScale(5);
		return ret.divide(totActions == 0? BigDecimal.ONE : new BigDecimal(totActions), BigDecimal.ROUND_HALF_UP).toPlainString();
	}

}
