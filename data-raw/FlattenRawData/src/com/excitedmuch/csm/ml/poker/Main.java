package com.excitedmuch.csm.ml.poker;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zack Tillotson
 *
 */

public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		
		if(args.length == 1) {
			System.err.println("Usage: java Main <directory 1> [<directory 2> <directory 3>...]");
		}
		
		List<Flattenizer> toRun = new ArrayList<Flattenizer>();
		
		// For each directory given
		boolean skippedFirst = false;
		for(String dir : args) {
			
			if(!skippedFirst) {
				skippedFirst = true;
				continue;
			}
			
			toRun.add(new Flattenizer(dir));
			
		}
		
		for(Flattenizer f : toRun) {
			f.start();
		}
		
		for(Flattenizer f : toRun) {
			f.join();
		}
		
	}

}
