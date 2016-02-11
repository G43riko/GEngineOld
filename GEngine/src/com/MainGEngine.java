package com;

import com.test.GTest;
import com.test.TestGame;

public class MainGEngine {
	
	public static void main(String[] args) {
		new MainGEngine();
	}
	
	public MainGEngine(){
		GTest g = new GTest();
		g.init();
		g.loadGame(new TestGame(g));
		g.start();
	}
}
