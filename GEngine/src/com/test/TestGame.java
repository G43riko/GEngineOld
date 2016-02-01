package com.test;

import com.engine.core.Controllable;
import com.engine.core.GameAble;
import com.engine.entity.Entity;
import com.engine.entity.components.LifeComponent;

import glib.data.good.Scene;

public class TestGame implements GameAble{
	private Scene scene;
	private Controllable parent;
	
	public TestGame(Controllable parent) {
		this.parent = parent;
	}
	
	@Override
	public void init() {
		scene = new Scene(parent.getRenderingEngine()::add);
	}
	
	@Override
	public void update(float delta) {
	}

	@Override
	public void input() {
	}


}
