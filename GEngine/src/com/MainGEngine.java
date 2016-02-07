package com;

import com.test.GTest;
import com.test.TestGame;
import com.voxel.world.World;

public class MainGEngine {
	
	public static void main(String[] args) {
		new World();
		new MainGEngine();
		
	}
	
	public MainGEngine(){
		GTest g = new GTest();
		g.init();
		g.loadGame(new TestGame(g));
		g.start();
		
	}
}
