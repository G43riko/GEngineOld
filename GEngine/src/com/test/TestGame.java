package com.test;

import com.engine.core.Controllable;
import com.engine.core.GameAble;
import com.engine.entity.Entity;
import com.engine.entity.components.ModelAndTextureComponent;

import ggllib.render.model.Model;
import glib.data.good.GScene;
import glib.network.tcp_server_client.GClient;
import glib.network.tcp_server_client.GServer;
import glib.util.Utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class TestGame implements GameAble{
	private GScene<Entity> scene;
	private Controllable parent;
	private Model model;
	public TestGame(Controllable parent) {
		this.parent = parent;
		init();
	}
	
	@Override
	public void init() {
		scene = new GScene<Entity>(parent.getRenderingEngine()::add);
		
		float[] vertices = {
				-0.5f, 0.5f, 0,
				-0.5f,-0.5f, 0,
				 0.5f,-0.5f, 0,
				 0.5f, 0.5f, 0
		};
		int[] indices = {
				0, 1, 3,
				3, 1, 2
		};
		model = parent.getContentManager().getLoader().loadToVAO(vertices, indices);
		parent.getRenderingEngine().add(model);
	}
	
	@Override
	public void update(float delta) {
//		System.out.println("aaa");
//		scene.foreach(a -> a.update(delta));
//		parent.getRenderingEngine().render(model);
	}

	@Override
	public void input() {
//		scene.foreach(a -> a.input());
	}
}
