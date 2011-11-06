package com.excitedmuch.csm.ml.poker;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zack Tillotson
 *
 */

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		if(args.length == 0) {
			System.err.println("Usage: java Main <directory 1> [<directory 2> <directory 3>...]");
			System.exit(1);
		}
		
		List<Flattenizer> running = new ArrayList<Flattenizer>();
		
		// For each directory given	
		for(String dir : args) {
			Flattenizer f = new Flattenizer(dir);
			f.start();
			running.add(f);
		}
		
		for(Flattenizer f : running) {
			f.join();
		}
		
	}

}
