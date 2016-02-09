package com;

import com.test.GTest;
import com.test.TestGame;

import ggllib.core.Profil;
import ggllib.utils.ContentManager;
import glib.util.ResourceLoader;
import glib.util.Utils;

public class MainGEngine {
	
	public static void main(String[] args) {
		Profil p = new Profil("Gabo2");
		p.saveProfil();
	}
	
	public MainGEngine(){
		GTest g = new GTest();
		g.init();
		g.loadGame(new TestGame(g));
		g.start();
	}
}
