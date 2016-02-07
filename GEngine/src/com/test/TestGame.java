package com.test;

import com.engine.core.Controllable;
import com.engine.core.GameAble;
import com.engine.entity.Entity;

import glib.data.good.GScene;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class TestGame implements GameAble{
	private GScene<Entity> scene;
	private Controllable parent;
	
	public TestGame(Controllable parent) {
		this.parent = parent;
	}
	
	@Override
	public void init() {
		scene = new GScene<Entity>(parent.getRenderingEngine()::add);
	}
	
	@Override
	public void update(float delta) {
		System.out.println("aaa");
//		scene.foreach(a -> a.update(delta));
	}

	@Override
	public void input() {
//		scene.foreach(a -> a.input());
	}
}
